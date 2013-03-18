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

  def listText(cssQuery: String): List[String] = {
    Logger.debug(s"""CSS query text '$cssQuery'""")
    list(cssQuery).map(_.text())
  }

  def listAttribute(cssQuery: String, attributeName: String): List[String] = {
    Logger.debug(s"""CSS query '$cssQuery' for attribute '$attributeName'""")
    list(cssQuery).map(_.attr(attributeName))
  }

  def list(cssQuery: String): List[Element] = {
    var buffer: ListBuffer[Element] = ListBuffer()

    val elements: Elements = document.select(cssQuery)
    val it = elements.iterator()
    while (it.hasNext) {
      buffer += it.next()
    }

    buffer.toList
  }

}

