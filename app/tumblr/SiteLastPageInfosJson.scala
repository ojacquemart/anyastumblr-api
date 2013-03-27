package tumblr

import play.api.Play.current
import play.api.libs.json._

import model.Link
import dao.SiteDao
import play.api.cache.Cache

object SiteLastPageInfos {

  def getAsJson(siteId: String) = {
    import model.LinkJSON.Writer
    Json.toJson(get(siteId))
  }

  def get(siteId: String): Link = {
    Cache.getOrElse[Link](s"site.$siteId", 300 ) {
      val site = SiteDao.findSite(siteId)

      val resolver: PageNumberResolver = new PageNumberResolver(site)
      val lastPageNumber = resolver.getLastPageNumber()
      val lastPageUrl = resolver.getPageUrl(lastPageNumber)

      Link(lastPageUrl, site.name, lastPageNumber)
    }
  }

}
