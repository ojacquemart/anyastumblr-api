import org.specs2.mutable._

import scala.Some

import dao._
import tumblr._

class PageNumberResolverSpec extends Specification {

  "The PageNumberResolver class" should {

    "resolve hfr urlAndPageNumber from firstPage without pageNumber" in new FakeApp {
      val site = SiteDao.getFirstSite()
      val resolver = new PageNumberResolver(site, None)
      val urlAndPageNumber = resolver.resolve

      val url = urlAndPageNumber._1
      url must not be equalTo(site.url)

      val pageNumber = urlAndPageNumber._2
      pageNumber must be > (1)
    }

    "resolve hfr urlAndPageNumber from firstPage with pageNumber" in new FakeApp {
      val site = SiteDao.getFirstSite()
      val resolver = new PageNumberResolver(site, Some(1000))
      val urlAndPageNumber = resolver.resolve

      val url = urlAndPageNumber._1
      url must not be equalTo(site.url)

      val pageNumber = urlAndPageNumber._2
      pageNumber must be equalTo (1000)
    }

    "getLastPageNumber for joiesudcode" in new FakeApp {
      getLastPageNumber("joiesducode") must be > (1)
    }

    "getLastPageNumber for joiesdusysadmin" in new FakeApp {
      getLastPageNumber("joiesdusysadmin") must be > (1)
    }

    "getLastPageNumber for joiesdutest" in new FakeApp {
      getLastPageNumber("joiesdutest") must be > (1)
    }

    "getLastPageNumber for joiesduscrum" in new FakeApp {
      getLastPageNumber("joiesduscrum") must be > (1)
    }

    def getLastPageNumber(name: String): Int = {
      val optionSite = SiteDao.getSites().find(site => site.name.toLowerCase() == name)
      optionSite must not be equalTo(None)

      val site = optionSite.get
      val resolver = new PageNumberResolver(site, None)
      val lastPageNumber = resolver.getLastPageNumber()

      lastPageNumber
    }

  }

}