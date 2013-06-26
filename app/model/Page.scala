package model

import org.joda.time.DateTime

import play.api.libs.json._

import reactivemongo.bson._
import play.modules.reactivemongo.json.BSONFormats._

case class Page(_id: Option[BSONObjectID],
                     siteId: String,
                     pageNumber: Int,
                     nbViews: Int,
                     images_1: List[Image],
                     images_2: List[Image],
                     createdAt: Option[DateTime],
                     var link: Option[Link] = None) extends MongoModel[Option[BSONObjectID]] {
  def this(topicId: String,
           offset: Int,
           nbViews: Int,
           icons: List[Image],
           images: List[Image]) =
    this(Some(BSONObjectID.generate), topicId, offset, nbViews, icons, images, Some(DateTime.now()))

  def this(topicId: String,
           pageNumber: Int,
           icons: List[Image],
           images: List[Image]) =
    this(topicId, pageNumber, 1, icons, images)

  override def toString =
    s"Page=[$id,siteId=$siteId,pageNumber=$pageNumber,nbViews=$nbViews,iconsSize=${images_1.size},imagesSize=${images_2.size},createdAt=$createdAt]"

}

object Page {
  implicit val format: Format[Page] = Json.format[Page]
  implicit val writes: Writes[Page] = Json.writes[Page]
}