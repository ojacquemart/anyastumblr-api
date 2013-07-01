package tumblr.controllers.admin

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.modules.reactivemongo.json.collection.JSONCollection
import play.autosource.reactivemongo._

import tumblr.model._
import tumblr.model.AdminSiteJSON._

import tumblr.dao.SiteDao
import tumblr.dao.SiteTypeDao

object SiteTypeController extends ReactiveMongoAutoSourceController[SiteType] {
  val coll = db.collection[JSONCollection](SiteTypeDao.collectionName)
}

object SiteController extends ReactiveMongoAutoSourceController[Site] {
  val coll = db.collection[JSONCollection](SiteDao.collectionName)
}

