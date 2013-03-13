package jsoup

import collection.mutable.ListBuffer

import org.jsoup.Jsoup
import org.jsoup.nodes._
import org.jsoup.select.Elements

import play.api.Logger

case class DocumentWrapper(url: String) {

  val document: Document = {
    Logger.debug(s"getDocument($url)")

    Jsoup.connect(url)
      .userAgent("Mozilla")
      .cookie("auth", "token")
      .timeout(3000)
      .get()
  }

  def listElements(cssSelector: String, attributeName: String): List[String] = {
    Logger.debug(s"""CSS Selector '$cssSelector' for attribute '$attributeName'""")
    var buffer: ListBuffer[String] = ListBuffer()

    val elements: Elements = document.select(cssSelector)
    val it = elements.iterator()
    while (it.hasNext) {
      val img: Element = it.next()
      buffer += img.attr(attributeName)
    }

    buffer.toList
  }
}
