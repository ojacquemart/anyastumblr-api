package tumblr

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api.cache.Cache
import play.api.Logger

import tumblr.model.{Site, Link}
import tumblr.dao.SiteDao

/**
 * Object to retrieve the last page information.
 */
object SiteLastPageInfos {

  /**
   * This methods return an Option[Link], giving the last page of the tumblr site.
   *
   * @param siteId the site id to parse the last page informations through a css query.
   * @return if the site permits to retrieve a last page, a link with the informations is returned.
   */
  def get(siteId: String): Future[Option[Link]] = {
    // Cache for 5 minutes the last page number from the site.
    Cache.getOrElse[Future[Option[Link]]](s"site.$siteId", 300) {
      for {
        maybeSite <- SiteDao.findSiteById(siteId)
      } yield getLink(maybeSite.get)
    }
  }

  def getLink(site: Site): Option[Link] = {
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
