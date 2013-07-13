package tumblr

import scala.concurrent.future
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

import play.api._

import tumblr.model._
import tumblr.dao._

case class PageContentFinder(site: Site, pageNumber: Option[Int])  {

  def getContent() = getContentFromSite()

  def getContentFromSite(): Future[Option[Page]] = {
    Logger.info("getContentFromSite")

    val page = getPageFromSite()
    val futureOptionPage: Future[Option[Page]] = future {
      PageDao.saveOrUpdate(page)
      Some(page)
    }
    futureOptionPage
  }

  def getPageFromSite(): Page = {
    Logger.debug(s"getPage from $site")
    val urlAndPageNumber: (String, Int) = new PageNumberResolver(site, pageNumber).resolve()
    val currentUrl = urlAndPageNumber._1
    val currentPageNumber = urlAndPageNumber._2
    Logger.debug(s"Current url $currentUrl with image $currentPageNumber")

    val allImages = new ImagesFinder(currentUrl, site.configuration).find()
    val allImagesSize = allImages._1.size + allImages._2.size
    Logger.info(s"Load from src $currentUrl")
    Logger.debug(s"Content Site => [pageNumber=$currentPageNumber,imagesSize=$allImagesSize]")

    val page = new Page(site._id.get.toString(), currentPageNumber, allImages._1, allImages._2)
    page.link = Some(Link.get(currentUrl, site.name, currentPageNumber))
    page
  }

}

object PageContentFinder {

  def apply(site: String, pageNumber: Option[Int]) = {
    Logger.info(s"Site siteId=$site")
    val topic = SiteDao.findSite(site)
    new PageContentFinder(topic, pageNumber)
  }

}

