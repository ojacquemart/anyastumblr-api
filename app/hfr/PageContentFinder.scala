package hfr

import play.api._

import scala.concurrent.future
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

case class PageContentFinder(topic: Topic, pageNumber: Option[Int])  {

  def getContent() = {
    pageNumber match {
      case None         => getContentFromSite()
      case Some(p: Int) => getContentFromMongo()
    }
  }

  def getContentFromSite(): Future[Option[Page]] = {
    Logger.info("getContentFromSite")

    val page = getPageFromSite()
    val futureOptionPage: Future[Option[Page]] = future {
      PageCollection.saveOrUpdate(page)
      Some(page)
    }
    futureOptionPage
  }

  def getPageFromSite(): Page = {
    Logger.debug(s"getPage from $topic")
    val urlAndPageNumber: (String, Int) = new PageNumberResolver(topic, pageNumber).resolve()
    val currentUrl = urlAndPageNumber._1
    val currentPageNumber = urlAndPageNumber._2
    Logger.debug(s"Current src $currentUrl with page $currentPageNumber")

    val allImages = new PageImagesFinder(currentUrl, topic.configuration).find()
    val allImagesSize = allImages._1.size + allImages._2.size
    Logger.info(s"Load from src $currentUrl")
    Logger.debug(s"Content Topic => [pageNumber=$currentPageNumber,imagesSize=$allImagesSize]")

    val page = new Page(topic.id, currentPageNumber, allImages._1, allImages._2)
    page
  }

  def getContentFromMongo(): Future[Option[Page]] = {
    Logger.info("getContentFromMongo")
    val futureOptionPage = PageCollection.findHeadByTopicIdAndPageOffset(topic.id, pageNumber.get)
    // TODO: see to avoid the flatMap and use a map instead.
    futureOptionPage.flatMap(checkMongoContent(_))
  }

  def checkMongoContent(optionPage: Option[Page]): Future[Option[Page]] = {
    Logger.debug("checkMongoContent object... retrieve it from hfr or update existing one.")
    // Page doesn't exist in mongo, retrieve it from hfr else return the future optionPage.
    optionPage match {
      case None => getContentFromSite()
      case _    => future {
        future {
          val existingPage = getPageFromSite()
          PageCollection.update(existingPage)
        }
        optionPage
      }
    }
  }
}

object PageContentFinder {

  def apply(topicId: String, pageNumber: Option[Int]) = {
    Logger.info(s"Topic id=$topicId")
    val topic = TopicRepository.findTopic(topicId)
    new PageContentFinder(topic, pageNumber)
  }

}

