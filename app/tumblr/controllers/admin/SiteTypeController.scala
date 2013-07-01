package tumblr.controllers.admin

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.modules.reactivemongo.json.collection.JSONCollection
import play.autosource.reactivemongo._

import tumblr.dao.SiteTypeDao

object SiteTypeController extends ReactiveMongoAutoSourceController[JsObject] {
  val coll = db.collection[JSONCollection](SiteTypeDao.collectionName)

  override val reader = __.read[JsObject] keepAnd (
    (__ \ "name").read[String]
  )
}

