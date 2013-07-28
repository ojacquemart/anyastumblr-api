package tumblr

import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

import play.api.Logger
import play.api.libs.json._

import reactivemongo.bson._
import tumblr.model.Site
import tumblr.dao.{PageDao, SiteDao}

trait ViewableStats {
  def nbDocuments: Int
  def nbViews: Int
}

case class Stats(nbDocuments: Int, nbViews: Int, sitesStats: List[SiteStat]) extends ViewableStats
case class SiteStat(name: String, nbDocuments: Int, nbViews: Int) extends ViewableStats

object Stats {

  implicit val siteStatwWites = Json.writes[SiteStat]
  implicit val writes = Json.writes[Stats]

}

object PageStats {

  /**
   * Reader to read BSONNumberLike as Int.
   */
  private val bsonNumberLikeReader = new BSONNumberLikeReader[BSONValue]()

  def generate(): Future[Stats] = {
    import tumblr.model.AdminSiteJSON.format
    for {
      sites <- SiteDao.findAll()
      stats <- PageDao.statsBySiteId()
      sitesStats = compute(sites, stats)
      nbDocs = sumNbDocuments(sitesStats)
      nbViews = sumNbViews(sitesStats)
    } yield Stats(nbDocs, nbViews, sitesStats)
  }

  def compute(sites: List[Site], docs: Stream[BSONDocument]) = {
    docs.map(doc => {
      val slug = doc.getAs[BSONString]("_id").get.value
      val siteName = sites.find(_.slug == slug).map(_.name).getOrElse("??? " + slug)

      val nbDocs = bsonNumberLikeReader.read(doc.get("nbDocs").get).toInt
      val nbViews = bsonNumberLikeReader.read(doc.get("nbViews").get).toInt
      Logger.debug(s"Computed site $siteName with $nbDocs docs and $nbViews views")

      SiteStat(siteName, nbDocs, nbViews)
    }).toList
  }

  def sumNbDocuments(l: List[SiteStat]): Int = l.map(_.nbDocuments).sum
  def sumNbViews(l: List[SiteStat]): Int = l.map(_.nbViews).sum

}