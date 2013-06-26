package dao

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.json._
import play.api.libs.json.util._
import play.api.libs.json.Writes._
import play.api.Logger

import model._
import model.Site.SiteWrites.writes

/**
 * TODO: use mongodb to store sites and configurations.
 */
object SiteDao {

  def getSites() = {
    Cache.getOrElse[List[Site]]("dao.topics") {
      Logger.info("Reading sites content...")

      val hfrConfiguration: Configuration = HfrConfiguration.get()

      List(
        Site.get(SiteType.HFR, "Images étonnantes", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm", hfrConfiguration),
        Site.get(SiteType.HFR, "Gifs: Femmes, Caca, Chutes&Co", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm", hfrConfiguration),
        Site.get(SiteType.LESJOIES, "Joiesducode", "http://lesjoiesducode.tumblr.com/page/1", JoiesDuCodeConfiguration.get),
        Site.get(SiteType.LESJOIES, "Joiesdusysadmin", "http://lesjoiesdusysadmin.tumblr.com/page/1", JoiesDuSysadminConfiguration.get),
        Site.get(SiteType.LESJOIES, "Joiesdutest", "http://lesjoiesdutest.tumblr.com/page/1", JoiesDuTestConfiguration.get),
        Site.get(SiteType.LESJOIES, "Joiesduscrum", "http://lesjoiesduscrum.tumblr.com/page/1", JoiesDuScrumConfiguration.get),
        Site.get(SiteType.MISC, "ActressesWithoutTeeth", "http://actresseswithoutteeth.net/page/1", ActressesWithoutTeeth.get),
        Site.get(SiteType.MISC, "ChersVoisins", "http://chersvoisins.tumblr.com/page/1", ChersVoisinsConfiguration.get),
        Site.get(SiteType.MISC, "CommitStrip", "http://www.commitstrip.com/page/1", CommitStripConfiguration.get),
        Site.get(SiteType.MISC, "DataAnxiety", "http://dataanxiety.tumblr.com/page/1", DataAnxietyConfiguration.get),
        Site.get(SiteType.MISC, "Failbog.fr", "http://failblog.fr/fail/page-1.html", FailBlogFrConfiguration.get),
        Site.get(SiteType.MISC, "N'oubliez jamais la capote", "http://noubliezjamaislacapote.tumblr.com/page/1/", DontForgetCondomConfiguration.get),
        Site.get(SiteType.MISC, "Sportballsreplacedwithcats", "http://sportballsreplacedwithcats.tumblr.com/page/1", SportBallsReplacedWithCatsConfiguration.get)
      )
    }
  }

  def getSitesAsJson() = Json.toJson(getSites())

  def getFirstSite() = getSites().head

  def getDefaultSite() = getFirstSite()

  def findSite(siteId: String): Site = {
    val topic = getSites().find(_.id == siteId).getOrElse(getDefaultSite())
    Logger.info("Get siteId %s from siteId %s".format(topic.name, siteId))
    topic
  }

  def findSiteUrl(id: String) = findSite(id).url

}
