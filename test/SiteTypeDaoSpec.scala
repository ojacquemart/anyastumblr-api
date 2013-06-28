import org.specs2.mutable._

import play.api.Logger

import reactivemongo.bson.BSONObjectID
import TestAwait._

import tumblr.dao._
import tumblr.model._

import play.modules.reactivemongo.json.BSONFormats._

class SiteTypeDaoSpec extends Specification {

  "The SiteTypeDao" should {

    def saveItem() = {
      val newType = SiteType.get("foo")
      Logger.debug("Save new type")
      SiteTypeDao.save(newType)

      newType
    }

    "save new type" in new FakeApp {
     val newItem = saveItem()

      Logger.debug("Check count > 0")
      val count = result(SiteTypeDao.count())
      count must be>(0)

      Logger.debug("Check name is retrievable")
      val types = result(SiteTypeDao.findAll())
      types must not be empty
      types.head.name must be equalTo(newItem.name)
    }

    "save and update existing type" in new FakeApp {
      saveItem()

      Logger.debug("Find head")
      val types = result(SiteTypeDao.findAll())
      val head = types.head
      head.name = "foo2"

      Logger.debug(s"Update $head")
      SiteTypeDao.save(head)

      Logger.debug("Check update...")
      val updates = result(SiteTypeDao.findAll())
      updates.head.name must be equalTo(head.name)
    }

    "find by id" in new FakeApp {
      val newItem = saveItem()

      Logger.debug("Find by PK")
      val maybeItem = result(SiteTypeDao.findByPK(newItem.id.get))
      maybeItem must not be equalTo(None)

      val item = maybeItem.get
      item.name must be equalTo(newItem.name)

      Logger.debug(s"Found item $item")
    }

    "remove all documents" in new FakeApp {
      saveItem()
      SiteTypeDao.removeAll()
    }

  }

}