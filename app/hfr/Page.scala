package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

import org.joda.time.DateTime

import reactivemongo.bson._
import reactivemongo.bson.handlers._
import play.api.i18n.Messages


case class Image()

case class Page(id: Option[BSONObjectID],
                     topicId: String,
                     offset: Int,
                     nbViews: Int,
                     icons: List[String],
                     images: List[String],
                     createdAt: Option[DateTime]) {
  def this(topicId: String,
           offset: Int,
           nbViews: Int,
           icons: List[String],
           images: List[String]) =
    this(Some(BSONObjectID.generate), topicId, offset, nbViews, icons, images, Some(DateTime.now()))

  def this(topicId: String,
           offset: Int,
           icons: List[String],
           images: List[String]) =
    this(topicId, offset, 1, icons, images)

  lazy val title = "%s %d".format(Messages("page.label"), offset)

  override def toString = {
    val iconsSize = icons.size
    val imagesSize = images.size
    s"Page=[$id,topicId=$topicId,offset=$offset,nbViews=$nbViews,iconsSize=$iconsSize,imagesSize=$imagesSize,createdAt=$createdAt]"
  }

}

object PageJSON {

  implicit object PageJsonHandlers extends Format[Page] {

    def reads(json: JsValue): JsResult[Page] = JsSuccess(
      new Page(
        (json \"topicId").as[String],
        (json \ "offset").as[Int],
        (json \ "nbViews").as[Int],
        (json \ "icons").as[List[String]],
        (json \ "images").as[List[String]])
    )

    def writes(content: Page): JsValue = JsObject(
      List("title" -> JsString(content.title),
        "offset" -> JsNumber(content.offset),
        "nbViews" -> JsNumber(content.nbViews),
        "icons" -> Json.toJson(content.icons),
        "images" -> Json.toJson(content.images)))
  }

}

object PageBSON {
  implicit object Reader extends BSONReader[Page] {
    def fromBSON(document: BSONDocument): Page = {
      val doc = document.toTraversable
      val page = new Page(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("topicId").get.value,
        doc.getAs[BSONInteger]("offset").get.value,
        doc.getAs[BSONInteger]("nbViews").get.value,
        doc.getAs[BSONArray]("icons").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        },
        doc.getAs[BSONArray]("images").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        },
        doc.getAs[BSONDateTime]("createdAt").map(dt => new DateTime(dt.value)))
      page
    }
  }
  implicit object Writer extends BSONWriter[Page] {
    def toBSON(page: Page) = {
      BSONDocument(
        "_id" -> page.id.getOrElse(BSONObjectID.generate),
        "topicId" -> BSONString(page.topicId),
        "offset" -> BSONInteger(page.offset),
        "nbViews" -> BSONInteger(page.nbViews),
        "icons" -> BSONArray(page.icons.map { s => BSONString(s) }: _*),
        "images" -> BSONArray(page.images.map { s => BSONString(s) }: _*),
        "createdAt" ->  page.createdAt.map(date => BSONDateTime(date.getMillis))
      )
    }
  }

}