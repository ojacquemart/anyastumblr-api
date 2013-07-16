package test.tumblr.dao

import org.specs2.mutable._

import play.api.Logger
import play.api.test.Helpers._

import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

import tumblr.dao._
import tumblr.model._


class SiteTypeDaoSpec extends Specification {

  "The SiteTypeDao" should {

    def saveItem() = {
      val newType = SiteType.get("foo")
      Logger.debug("Save new type")
      SiteTypeDao.save(newType)

      newType
    }

    def saveItemAndGetPK() = saveItem().id.get

    def findByPK(id: BSONObjectID) = {
      val maybeItem: Option[SiteType] = await(SiteTypeDao.findByPK(id))
      maybeItem must not be equalTo(None)
      maybeItem.get
    }

    "save" in new FakeDaoApp {
      val newItem = saveItem()

      Logger.debug("Check count > 0")
      val count = await(SiteTypeDao.count())
      count must be > (0)

      Logger.debug("Check name is retrievable")
      val types = await(SiteTypeDao.findAll())
      types must not be empty
      types.head.name must be equalTo (newItem.name)
    }

    "update (using findByPK)" in new FakeDaoApp {
      val newItem = saveItem()
      val PK = newItem.id.get

      val newItemChanged = findByPK(PK)
      newItemChanged.name must be equalTo(newItem.name)
      newItemChanged.name = "foo2"

      SiteTypeDao.save(newItemChanged)

      val itemUpdated = findByPK(PK)
      itemUpdated.name must be equalTo (newItemChanged.name)
    }

    "remove all documents" in new FakeDaoApp {
      saveItem()
      SiteTypeDao.removeAll()
    }

    "remove one document by PK" in new FakeDaoApp {
      val PK = saveItemAndGetPK()
      SiteTypeDao.remove(PK)

    }

  }

}