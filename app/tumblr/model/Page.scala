package tumblr.model

import org.joda.time.DateTime

import play.api.libs.json._

import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson._

case class Page(_id: Option[BSONObjectID],
                siteId: String,
                pageNumber: Int,
                nbViews: Int,
                images_1: List[Image],
                images_2: List[Image],
                link: Option[Link],
                createdAt: Option[DateTime]) extends GenericMongoModel {
  def this(topicId: String,
           pageNumber: Int,
           nbViews: Int,
           icons: List[Image],
           images: List[Image],
           link: Link) =
    this(Some(BSONObjectID.generate), topicId, pageNumber, nbViews, icons, images, Some(link), Some(DateTime.now()))

  def this(topicId: String,
           pageNumber: Int,
           icons: List[Image],
           images: List[Image],
           link: Link) =
    this(topicId, pageNumber, 1, icons, images, link)

  override def toString =
    s"Page=[$id,slug=$siteId,pageNumber=$pageNumber,nbViews=$nbViews,iconsSize=${images_1.size},imagesSize=${images_2.size},createdAt=$createdAt]"

}

case class PageWithTotal(page: Page, totalPage: Option[Link])

object Page {
  implicit val format: Format[Page] = Json.format[Page]
  implicit val writes: Writes[Page] = Json.writes[Page]

  def getImagesAsListOfJsValue(images: List[Image]): List[JsValue] = {
    val imageWriter = Writes[Image] { image =>
      Json.obj("src" -> image.src, "text" -> JsString(image.text))
    }

    images.map(image => imageWriter.writes(image))
  }

  // Simple writer to hide some informations (bsonObjectId, nbViews...)
  val simpleWriter = Writes[Page] { page =>


    Json.obj(
      "link" -> page.link,
      "pageNumber" -> page.pageNumber,
      "images_1" -> getImagesAsListOfJsValue(page.images_1),
      "images_2" -> getImagesAsListOfJsValue(page.images_2)
    )
  }

  val simplePageWithTotalWriter = Writes[PageWithTotal] { pageWithTotal =>
    val page = pageWithTotal.page
    Json.obj(
      "link" -> page.link,
      "linkLastPage" -> pageWithTotal.totalPage,
      "pageNumber" -> page.pageNumber,
      "images_1" -> getImagesAsListOfJsValue(page.images_1),
      "images_2" -> getImagesAsListOfJsValue(page.images_2)
    )
  }

}
