package tumblr.content

import play.api.Play.current
import play.api.cache.Cache
import play.api.Logger

import tumblr.model.{Site, Link}

/**
 * Object to retrieve the last page information.
 */
object SiteLastPageResolver {

  /**
   * This methods return an Option[Link], giving the last page of the site.
   *
   * @param site the site  to parse the last page informations through a css query.
   * @return if the site permits to retrieve a last page, a link with the informations is returned.
   */
  def get(site: Site): Option[Link] = {
    // Cache for 5 minutes the last page number for the site.
    Cache.getOrElse[Option[Link]](s"site.${site.slug}", 300) {
      getLink(site)
    }
  }

  def getLink(site: Site): Option[Link] = {
    val lastPageByCss = site.configuration.lastPageByCss
    if (!lastPageByCss) {
      None
    } else {
      Logger.debug(s"Get last page by css for ${site.name}")

      val resolver: PageNumberResolver = new PageNumberResolver(site)
      val lastPageNumber = resolver.getLastPageNumber()
      val lastPageUrl = resolver.getPageUrl(lastPageNumber)

      Some(Link.get(lastPageUrl, site.name, lastPageNumber))
    }
  }

}
