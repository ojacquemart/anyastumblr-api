package hfr

import play.api.libs.json._

import reactivemongo.bson._
import reactivemongo.bson.handlers._
import reactivemongo.bson.BSONInteger
import reactivemongo.bson.BSONString

case class Image(url: String, text: String = "", likes: Int = 0, hates: Int = 0)

object Image {
  def apply(url: String) = new Image(url)
}

object ImageJSON {
  implicit object Writes extends Writes[Image] {
    def writes(content: Image): JsValue = JsObject(
      List(
        "url" -> JsString(content.url),
        "text" -> JsString(content.text),
        "likes" -> JsNumber(content.likes),
        "hates" -> JsNumber(content.hates)
      )
    )
  }
}

object ImageBSON {
  implicit object Reader extends BSONReader[Image] {
    def fromBSON(document: BSONDocument): Image = {
      val doc = document.toTraversable
      new Image(
        doc.getAs[BSONString]("url").get.value,
        doc.getAs[BSONString]("text").get.value,
        doc.getAs[BSONInteger]("likes").get.value,
        doc.getAs[BSONInteger]("hates").get.value
      )
    }
  }
  implicit object Writer extends BSONWriter[Image] {
    def toBSON(page: Image) = {
      BSONDocument(
        "url" -> BSONString(page.url),
        "text" -> BSONString(page.text),
        "likes" -> BSONInteger(page.likes),
        "hates" -> BSONInteger(page.hates)
      )
    }
  }

}