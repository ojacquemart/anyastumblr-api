package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

import org.joda.time.DateTime

import reactivemongo.bson._
import reactivemongo.bson.handlers._

case class TopicPage(id: Option[BSONObjectID],
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
    this(Some(BSONObjectID.generate), title, pageNumber, icons, images, createdAt, updatedAt)

  def this(title: String, pageNumber: Int,
           icons: List[String],
           images: List[String]) =
    this(Some(BSONObjectID.generate), title, pageNumber, icons, images, Some(DateTime.now()), None)

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

object TopicPage {
  implicit object TopicPageBSONReader extends BSONReader[TopicPage] {
    def fromBSON(document: BSONDocument): TopicPage = {
      val doc = document.toTraversable
      TopicPage(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[BSONInteger]("pageNumber").get.value,
        doc.getAs[BSONArray]("icons").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        },
        doc.getAs[BSONArray]("images").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        },
        doc.getAs[BSONDateTime]("createdAt").map(dt => new DateTime(dt.value)),
        doc.getAs[BSONDateTime]("updatedAt").map(dt => new DateTime(dt.value)))
    }
  }
  implicit object TopicPageBSONWriter extends BSONWriter[TopicPage] {
    def toBSON(article: TopicPage) = {
      BSONDocument(
        "_id" -> article.id.getOrElse(BSONObjectID.generate),
        "title" -> BSONString(article.title),
        "pageNumber" -> BSONInteger(article.pageNumber),
        "icons" -> BSONArray(article.icons.map { s => BSONString(s) }: _*),
        "images" -> BSONArray(article.icons.map { s => BSONString(s) }: _*),
        "createdAt" ->  article.createdAt.map(date => BSONDateTime(date.getMillis)),
        "updatedAt" ->  article.updatedAt.map(date => BSONDateTime(date.getMillis)))
    }
  }
}