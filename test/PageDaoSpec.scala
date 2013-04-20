import org.specs2.mutable._
import org.specs2.execute._

import java.util.concurrent.TimeUnit

import concurrent.duration.Duration
import concurrent.{Await, ExecutionContext, Future}
import ExecutionContext.Implicits.global
import util.Try

import play.api.Play.current
import play.api.Logger
import play.api.test._
import play.api.test.Helpers._
import play.modules.reactivemongo.ReactiveMongoPlugin

import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers.DefaultBSONDocumentWriter

import dao._
import model._
import tumblr._

import TestHelpers._

class PageDaoSpec extends Specification {

  "The PageDao class" should {

    "save new page and retrieve it" in new FakeApp {
      val images_1 = List(Image("a"), Image("b"), Image("c"))
      val images_2 = List(Image("d"), Image("e"), Image("f"))
      val newPage = new Page("foo", 1, images_1, images_2)
      PageDao.save(newPage)

      val futureOptionPage = PageDao.findHeadByTopicIdAndPageOffset("foo", 1)
      Await.ready(futureOptionPage, Duration(5, TimeUnit.SECONDS))

      val optionPage = option(futureOptionPage)
      val page = optionPage.get
      page.siteId must be equalTo ("foo")
      page.pageNumber must be equalTo (1)
      page.images_1 must be equalTo images_1
      page.images_2 must be equalTo images_2
    }

    "save and then update existing page" in new FakeApp {
      val images_1 = List(Image("a"))
      val images_2 = List(Image("d"))
      val newPage = new Page("foo2", 2, images_1, images_2)
      PageDao.save(newPage)

      // check save

      val futureOptionPage = PageDao.findHeadByTopicIdAndPageOffset("foo2", 2)
      Await.ready(futureOptionPage, Duration(60, TimeUnit.SECONDS))

      val optionPage = option(futureOptionPage)
      optionPage must not be equalTo(None)

      val page = optionPage.get
      page.images_1 must be equalTo images_1
      page.images_2 must be equalTo images_2

      // update

      val images_1ToUpdate = List(Image("a"), Image("b"), Image("c"))
      val images_2ToUpdate = List(Image("d"), Image("e"), Image("g"))
      val pageToUpdate = new Page("foo2", 2, images_1ToUpdate, images_2ToUpdate)
      val futureUpdate = PageDao.update(pageToUpdate)
      Await.ready(futureUpdate, Duration(60, TimeUnit.SECONDS))

      // check update

      val futureOptionPageUpdated = PageDao.findHeadByTopicIdAndPageOffset("foo2", 2)
      Await.ready(futureOptionPageUpdated, Duration(60, TimeUnit.SECONDS))

      val optionPageUpdated = option(futureOptionPageUpdated)
      optionPageUpdated must not be equalTo(None)

      val pageUpdated = optionPageUpdated.get
      pageUpdated.images_1 must be equalTo(images_1ToUpdate)
      pageUpdated.images_2 must be equalTo(images_2ToUpdate)
    }

    "count documents" in new FakeApp {
      val images_1 = List(Image("a"))
      val images_2 = List(Image("d"))
      val newPage = new Page("foo2", 2, images_1, images_2)
      PageDao.save(newPage)

      val futureOptionPage = PageDao.findHeadByTopicIdAndPageOffset("foo2", 2)
      Await.ready(futureOptionPage, Duration(60, TimeUnit.SECONDS))

      val futureCount = PageDao.count()
      Await.ready(futureCount, Duration(60, TimeUnit.SECONDS))
      val count = simple(futureCount)
      count must be equalTo(1)
    }

  }

}