package hfr

import jsoup.DocumentWrapper

case class TopicUrlAndPageNumberResolver(topic: Topic, pageNumber: Option[Int]) {

  val CssLinksSelector = "tr.cBackHeader.fondForum2PagesHaut div.left a:last-child"
  val LinkHrefAttribute = "href"

  val RegexPageNumber = """([0-9]+)\.htm""".r
  val RegexReplaceLastPage = """_[0-9]+\.""".r

  def resolve(): (String, Int) = {
    val pageIndex = pageNumber match {
      case None => getNbPagesFromFirstTopicPage(topic.url)
      case Some(pageNumber: Int) => pageNumber
    }

    val pageUrl = RegexReplaceLastPage.replaceFirstIn(topic.url, "_%d.".format(pageIndex))
    (pageUrl, pageIndex)
  }

  def getNbPagesFromFirstTopicPage(url: String): Int = {
    val links: List[String] = new DocumentWrapper(url).listElements(CssLinksSelector, LinkHrefAttribute)
    if (links.isEmpty) 1
    else extractNumberPage(links(0))
  }

  private def extractNumberPage(value: String) = {
    val result = RegexPageNumber.findFirstMatchIn(value).get
    result.group(1).toInt
  }

}
