package dao

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
    val hfrConfiguration: Configuration = HfrConfiguration.get()
    List(
      Site("Images étonnantes", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm", hfrConfiguration),
      Site("Gifs: Femmes, Caca, Chutes&Co", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm", hfrConfiguration),
      Site("Joiesducode", "http://lesjoiesducode.tumblr.com/page/1", JoiesDuCodeConfiguration.get),
      Site("Joiesdusysadmin", "http://lesjoiesdusysadmin.tumblr.com/page/1", JoiesDuCodeConfiguration.get),
      Site("Joiesdutest", "http://lesjoiesdutest.tumblr.com/page/1", JoiesDuTestConfiguration.get),
      Site("Joiesduscrum", "http://lesjoiesduscrum.tumblr.com/page/1", JoiesDuScrumConfiguration  .get)
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
