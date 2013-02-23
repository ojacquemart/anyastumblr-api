package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

import org.joda.time.DateTime

case class TopicPage(id: Option[Long],
                     title: String, pageNumber: Int,
                     icons: List[String],
                     images: List[String],
                     createdAt: Option[DateTime],
                     updatedAt: Option[DateTime]) {

  def this(title: String, pageNumber: Int,
           icons: List[String],
           images: List[String],
           createdAt: Option[DateTime],
          updatedAt: Option[DateTime]) =
    this(None, title, pageNumber, icons, images, createdAt, updatedAt)

  def this(title: String, pageNumber: Int,
           icons: List[String],
           images: List[String]) =
    this(None, title, pageNumber, icons, images, Some(DateTime.now()), None)

}

object ContentFormats {

  implicit object ContentFormat extends Format[TopicPage] {

    def reads(json: JsValue): JsResult[TopicPage] = JsSuccess(
      new TopicPage(
        (json \ "title").as[String], (json \ "pageNumber").as[Int],
        (json \ "icons").as[List[String]], (json \ "images").as[List[String]],
        (json \ "createdAt").as[Option[DateTime]], (json \ "updatedAt").as[Option[DateTime]])
    )

    def writes(content: TopicPage): JsValue = JsObject(List("title" -> JsString(content.title),
      "pageNumber" -> JsNumber(content.pageNumber),
      "icons" -> Json.toJson(content.icons),
      "images" -> Json.toJson(content.images)))
  }

}
