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

import TestAwait._

import dao._
import model._
import tumblr._

class PageDaoSpec extends Specification {

  "The PageDao class" should {

    "save new page and retrieve it" in new FakeApp {
      val images_1 = List(Image.get("a"), Image.get("b"), Image.get("c"))
      val images_2 = List(Image.get("d"), Image.get("e"), Image.get("f"))
      val newPage = new Page("foo", 1, images_1, images_2)
      Logger.debug("Save")
      PageDao.save(newPage)

      Logger.debug("Check save")
      val optionPage = result(PageDao.findHeadByTopicIdAndPageOffset("foo", 1))

      Logger.debug("Item found!")
      
      val page = optionPage.get
      page.siteId must be equalTo ("foo")
      page.pageNumber must be equalTo (1)
      page.images_1 must be equalTo images_1
      page.images_2 must be equalTo images_2
    }

    "save and then update existing page" in new FakeApp {
      val images_1 = List(Image.get("a"))
      val images_2 = List(Image.get("d"))
      val newPage = new Page("foo2", 2, images_1, images_2)
      Logger.debug("Save")
      result(PageDao.save(newPage))

      Logger.debug("Check save")
      val optionPage = result(PageDao.findHeadByTopicIdAndPageOffset("foo2", 2))
      optionPage must not be equalTo(None)
      Logger.debug("Item found!")

      val page = optionPage.get
      page.images_1 must be equalTo images_1
      page.images_2 must be equalTo images_2

      val images_1ToUpdate = List(Image.get("a"), Image.get("b"), Image.get("c"))
      val images_2ToUpdate = List(Image.get("d"), Image.get("e"), Image.get("g"))
      val pageToUpdate = new Page("foo2", 2, images_1ToUpdate, images_2ToUpdate)
      Logger.debug("Update...")
      result(PageDao.update(pageToUpdate))

      Logger.debug("Check update")

      val optionPageUpdated = result(PageDao.findHeadByTopicIdAndPageOffset("foo2", 2))
      optionPageUpdated must not be equalTo(None)
     val pageUpdated = optionPageUpdated.get
      pageUpdated.images_1 must be equalTo(images_1ToUpdate)
      pageUpdated.images_2 must be equalTo(images_2ToUpdate)
      Logger.debug("Item updated!")
    }

    "count documents" in new FakeApp {
      val images_1 = List(Image.get("a"))
      val images_2 = List(Image.get("d"))
      val newPage = new Page("foo2", 2, images_1, images_2)
      Logger.debug("Save one item")
      PageDao.save(newPage)

      val count = result(PageDao.count())
      count must be equalTo(1)
      Logger.debug("Foud one item!")
    }

  }

}