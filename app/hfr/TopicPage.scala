package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

case class TopicPage(title: String, pageNumber: Int, icons: List[String], images: List[String])

object ContentFormats {

  implicit object ContentFormat extends Format[TopicPage] {
    def reads(json: JsValue) = {
      new TopicPage((json \ "title").as[String], (json \ "pageNumber").as[Int],
        (json \ "icons").as[List[String]] ,(json \ "images").as[List[String]])
    }

    def writes(content: TopicPage): JsValue = {
      JsArray(List(
          JsObject(List("title" -> JsString(content.title),
            "pageNumber" -> JsNumber(content.pageNumber),
            "icons" -> Json.toJson(content.icons),
            "images" -> Json.toJson(content.images))))
      )
    }
  }

}