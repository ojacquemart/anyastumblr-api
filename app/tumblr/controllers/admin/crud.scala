package tumblr.controllers.admin

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api.Logger
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc.{Controller, EssentialAction, Action}

import play.autosource.reactivemongo._
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.json.collection.JSONCollection

import reactivemongo.bson.BSONObjectID

import tumblr.model._
import tumblr.model.AdminSiteJSON._

import tumblr.CacheKeys
import tumblr.dao.SiteDao
import tumblr.dao.SiteTypeDao

/**
 * Trait to check if a slug exists in a given collection.
 * TODO: clean up when mixing MongoDao with AutoSource.
 */
trait SlugChecker extends Controller {

  def res(): ReactiveMongoAutoSource[_ <: Slugifiable]

  def existsSlug(slug: String) = Action { request =>
    Async {
      val futureSlugs = res.find(Json.obj("slug" -> slug))
      futureSlugs.map(slugs => {
        val exists = slugs.size > 0
        val id = slugs.headOption.map(head => head._2.stringify).getOrElse("")
        Ok(Json.obj("exists" -> exists, "id" -> id)).as("application/json")
      })
    }

  }

}

object SiteTypeController extends ReactiveMongoAutoSourceController[SiteType] with SlugChecker {
  val coll = db.collection[JSONCollection](SiteTypeDao.collectionName)

  override def update(id: BSONObjectID) = Action(parse.json){ request =>
    Json.fromJson[SiteType](request.body)(reader).map { newSiteTpe =>
        Async {
          // Gets the old site type object.
          res.get(id).map(maybeTupleSiteTypeAndId => {
            val oldSiteType = maybeTupleSiteTypeAndId.get._1
            SiteController.changeSiteType(oldSiteType, newSiteTpe)
          })

          res.update(id, newSiteTpe).map {
            _ => Ok(Json.toJson(id)(idWriter))
          }
        }
    }.recoverTotal { e => BadRequest(JsError.toFlatJson(e)) }
  }

  override def delete(id: BSONObjectID) = Action{
    Async {
      // Remove the site types from sites.
      res.get(id).map { t =>
        SiteController.removeBySiteType(t.get._1)
      }

      res.delete(id).map{ le =>
        Ok(Json.toJson(id)(idWriter))
      }
    }
  }

}

object SiteController extends ReactiveMongoAutoSourceController[Site] with SlugChecker {
  val cacheKeysToClear = List(CacheKeys.Sites, CacheKeys.ActionSites)

  val coll = db.collection[JSONCollection](SiteDao.collectionName)

  /**
   * Changes the site type of sites, replacing the old site type by the new one.
   * @param oldSiteType the old site type.
   * @param newSiteType the new site type.
   */
  def changeSiteType(oldSiteType: SiteType, newSiteType: SiteType) = {
    Logger.debug(s"Replace ${oldSiteType.slug} by ${newSiteType.slug}")

    val selector = Json.obj("siteType.slug" -> oldSiteType.slug)
    val modifier = Json.obj(
      "$set" -> Json.obj(
        "siteType" -> newSiteType
      )
    )

    coll.update(selector, modifier, multi = true)
  }

  /**
   * Deletes the sites having the matching site type.
   * @param siteType: the site types
   */
  def removeBySiteType(siteType: SiteType) = {
    Logger.debug(s"Delete sites where ${siteType.slug}")

    coll.remove(Json.obj("siteType.slug" -> siteType.slug))
  }

  override def insert: EssentialAction = clearCache() {
    super.insert
  }

  override def update(id: BSONObjectID): EssentialAction = clearCache() {
    super.update(id)
  }

  override def updatePartial(id: BSONObjectID): EssentialAction = clearCache() {
    super.updatePartial(id)
  }

  override def delete(id: BSONObjectID): EssentialAction = clearCache() {
    super.delete(id)
  }

  override def batchInsert: EssentialAction = clearCache() {
    super.batchInsert
  }

  override def batchUpdate: EssentialAction = clearCache() {
    super.batchUpdate
  }

  def clearCache()(f: => EssentialAction): EssentialAction = {
    Logger.debug(s"Clear caches $cacheKeysToClear")
    cacheKeysToClear.foreach(Cache.remove(_))
    f
  }


}

