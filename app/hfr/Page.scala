package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

import org.joda.time.DateTime

import reactivemongo.bson._
import reactivemongo.bson.handlers._

case class Page(id: Option[BSONObjectID],
                     topicId: String,
                     title: String,
                     pageNumber: Int,
                     nbViews: Int,
                     icons: List[String],
                     images: List[String],
                     createdAt: Option[DateTime],
                     updatedAt: Option[DateTime]) {

  def this(topicId: String, title: String,
           pageNumber: Int,
           nbViews: Int,
           icons: List[String],
           images: List[String],
           createdAt: Option[DateTime],
           updatedAt: Option[DateTime]) =
    this(Some(BSONObjectID.generate), topicId, title, pageNumber, nbViews, icons, images, createdAt, updatedAt)

  def this(topicId: String, title:String,
           pageNumber: Int,
           icons: List[String],
           images: List[String]) =
    this(Some(BSONObjectID.generate), topicId, title, pageNumber, 1, icons, images, Some(DateTime.now()), None)

  override def toString = {
    val iconsSize = icons.size
    val imagesSize = images.size
    s"Page=[$id,topicId=$topicId,number=$pageNumber,nbViews=$nbViews,iconsSize=$iconsSize,imagesSize=$imagesSize,createdAt=$createdAt]"
  }

}

object PageJSON {

  implicit object PageJsonHandlers extends Format[Page] {

    def reads(json: JsValue): JsResult[Page] = JsSuccess(
      new Page(
        (json \"topicId").as[String],
        (json \ "title").as[String], (json \ "pageNumber").as[Int],
        (json \ "pageNumber").as[Int],
        (json \ "icons").as[List[String]], (json \ "images").as[List[String]],
        (json \ "createdAt").as[Option[DateTime]], (json \ "updatedAt").as[Option[DateTime]])
    )

    def writes(content: Page): JsValue = JsObject(
      List("title" -> JsString(content.title),
          "pageNumber" -> JsNumber(content.pageNumber),
          "nbViews" -> JsNumber(content.nbViews),
          "icons" -> Json.toJson(content.icons),
          "images" -> Json.toJson(content.images)))
  }

}

object PageBSON {
  implicit object TopicPageBSONReader extends BSONReader[Page] {
    def fromBSON(document: BSONDocument): Page = {
      val doc = document.toTraversable
      val nbViews = doc.getAs[BSONInteger]("nbViews")
      val page =  Page(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("topicId").get.value,
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[BSONInteger]("pageNumber").get.value,
        if (nbViews.isDefined) nbViews.get.value else 1,
        doc.getAs[BSONArray]("icons").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        },
        doc.getAs[BSONArray]("images").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        },
        doc.getAs[BSONDateTime]("createdAt").map(dt => new DateTime(dt.value)),
        doc.getAs[BSONDateTime]("updatedAt").map(dt => new DateTime(dt.value)))
      page
    }
  }
  implicit object TopicPageBSONWriter extends BSONWriter[Page] {
    def toBSON(page: Page) = {
      BSONDocument(
        "_id" -> page.id.getOrElse(BSONObjectID.generate),
        "topicId" -> BSONString(page.topicId),
        "title" -> BSONString(page.title),
        "pageNumber" -> BSONInteger(page.pageNumber),
        "nbViews" -> BSONInteger(page.nbViews),
        "icons" -> BSONArray(page.icons.map { s => BSONString(s) }: _*),
        "images" -> BSONArray(page.images.map { s => BSONString(s) }: _*),
        "createdAt" ->  page.createdAt.map(date => BSONDateTime(date.getMillis)),
        "updatedAt" ->  page.updatedAt.map(date => BSONDateTime(date.getMillis)))
    }
  }

}