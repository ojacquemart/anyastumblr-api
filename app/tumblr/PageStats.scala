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

  implicit val siteStatWrites = Json.writes[SiteStat]
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

  def compute(sites: List[Site], stats: Stream[BSONDocument]): List[SiteStat] = {
    sites.map(site => {
      val maybeStat = stats.find(doc => doc.getAs[BSONString]("_id").get.value == site.slug)
      maybeStat match {
        case None => SiteStat(site.name, 0, 0)
        case Some(doc) => {
          val nbDocs = bsonNumberLikeReader.read(doc.get("nbDocs").get).toInt
          val nbViews = bsonNumberLikeReader.read(doc.get("nbViews").get).toInt
          Logger.debug(s"Computed site ${site.name} with $nbDocs docs and $nbViews views")

          SiteStat(site.name, nbDocs, nbViews)
        }
      }
    }).sortBy(- _.nbDocuments)
  }

  def sumNbDocuments(l: List[SiteStat]): Int = l.map(_.nbDocuments).sum
  def sumNbViews(l: List[SiteStat]): Int = l.map(_.nbViews).sum

}