package hfr

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsString

import org.joda.time.DateTime

import reactivemongo.bson._
import reactivemongo.bson.handlers._
import play.api.i18n.Messages
import mongo.bson.{BSONWriterHelper, BSONReaderHelper}

case class Page(id: Option[BSONObjectID],
                     topicId: String,
                     offset: Int,
                     nbViews: Int,
                     images_1: List[Image],
                     images_2: List[Image],
                     createdAt: Option[DateTime]) {
  def this(topicId: String,
           offset: Int,
           nbViews: Int,
           icons: List[Image],
           images: List[Image]) =
    this(Some(BSONObjectID.generate), topicId, offset, nbViews, icons, images, Some(DateTime.now()))

  def this(topicId: String,
           offset: Int,
           icons: List[Image],
           images: List[Image]) =
    this(topicId, offset, 1, icons, images)

  lazy val title = "%s %d".format(Messages("page.label"), offset)

  override def toString = {
    val images_1Size = images_1.size
    val images_2Size = images_2.size
    s"Page=[$id,topicId=$topicId,offset=$offset,nbViews=$nbViews,iconsSize=$images_1Size,imagesSize=$images_2Size,createdAt=$createdAt]"
  }

}

object PageJSON {

  implicit object PageJsonHandlers extends Writes[Page] {

    import ImageJSON.Writes

    def writes(content: Page): JsValue = JsObject(
      List("title" -> JsString(content.title),
        "offset" -> JsNumber(content.offset),
        "nbViews" -> JsNumber(content.nbViews),
        "images_1" -> Json.toJson(content.images_1),
        "images_2" -> Json.toJson(content.images_2)
      )
    )
  }

}

object PageBSON {
  implicit object Reader extends BSONReader[Page] with BSONReaderHelper {
    def fromBSON(document: BSONDocument): Page = {
      implicit val imageWriter = ImageBSON.Reader
      implicit val doc = document.toTraversable
      val page = new Page(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONString]("topicId").get.value,
        doc.getAs[BSONInteger]("offset").get.value,
        doc.getAs[BSONInteger]("nbViews").get.value,
        listDocument[Image]("images_1"),
        listDocument[Image]("images_2"),
        doc.getAs[BSONDateTime]("createdAt").map(dt => new DateTime(dt.value)))
      page
    }
  }
  implicit object Writer extends BSONWriter[Page] with BSONWriterHelper {
    def toBSON(page: Page) = {
      implicit val imageWriter = ImageBSON.Writer
      BSONDocument(
        "_id" -> page.id.getOrElse(BSONObjectID.generate),
        "topicId" -> BSONString(page.topicId),
        "offset" -> BSONInteger(page.offset),
        "nbViews" -> BSONInteger(page.nbViews),
        "images_1" -> listDocument(page.images_1),
        "images_2" -> listDocument(page.images_2),
        "createdAt" ->  page.createdAt.map(date => BSONDateTime(date.getMillis))
      )
    }
  }

}