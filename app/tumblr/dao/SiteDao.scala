package tumblr.dao

import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.json._
import play.api.Logger

import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson.BSONObjectID

import tumblr.CacheKeys
import tumblr.model._
import tumblr.model.AdminSiteJSON._

object SiteDao extends MongoDao[Site, BSONObjectID] {

  val collectionName = "sites"

  override val sorter = Json.obj("siteType.ordinal" -> 1, "ordinal" -> 1, "name:" -> 1)
  override val finder = Json.obj("siteType.enabled" -> true, "enabled" -> true)

  override def findAll()(implicit reader: Reads[Site]): Future[List[Site]] = {
    Cache.getOrElse[Future[List[Site]]](CacheKeys.Sites) {

      Logger.info("Reading sites content...")
      super.findAll()
    }
  }

  def findBySlug(slug: String): Future[Option[Site]] = {
    debug(s"findBySlug($slug)")

    collection.find(Json.obj("slug" -> slug)).one[Site]
  }

}
