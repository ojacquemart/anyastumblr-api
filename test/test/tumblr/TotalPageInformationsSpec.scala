package test.tumblr

import org.specs2.mutable._

import tumblr.model._
import tumblr._

import test.tumblr.dao.LocalSiteDao._

class TotalPageInformationsSpec extends Specification {

  def getSiteByNameLowerCase(name: String): Site = {
    val maybeSite = LocalSites.find(site => site.name.toLowerCase() == name)
    maybeSite must not be equalTo(None)

    val site: tumblr.model.Site = maybeSite.get
    site.name.toLowerCase() must be equalTo(name)
    site
  }

  def getLastPageNumber(name: String): Int = {
    val site = getSiteByNameLowerCase(name)
    val resolver = new PageNumberResolver(site, None)
    val lastPageNumber = resolver.getLastPageNumber()

    lastPageNumber
  }

  "The SiteLastPageInfosJson class" should {

    "returns None when the site doesn't expose its total page number" in {
      val site = getSiteByNameLowerCase("commitstrip")

      SiteLastPageInfos.getLink(site) must be equalTo(None)
    }
  }

  "The PageNumberResolver class" should {

    def getFirstSitePageNumber(pageNumber: Option[Int]) = {
      val site = LocalSites.head

      val resolver = new PageNumberResolver(site, pageNumber)
      val urlAndPageNumber = resolver.resolve

      val url = urlAndPageNumber._1
      url must not be equalTo(site.url)

      urlAndPageNumber._2
    }

    "resolve hfr page number from None and Some given number" in {
      getFirstSitePageNumber(None) must be > (1)
      getFirstSitePageNumber(Some(1000)) must be equalTo(1000)
    }

    "get last page from joiesX sites" in {
      getLastPageNumber("joiesducode") must be > (1)
      getLastPageNumber("joiesdusysadmin") must be > (1)
      getLastPageNumber("joiesdutest") must be > (1)
      getLastPageNumber("joiesduscrum") must be > (1)
    }

  }

}