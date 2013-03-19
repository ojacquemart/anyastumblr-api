package hfr

import play.api.libs.json._
import play.api.Logger

object SiteRepository {

  import SiteJSON._

  def getSites() = {
    val hfrConfiguration: Configuration = HfrConfiguration.get()
    List(
      Site("Images étonnantes", "http://forum.hardware.fr/hfr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm", hfrConfiguration),
      Site("Gifs: Femmes, Caca, Chutes&Co", "http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm", hfrConfiguration),
      Site("Joiesducode", "http://lesjoiesducode.tumblr.com/page/1", JoiesDuCodeConfiguration.get),
      Site("Joiesdusysadmin", "http://lesjoiesdusysadmin.tumblr.com/page/1", JoiesDuCodeConfiguration.get)
    )
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
