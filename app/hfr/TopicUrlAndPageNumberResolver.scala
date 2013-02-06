package hfr

import jsoup.DocumentWrapper

case class TopicUrlAndPageNumberResolver(url: String, pageNumber: Option[Int]) {

  val CssLinksSelector = "tr.cBackHeader.fondForum2PagesHaut div.left a"
  val LinkHrefAttribute = "href"

  val RegexPageNumber = """([0-9]+)\.htm""".r
  val RegexReplaceLastPage = """_[0-9]+\.""".r

  def resolve(): (String, Int) = {
    val pageIndex = pageNumber match {
      case None => getNbPagesFromFirstTopicPage(url)
      case Some(pageNumber: Int) => pageNumber
    }

    val pageUrl = RegexReplaceLastPage.replaceFirstIn(url, "_%d.".format(pageIndex))
    (pageUrl, pageIndex)
  }

  def getNbPagesFromFirstTopicPage(url: String): Int = {
    val links: List[String] = new DocumentWrapper(url).listElements(CssLinksSelector, LinkHrefAttribute)
    // TODO: try to use css select :last-child added in jsoup 1.7.2
    extractNumberPage(links.reverse.head)
  }

  private def extractNumberPage(value: String) = {
    val result = RegexPageNumber.findFirstMatchIn(value).get
    result.group(1).toInt
  }

}
