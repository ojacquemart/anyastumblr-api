package model

import play.api.libs.json._

import reactivemongo.bson._

import play.modules.reactivemongo.json.BSONFormats._

case class Image(id: Option[BSONObjectID], src: String, text: String, likes: Int = 0, hates: Int = 0) {

  override def equals(other: Any) = other match {
    case otherImage: Image => src == otherImage.src
    case _ => false
  }

  override def hashCode(): Int = src.hashCode
}

object Image {
  implicit val format: Format[Image] = Json.format[Image]
  implicit val writes: Writes[Image] = Json.writes[Image]

  def get(url: String, text: String) = new Image(Some(BSONObjectID.generate), url, text)
  def get(url: String) = new Image(Some(BSONObjectID.generate), url, "")
}
