package tumblr.dao

import scala.concurrent.Future

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.json._
import play.api.Logger

import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson.BSONObjectID

import tumblr.model._
import tumblr.model.AdminSiteJSON._

object SiteDao extends MongoDao[Site, BSONObjectID] {

  val collectionName = "sites"

  override def findAll()(implicit reader: Reads[Site]): Future[List[Site]] = {
    Cache.getOrElse[Future[List[Site]]]("dao.topics") {
      Logger.info("Reading sites content...")
      super.findAll()
    }
  }

  def findSiteById(siteId: String): Future[Option[Site]] = {
    Logger.debug(s"site id: $siteId")

    findByPK(BSONObjectID(siteId))
  }

}
