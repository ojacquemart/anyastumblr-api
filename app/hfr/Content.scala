package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

case class Content(pageNumber: Int, images: List[String])

object ContentFormats {

  implicit object ContentFormat extends Format[Content] {
    def reads(json: JsValue) = {
      new Content((json \ "pageNumber").as[Int], (json \ "images").as[List[String]])
    }

    def writes(content: Content): JsValue = {
      JsObject(
        List("pageNumber" -> JsNumber(content.pageNumber),
          "images" -> Json.toJson(content.images)))
    }
  }

}