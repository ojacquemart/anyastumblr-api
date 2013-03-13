import java.util.concurrent.TimeUnit

import concurrent.duration.Duration
import concurrent.{Await, ExecutionContext, Future}
import ExecutionContext.Implicits.global

import util.Try

import org.specs2.mutable._
import org.specs2.execute._

import play.api.Play.current
import play.api.Logger
import play.api.test._
import play.api.test.Helpers._
import play.modules.reactivemongo.ReactiveMongoPlugin

import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers.DefaultBSONDocumentWriter

import TestHelpers._

import hfr._

class PageCollectionSpec extends Specification {

  "The ContentFinder class" should {

    "save new page and retrieve it" in new FakeApp {
      val newPage = new Page("foo", 1, List("a", "b", "c"), List("d", "e", "f"))
      PageCollection.save(newPage)

      val futureOptionPage = PageCollection.findHeadByTopicIdAndPageOffset("foo", 1)
      Await.ready(futureOptionPage, Duration(5, TimeUnit.SECONDS))

      val optionPage = option(futureOptionPage)
      val page = optionPage.get
      page.topicId must be equalTo ("foo")
      page.title must be equalTo ("Page 1")
      page.offset must be equalTo (1)
      page.icons must be equalTo (List("a", "b", "c"))
      page.images must be equalTo (List("d", "e", "f"))
    }

    "save and then update existing page" in new FakeApp {
      val newPage = new Page("foo2", 2, List("a"), List("b"))
      PageCollection.save(newPage)

      // check save

      val futureOptionPage = PageCollection.findHeadByTopicIdAndPageOffset("foo2", 2)
      Await.ready(futureOptionPage, Duration(60, TimeUnit.SECONDS))

      val optionPage = option(futureOptionPage)
      optionPage must not be equalTo(None)

      val page = optionPage.get
      page.icons must be equalTo (List("a"))
      page.images must be equalTo (List("b"))

      // update

      val pageToUpdate = new Page("foo2", 2, List("a", "b", "c"), List("d", "e", "g"))
      val futureUpdate = PageCollection.update(pageToUpdate)
      Await.ready(futureUpdate, Duration(60, TimeUnit.SECONDS))

      // check update

      val futureOptionPageUpdated = PageCollection.findHeadByTopicIdAndPageOffset("foo2", 2)
      Await.ready(futureOptionPageUpdated, Duration(60, TimeUnit.SECONDS))

      val optionPageUpdated = option(futureOptionPageUpdated)
      optionPageUpdated must not be equalTo(None)

      val pageUpdated = optionPageUpdated.get
      pageUpdated.icons must be equalTo (List("a", "b", "c"))
      pageUpdated.images must be equalTo (List("d", "e", "g"))
    }

  }

}