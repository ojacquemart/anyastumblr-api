import org.specs2.mutable._


import hfr._
import scala.Some

class PageNumberResolverSpec extends Specification {

  "The PageNumberResolver class" should {

    "resolve urlAndPageNumber from firstPage without pageNumber" in {
      val site = SiteRepository.getFirstSite()
      val resolver = new PageNumberResolver(site, None)
      val urlAndPageNumber = resolver.resolve
      val url = urlAndPageNumber._1
      val pageNumber = urlAndPageNumber._2

      url must not be equalTo(site.url)
      pageNumber must be > (1)
    }

    "resolve urlAndPageNumber from firstPage with pageNumber" in {
      val site = SiteRepository.getFirstSite()
      val resolver = new PageNumberResolver(site, Some(1000))
      val urlAndPageNumber = resolver.resolve
      val url = urlAndPageNumber._1
      val pageNumber = urlAndPageNumber._2

      url must not be equalTo(site.url)
      pageNumber must be equalTo (1000)
    }

  }

}