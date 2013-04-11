package dao

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.json._
import play.api.Logger

import model._
import model.Configuration

/**
 * TODO: use mongodb to store sites and configurations.
 */
object SiteDao {

  import SiteJSON._

  def getSites() = {
    Cache.getOrElse[List[Site]]("dao.topics") {
      Logger.info("Reading sites content...")

      val hfrConfiguration: Configuration = HfrConfiguration.get()

      List(
        Site(SiteType.HFR, "Images étonnantes", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm", hfrConfiguration),
        Site(SiteType.HFR, "Gifs: Femmes, Caca, Chutes&Co", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm", hfrConfiguration),
        Site(SiteType.LESJOIES, "Joiesducode", "http://lesjoiesducode.tumblr.com/page/1", JoiesDuCodeConfiguration.get),
        Site(SiteType.LESJOIES, "Joiesdusysadmin", "http://lesjoiesdusysadmin.tumblr.com/page/1", JoiesDuSysadminConfiguration.get),
        Site(SiteType.LESJOIES, "Joiesdutest", "http://lesjoiesdutest.tumblr.com/page/1", JoiesDuTestConfiguration.get),
        Site(SiteType.LESJOIES, "Joiesduscrum", "http://lesjoiesduscrum.tumblr.com/page/1", JoiesDuScrumConfiguration.get),
        Site(SiteType.MISC, "CommitStrip", "http://www.commitstrip.com/page/1", CommitStripConfiguration.get),
        Site(SiteType.MISC, "N'oubliez jamais la capote", "http://noubliezjamaislacapote.tumblr.com/page/2/", DontForgetCondomConfiguration.get),
        Site(SiteType.MISC, "Failbog.fr", "http://failblog.fr/fail/page-1.html", FailBlogFrConfiguration.get)
      )
    }
  }

  def getSitesAsJson() = Json.toJson(getSites())

  def getFirstSite() = getSites().head

  def getDefaultSite() = getSites().head

  def findSite(siteId: String): Site = {
    val topic = getSites().find(_.id == siteId).getOrElse(getDefaultSite())
    Logger.info("Get siteId %s from siteId %s".format(topic.name, siteId))
    topic
  }

  def findSiteUrl(id: String) = findSite(id).url

}
