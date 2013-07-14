package test.tumblr.dao

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api.Logger

import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import reactivemongo.bson.BSONDocument

import tumblr.dao._

import test.utils.SimpleFakeApp

trait FakeDaoApp extends SimpleFakeApp {

  val TestMongoDbName = "hfr_test"

  override val testAdditionalConfig: Map[String, _] = Map(("mongodb.uri" -> s"mongodb://127.0.0.1:27017/$TestMongoDbName"))

  override def doAround(): Unit = {
    Logger.debug("Running test ==================================")
    Logger.debug("Clear test database ===========================")

    def clearCollections(collectionsNames: String*) = {
      collectionsNames.foreach(collectionName => {
        Logger.debug(s"\tClear collection: $collectionName")
        val futureRemove = ReactiveMongoPlugin.db.collection[BSONCollection](collectionName).remove(BSONDocument())
        Await.ready(futureRemove, Duration.create(1, "min"))
      })
    }

    clearCollections(PageDao.collectionName, SiteTypeDao.collectionName, UserDao.collectionName)
  }
}