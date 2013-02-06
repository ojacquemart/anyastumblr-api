package hfr

import play.api.libs.json._

import play.api.Logger
import play.api.i18n.Messages
import jsoup.DocumentWrapper

object JsonContentFinder {

  def getContentFromFirstPageAsJson(topicId: String): JsValue = {
    getContentAsJson(topicId, None)
  }

  def getContentFromPageNumberAsJson(topicId: String, pageNumber: Int): JsValue = {
    getContentAsJson(topicId, Some(pageNumber))
  }

  def getContentAsJson(topicId: String, pageNumber: Option[Int]): JsValue = {
    import hfr.ContentFormats._

    val url = TopicRepository.findTopicUrl(topicId)
    Json.toJson(getContent(url, pageNumber))
  }

  def getContent(url: String, pageNumber: Option[Int]): TopicPage = {
    val urlAndPageNumber: (String, Int) = new TopicUrlAndPageNumberResolver(url, pageNumber).resolve()
    val currentUrl = urlAndPageNumber._1
    val currentPageNumber = urlAndPageNumber._2
    val allImages = new TopicPageImagesFinder(currentUrl).find()
    Logger.info("Load from url %s".format(currentUrl))
    Logger.debug("Content Topic => [pageNumber=%d,images=%s]".
      format(currentPageNumber, allImages))

    new TopicPage("%s %d".format(Messages("page.label"), currentPageNumber), currentPageNumber, allImages._1, allImages._2)
  }

}



