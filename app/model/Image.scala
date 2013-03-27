package model

import play.api.libs.json._

import reactivemongo.bson._
import reactivemongo.bson.handlers._
import reactivemongo.bson.BSONInteger
import reactivemongo.bson.BSONString

case class Image(id: Option[BSONObjectID], src: String, text: String, likes: Int = 0, hates: Int = 0) {

  override def equals(other: Any) = other match {
    case otherImage: Image => src == otherImage.src
    case _ => false
  }

  override def hashCode(): Int = src.hashCode
}

object Image {
  def apply(url: String, text: String) = new Image(Some(BSONObjectID.generate), url, text)
  def apply(url: String) = new Image(Some(BSONObjectID.generate), url, "")
}

object ImageJSON {
  implicit object Writer extends Writes[Image] {
    def writes(content: Image): JsValue =
      Json.obj(
        "src" -> JsString(content.src),
        "text" -> JsString(content.text),
        "likes" -> JsNumber(content.likes),
        "hates" -> JsNumber(content.hates)
      )
  }
}

object ImageBSON {
  implicit object Reader extends BSONReader[Image] {
    def fromBSON(document: BSONDocument): Image = {
      val doc = document.toTraversable
      new Image(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("src").get.value,
        doc.getAs[BSONString]("text").get.value,
        doc.getAs[BSONInteger]("likes").get.value,
        doc.getAs[BSONInteger]("hates").get.value
      )
    }
  }
  implicit object Writer extends BSONWriter[Image] {
    def toBSON(image: Image) = {
      BSONDocument(
        "_id" -> image.id.getOrElse(BSONObjectID.generate),
        "src" -> BSONString(image.src),
        "text" -> BSONString(image.text),
        "likes" -> BSONInteger(image.likes),
        "hates" -> BSONInteger(image.hates)
      )
    }
  }

}