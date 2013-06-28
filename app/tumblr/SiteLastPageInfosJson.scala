package tumblr

import play.api.Play.current
import play.api.cache.Cache
import play.api.Logger
import play.api.libs.json._

import tumblr.model.Link
import tumblr.dao.SiteDao

/**
 * Object to retrieve the last page information.
 */
object SiteLastPageInfos {

  /**
   * The site may not permit to give the last page. In that case, an empty json object is returned.
   */
  def getAsJson(siteId: String) = {
    import Link.writes

    get(siteId) match {
      case None => JsNull
      case Some(link: Link) => Json.toJson(link)
    }
  }

  /**
   * This methods return an Option[Link], giving the last page of the tumblr site.
   *
   * @param siteId the site id to parse the last page informations through a css query.
   * @return if the site permits to retrieve a last page, a link with the informations is returned.
   */
  def get(siteId: String): Option[Link] = {
    // Cache for 5 minutes the last page number from the site.
    Cache.getOrElse[Option[Link]](s"site.$siteId", 300) {
      val site = SiteDao.findSite(siteId)
      val lastPageByCss = site.configuration.lastPageByCss
      Logger.debug(s"Get last page by css from ${site.name}=$lastPageByCss")

      if (!lastPageByCss) {
        None
      } else {
        val resolver: PageNumberResolver = new PageNumberResolver(site)
        val lastPageNumber = resolver.getLastPageNumber()
        val lastPageUrl = resolver.getPageUrl(lastPageNumber)

        Some(Link.get(lastPageUrl, site.name, lastPageNumber))
      }
    }
  }

}
