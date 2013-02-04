package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

case class Content(rootUrl: String, currentPage: Int, previousPage: Int, nextPage: Int, images: JsValue)

object ContentFormats {

  implicit object ContentFormat extends Format[Content] {
    def reads(json: JsValue) = {
      new Content(
        (json \ "rootUrl").as[String],
        (json \ "currentPage").as[Int], (json \ "previousPage").as[Int], (json \ "nextPage").as[Int],
        json \ "images")
    }

    def writes(content: Content): JsValue = {
      JsObject(
        List("rootUrl" -> JsString(content.rootUrl),
          "currentPage" -> JsNumber(content.currentPage),
          "previousPage" -> JsNumber(content.previousPage),
          "nextPage" -> JsNumber(content.nextPage),
          "images" -> content.images))
    }
  }

}