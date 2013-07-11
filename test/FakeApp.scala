import concurrent.duration.Duration
import concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global

import java.util.concurrent.TimeUnit
import play.api.Logger
import play.api.test.FakeApplication
import play.api.test.Helpers._
import play.api.Play.current

import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import reactivemongo.bson.BSONDocument

import org.specs2.execute.AsResult
import org.specs2.mutable.Around

import tumblr.dao._

trait FakeApp extends Around with org.specs2.specification.Scope {

  val TestMongoDbName = "hfr_test"

  val appTestMongoDatabase =
    Map(("mongodb.uri" -> s"mongodb://127.0.0.1:27017/$TestMongoDbName"))

  object FakeApp extends FakeApplication(
    additionalConfiguration = appTestMongoDatabase
  ) {
  }

  def around[T: AsResult](t: => T) = running(FakeApp) {
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

    // Run tests inside a fake application
    AsResult(t)
  }
}