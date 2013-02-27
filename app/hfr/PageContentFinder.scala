package hfr

import play.api.i18n.Messages
import play.api._

import scala.concurrent.future
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

case class PageContentFinder(topic: Topic, pageNumber: Option[Int])  {

  def getContent() = {
    pageNumber match {
      case None         => getContentFromHfr()
      case Some(p: Int) => getContentFromMongo()
    }
  }

  def getContentFromHfr(): Future[Option[Page]] = {
    Logger.info("getContentFromHfr")
    val urlAndPageNumber: (String, Int) = new TopicUrlAndPageNumberResolver(topic.url, pageNumber).resolve()
    val currentUrl = urlAndPageNumber._1
    val currentPageNumber = urlAndPageNumber._2

    val allImages = new PageImagesFinder(currentUrl).find()
    Logger.info(s"Load from url $currentUrl")
    Logger.debug(s"Content Topic => [pageNumber=$currentPageNumber,images=$allImages]")

    val page = new Page(topic.id, "%s %d".format(Messages("page.label"), currentPageNumber), currentPageNumber, allImages._1, allImages._2)

    val futureOptionPage: Future[Option[Page]] = future {
      PageCollection.saveOrUpdate(page)
      Some(page)
    }
    futureOptionPage
  }

  def getContentFromMongo(): Future[Option[Page]] = {
    Logger.info("getContentFromMongo")
    val futureOptionPage = PageCollection.findHeadByTopicIdAndPageNumber(topic.id, pageNumber.get)
    // TODO: see to avoid the flatMap and use a map instead.
    futureOptionPage.flatMap(checkMongo(_))
  }

  def checkMongo(optionPage: Option[Page]): Future[Option[Page]] = {
    Logger.info("checkMongo object...")
    // Page doesn't exist in mongo, retrieve it from hfr else return the future optionPage.
    optionPage match {
      case None => getContentFromHfr()
      case _    => future { optionPage }
    }
  }

}

object PageContentFinder {

  def apply(topicId: String, pageNumber: Option[Int]) = {
    val topic = TopicRepository.findTopic(topicId)
    new PageContentFinder(topic, pageNumber)
  }

}

