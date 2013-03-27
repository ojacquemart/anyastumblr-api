package model

import play.api.libs.json._
import play.api.i18n.Messages

case class Link(url: String, title: String, label: String, pageNumber: Int)

object Link {

  def apply(url: String, title: String, pageNumber: Int) =
    new Link(url, title, "%s %d".format(Messages(s"page.label"), pageNumber), pageNumber)

}

object LinkJSON {
  implicit object Writer extends Writes[Link] {
    def writes(content: Link) =
      Json.obj(
        "url" -> content.url,
        "title" -> content.title,
        "label" -> content.label,
        "pageNumber" -> content.pageNumber
      )
  }
}
