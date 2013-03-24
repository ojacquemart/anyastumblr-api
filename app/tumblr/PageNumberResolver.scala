package tumblr

import model._

import jsoup.DocumentWrapper

case class PageNumberResolver(site: Site, pageNumber: Option[Int]) {

  val LinkHrefAttribute = "href"

  def resolve(): (String, Int) = {
    val pageIndex = pageNumber match {
      case None => getPageNumber(site.url)
      case Some(pageNumber: Int) => pageNumber
    }

    val resolver: PageResolver = site.configuration.pageResolver
    val changeUrlPageDescriptor: ChangePageDescriptor = resolver.changePageDescriptor
    val pageUrl = changeUrlPageDescriptor.regex.r.replaceFirstIn(site.url, changeUrlPageDescriptor.replacement.format(pageIndex))
    (pageUrl, pageIndex)
  }

  def getPageNumber(url: String): Int = {
    val optionPageNumberDescriptor: Option[PageNumberDescriptor] = site.configuration.pageResolver.pageNumberDescriptor
    optionPageNumberDescriptor match {
      case None => 1
      case Some(pageNumberInfos) => {
        val links: List[String] = new DocumentWrapper(url).listAttribute(pageNumberInfos.cssSelector, LinkHrefAttribute)
        if (links.isEmpty) 1
        else {
          val url = links(0)
          val result = pageNumberInfos.regex.r.findFirstMatchIn(url).get
          result.group(1).toInt
        }
      }
    }
  }

}
