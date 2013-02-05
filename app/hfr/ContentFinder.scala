package hfr

import play.api.libs.json._

import play.api.Logger

object JsonContentFinder {

  def getContentFromFirstPageAsJson(topicId: String): JsValue = {
    getContentAsJson(topicId, None)
  }

  def getContentFromPageNumberAsJson(topicId: String, pageNumber: Int): JsValue = {
    getContentAsJson(topicId, Some(pageNumber))
  }

  def getContentAsJson(topicId: String, pageNumber: Option[Int]): JsValue = {
    import hfr.ContentFormats._

    val url = TopicRepository.findTopicUrl(topicId)
    Json.toJson(getContent(url, pageNumber))
  }

  def getContent(url: String, pageNumber: Option[Int]): Content = {
    val urlAndPageNumber: (String, Int) = new TopicUrlAndPageNumberResolver(url, pageNumber).resolve()
    val currentUrl = urlAndPageNumber._1
    val currentPageNumber = urlAndPageNumber._2
    val images = new TopicPageImagesFinder(currentUrl).find()
    Logger.info("Load from url %s".format(currentUrl))
    Logger.debug("Content Topic => [pageNumber=%d,images=%s]".
      format(currentPageNumber, images))

    new Content(currentPageNumber, images)
  }

}

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

case class TopicPageImagesFinder(url: String) {

  val HfrImagesCssSelector = "tr.message td.messCase2 img"
  val ImgSrcAttribute = "src"

  val HfrFilters = List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")
  val HfrThemes = "http://forum-images.hardware.fr/themes"

  def find() = {
    val images: List[String] = new DocumentWrapper(url).listElements(HfrImagesCssSelector, ImgSrcAttribute)
    rearrangeImages(images)
  }

  def rearrangeImages(images: List[String]): List[String] = {
    val (smileyImages, otherImages) = images
      .filterNot(_.startsWith(HfrThemes))
      .partition(i => HfrFilters.exists(i.startsWith))

    val concat = smileyImages ++ otherImages
    concat.distinct
  }

}