package tumblr.services

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api.cache.Cache

import play.api.Logger

import tumblr.CacheKeys
import tumblr.model._
import tumblr.content.{SiteLastPageResolver, PageContentFinder}
import tumblr.dao.{PageDao, SiteDao}

object SiteService {

  def findAllGroupedBySiteType(): Future[SitesInfo] = {
    Cache.getOrElse[Future[SitesInfo]](CacheKeys.Sites) {
      import tumblr.model.AdminSiteJSON.format

      Logger.info("Reading sites content...")

      for {
        sites <- SiteDao.findAll()
        sitesByType = compute(sites)
      } yield SitesInfo(sitesByType, sites)
    }
  }

  private def compute(sites: List[Site]): List[SitesByType] = {
    val group = sites.groupBy(_.siteType)
    group.map(g => SitesByType(g._1, g._2)).toList.sortBy(_.siteType.ordinal)
  }

  def getPage(slug: String, pageNumber: Option[Int] = None): Future[PageWithTotal] = {
    Logger.debug(s"Get page $pageNumber for site $slug")
    val futurePageWithTotal = for {
      maybeSite <- SiteDao.findBySlug(slug)
      site      = maybeSite.get
      page      = PageContentFinder.get(site, pageNumber).getContent()
      totalPage = SiteLastPageResolver.get(site)
    } yield PageWithTotal(page, totalPage)

    futurePageWithTotal.onSuccess { case pageWithTotal =>
      PageDao.saveOrUpdate(pageWithTotal.page)
    }

    futurePageWithTotal
  }

}
