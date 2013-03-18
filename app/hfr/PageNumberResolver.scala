package hfr

import jsoup.DocumentWrapper

case class PageNumberResolver(topic: Topic, pageNumber: Option[Int]) {

  val LinkHrefAttribute = "href"

  def resolve(): (String, Int) = {
    val pageIndex = pageNumber match {
      case None => getPageNumber(topic.url)
      case Some(pageNumber: Int) => pageNumber
    }

    val changeUrlPageInfos: ChangeUrlPageInfos = topic.configuration.pageResolverInfos.changeUrlPageInfos
    val pageUrl = changeUrlPageInfos.regex.r.replaceFirstIn(topic.url, changeUrlPageInfos.replacement.format(pageIndex))
    (pageUrl, pageIndex)
  }

  def getPageNumber(url: String): Int = {
    val optionPageNumberInfos: Option[ForumPageNumberInfos] = topic.configuration.pageResolverInfos.forumPageNumberInfos
    optionPageNumberInfos match {
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
