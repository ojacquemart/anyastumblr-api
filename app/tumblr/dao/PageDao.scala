package tumblr.dao

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

import play.api.Logger
import play.api.libs.json._

import play.modules.reactivemongo.json.BSONFormats._

import reactivemongo.bson._

import tumblr.model._
import tumblr.model.Page.{format, writes}

object PageDao extends MongoDao[Page, BSONObjectID] {

  val collectionName = "pages"

  implicit val formatImage: Format[Image] = Json.format[Image]
  implicit val writesImage: Writes[Image] = Json.writes[Image]

  def saveOrUpdate(page: Page) {
    val futurePage = findHead(page)

    futurePage onSuccess { case result =>
      result match {
        case None => save(page)
        case Some(p: Page) => update(p)
      }
    }
  }

  def update(page: Page): Future[reactivemongo.core.commands.LastError] = {
    Logger.debug("update page " + page)

    val selector = Json.obj("siteId" -> page.siteId, "pageNumber" -> page.pageNumber)
    val modifier = Json.obj(
      "$set" -> Json.obj(
        "images_1" -> page.images_1,
        "images_2" -> page.images_2
      ),
      "$inc" -> Json.obj("nbViews" -> 1)
    )

    collection.update(selector, modifier)
  }

  def findHead(page: Page): Future[Option[Page]] = findHeadByTopicIdAndPageOffset(page.siteId, page.pageNumber)

  def findHeadByTopicIdAndPageOffset(siteId: String, pageNumber: Int): Future[Option[Page]] = {
    Logger.debug(s"find head for $siteId and $pageNumber")

    collection
      .find(Json.obj("siteId" -> siteId, "pageNumber" -> pageNumber))
      .one[Page]
  }

}
