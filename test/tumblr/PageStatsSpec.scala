package tumblr

import org.specs2.mutable.Specification

import play.api.test.Helpers._

import tumblr.model._
import tumblr.dao._

object PageStatsSpec extends Specification {

  def saveSites(sites: Site *) {
    import tumblr.model.AdminSiteJSON.writes
    sites.foreach(SiteDao.save(_))
  }

  def savePages(pages: List[Page]) {
    pages.foreach(PageDao.save(_))
  }

  def getPage(slug: String, pageNumber: Int, nbViews: Int) = {
    new Page(slug, pageNumber, nbViews, List(), List(), Link.get("???", "foo", 1))
  }

  "Page Stats" should {

    "generate stats" in new FakeDaoApp {
      // Hfr images étonnantes
      val site1: model.Site = LocalSiteDao.LocalSites(0)
      // Joiesducode
      val site2: model.Site = LocalSiteDao.LocalSites(2)

      saveSites(site1, site2)

      // 6 pages for 81 views
      // Site 1 with 3 pages and 32 views
      // Site 2 with 2 pages and 48 views
      // ??? with 1 page and 1 view

      val pages: List[model.Page] = List(
        getPage(site1.slug, 1, 2),
        getPage(site1.slug, 2, 10),
        getPage(site1.slug, 3, 20),
        getPage(site2.slug, 1, 4),
        getPage(site2.slug, 2, 44),
        getPage("???", 1, 1)
      )
      savePages(pages)

      val stats = await(PageStats.generate())
      stats.nbDocuments must be equalTo(pages.size)
      stats.nbViews must be equalTo(81)
      stats.sitesStats.size must be equalTo(3)
      // Hfr images étonnantes : 3 docs
      stats.sitesStats(0).name must be equalTo(site1.name)
      stats.sitesStats(0).nbDocuments must be equalTo(3)
      stats.sitesStats(0).nbViews must be equalTo(32)
      // Joiesducode : 2 docs
      stats.sitesStats(1).name must be equalTo(site2.name)
      stats.sitesStats(1).nbDocuments must be equalTo(2)
      stats.sitesStats(1).nbViews must be equalTo(48)
      // ???
      stats.sitesStats(2).name must be equalTo("??? ???")
      stats.sitesStats(2).nbDocuments must be equalTo(1)
      stats.sitesStats(2).nbViews must be equalTo(1)
    }
  }

}
