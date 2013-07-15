package tumblr.model

import play.api.libs.json._
import play.api.i18n.Messages

case class Link(url: String, title: String, label: String, pageNumber: Int)

object Link {

  implicit val formats: Format[Link] = Json.format[Link]
  implicit val writes: Writes[Link] = Json.writes[Link]

  def get(url: String, title: String, pageNumber: Int) =
    new Link(url, title, "%s %d".format(Messages(s"page.label"), pageNumber), pageNumber)

}