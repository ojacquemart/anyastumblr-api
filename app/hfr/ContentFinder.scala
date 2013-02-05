package hfr

import play.api.libs.json._

import play.api.Logger

case class ContentFinder(topic: Topic, pageNumber: Option[Int]) {

  import hfr.ContentFormats._

  val HfrRoot     = "http://forum.hardware.fr"
  val HfrFilters  = List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")
  val HfrThemes   = "http://forum-images.hardware.fr/themes"

  // Css constants to select elements
  val CssImgsSelector   = "tr.message td.messCase2 img"
  val CssLinksSelector  = "tr.cBackHeader.fondForum2PagesHaut div.left a"

  // Regex to extract page number
  val RegexPageNumber       = """([0-9]+)\.htm""".r
  val RegexReplaceLastPage  = """_[0-9]+\.""".r

  def getUrl() = {
    RegexReplaceLastPage.replaceFirstIn(topic.url, "_%d.".format(getPageIndex))
  }

  def getPageIndex() = {
    pageNumber match {
      case None => getNbPages(topic.url)
      case Some(pageNumber: Int) => pageNumber
    }
  }

  def getContent(): Content = {
    val url = getUrl()
    val pageIndexes = getPageIndexes(url)
    val images: JsValue = getImagesAsJson(url)
    Logger.debug("pageIndexes=[current=%d,prev=%d,next=%d],images=[%s]".
      format(pageIndexes._1, pageIndexes._2, pageIndexes._3,
      images))

    new Content(HfrRoot, pageIndexes._1, pageIndexes._2, pageIndexes._3, images)
  }
  def getContentAsJson(): JsValue = {
    Logger.info("Load gifs")
    Json.toJson(getContent())
  }

  def getPageIndexes(url: String): (Int, Int, Int) = {
    val currentPage = getCurrentPage(url)
    val nbPages = getNbPages(url)
    Logger.debug("Current page=%d,nbPages=%d".format(currentPage, nbPages))

    val previousPage = if (currentPage <= 1) -1 else (currentPage - 1)
    val nextPage = if (currentPage >= nbPages) -1 else (currentPage + 1)

    (currentPage, previousPage, nextPage)
  }

  def getCurrentPage(url: String) = pageNumber match {
    case None => getNbPages(url)
    case Some(i: Int) => i
  }

  def getNbPages(url: String): Int = {
    val links: List[String] = new DocumentWrapper(url).listElements(CssLinksSelector, "href")
    // TODO: try to use css select :last-child added in jsoup 1.7.2
    extractNumberPage(links.reverse.head)
  }

  def extractNumberPage(value: String) = {
    val result = RegexPageNumber.findFirstMatchIn(value).get
    result.group(1).toInt
  }

  def getImages(url: String) = {
    val images: List[String] = new DocumentWrapper(url).listElements(CssImgsSelector, "src")
    rearrangeImages(images)
  }

  def rearrangeImages(images: List[String]): List[String] = {
    val (smileyImages, otherImages) = images
      .filterNot(_.startsWith(HfrThemes))
      .partition(i => HfrFilters.exists(i.startsWith))

    val concat = smileyImages ++ otherImages
    concat.distinct
  }

  def getImagesAsJson(url: String) = {
    Json.toJson(getImages(url))
  }

}

object ContentFinder {

  def apply(topicId: String) = {
    new ContentFinder(TopicRepository.findTopic(topicId), None)
  }

  def apply(topicId: String, pageNumber: Int) = {
    new ContentFinder(TopicRepository.findTopic(topicId), Some(pageNumber))
  }

}