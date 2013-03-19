package hfr

import play.api.Logger
import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin

import org.joda.time.DateTime

import reactivemongo.bson._
import reactivemongo.bson.handlers.BSONWriter
import reactivemongo.bson.handlers.DefaultBSONHandlers.DefaultBSONDocumentWriter
import reactivemongo.bson.handlers.DefaultBSONHandlers.DefaultBSONReaderHandler

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

object PageCollection {

  def db = ReactiveMongoPlugin.db
  def collection = db("pages")

  def saveOrUpdate(page: Page) {
    val futurePage = findHead(page)

    futurePage onSuccess {
      case result => result match {
        case None => println("No match found"); save(page)
        case Some(p: Page) => println("Found one match"); update(p)
      }
    }
  }

  def save(page: Page): scala.concurrent.Future[reactivemongo.core.commands.LastError] = {
    Logger.debug("save page " + page)
    implicit val writer = PageBSON.Writer
    collection.insert(page)
  }

  def update(page: Page): scala.concurrent.Future[reactivemongo.core.commands.LastError] = {
    Logger.debug("update page " + page)
    val writer = ImageBSON.Writer

    val selector = BSONDocument("siteId" -> BSONString(page.siteId), "pageNumber" -> BSONInteger(page.pageNumber))
    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "images_1" -> BSONArray(page.images_1.map {
          t => writer.toBSON(t)
        }: _*),
        "images_2" -> BSONArray(page.images_2.map {
          t => writer.toBSON(t)
        }: _*)
      ),
      "$inc" -> BSONDocument("nbViews" -> BSONInteger(1))
    )

    collection.update(selector, modifier)
  }


  def findHead(page: Page): Future[Option[Page]] = {
    findHeadByTopicIdAndPageOffset(page.siteId, page.pageNumber)
  }

  def findHeadByTopicIdAndPageOffset(topicId: String, offset: Int): Future[Option[Page]] = {
    Logger.debug(s"find head for $topicId and $offset")
    implicit val reader = PageBSON.Reader
    val query = BSONDocument("siteId" -> BSONString(topicId), "pageNumber" -> BSONInteger(offset))

    val cursor = collection.find(query)
    cursor.headOption
  }

}
