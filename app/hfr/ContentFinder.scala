package hfr

import collection.mutable.ListBuffer

import play.api.libs.json._

import org.jsoup.Jsoup
import org.jsoup.nodes._
import play.api.Logger

case class HfrTopic(topic: String, pageNumber: Int) {

  val Root = "http://forum.hardware.fr/hfr"
  val Extension = ".htm"

  override def toString = "%s/%s_%d%s".format(Root, topic, pageNumber, Extension)

}

case class Content(rootUrl: String, currentPage: Int, previousPage: Int, nextPage: Int, images: JsValue)

object ContentFormats {

  implicit object ContentFormat extends Format[Content] {
    def reads(json: JsValue) = {
      new Content((json \ "rootUrl").as[String],
        (json \ "currentPage").as[Int], (json \"previousPage").as[Int], (json \ "nextPage").as[Int],
        json \ "images")
    }
    def writes(content: Content): JsValue = {
      JsObject(
        List("rootUrl" -> JsString(content.rootUrl),
            "currentPage" -> JsNumber(content.currentPage),
            "previousPage" -> JsNumber(content.previousPage),
            "nextPage" -> JsNumber(content.nextPage),
            "images" -> content.images ))
    }
  }
}

case class ContentFinder(url: String) {

  import hfr.ContentFormats._

  val HfrRoot = "http://forum.hardware.fr"
  val HfrSmilies = "http://forum-images.hardware.fr/images/perso"

  // Regex to extract page number
  val RegexPageNumber = """([0-9]+)\.htm""".r

  // Css constants to select elements
  val CssImgsSelector   = "tr.cBackCouleurTab2 p img"
  val CssLinksSelector  = "tr.cBackHeader.fondForum2PagesHaut div.left a"

  // Html document
  def getDocument() = Jsoup.connect(url)
    .data("query", "Java")
    .userAgent("Mozilla")
    .cookie("auth", "token")
    .timeout(3000)
    .get()

  def getContent(): Content = {
    val pageIndexes = getPageIndexes()
    val images: JsValue = imagesAsJson()
    Logger.info("pageIndexes=[current=%d,prev=%d,next=%d], images = %s".format(pageIndexes._1, pageIndexes._2, pageIndexes._3, images))

    new Content(HfrRoot, pageIndexes._1, pageIndexes._2, pageIndexes._3, images)
  }
  def getContentAsJson(): JsValue = Json.toJson(getContent())

  def getPageIndexes(): (Int, Int, Int) = {
    val currentPage = getCurrentPage()
    val nbPages = getNbPages()
    val previousPage = if( currentPage <= 1) -1 else currentPage -1
    val nextPage = if (currentPage >= nbPages) -1 else currentPage + 1

    (currentPage, previousPage, nextPage)
  }
  def getCurrentPage(): Int = extractNumberPage(url)
  def getNbPages() = {
    val links: List[String] = listElements(CssLinksSelector, "href")
    // TODO: try to use css select :last-child added in jsoup 1.7.2
    extractNumberPage(links.reverse.head)
  }

  def extractNumberPage(value: String) = {
    val result = RegexPageNumber.findFirstMatchIn(value).get
    result.group(1).toInt
  }

  def imagesAsJson() = {
    val images: List[String] = listElements(CssImgsSelector, "src")
    val (smileyImages, otherImages) = images.partition(i => i.startsWith(HfrSmilies))

    Json.toJson(smileyImages ++ otherImages)
  }

  private def listElements(cssSelector: String, attributeName: String): List[String] = {
    var buffer: ListBuffer[String] = ListBuffer()

    val imgs = getDocument().select(cssSelector)
    val itImgs = imgs.iterator()
    while (itImgs.hasNext) {
      val img: Element = itImgs.next()
      buffer += img.attr(attributeName)
    }

    buffer.toList
  }

}