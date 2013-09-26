package tumblr.model

import play.api.libs.json.Json

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