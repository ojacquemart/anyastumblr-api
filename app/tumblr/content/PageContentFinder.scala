package tumblr.content

import play.api.Logger

import tumblr.model._

case class PageContentFinder(site: Site, pageNumber: Option[Int])  {

  def getContent(): Page = getPageFromSite()

  def getPageFromSite(): Page = {
    Logger.debug(s"getPage from $site")
    val urlAndPageNumber: (String, Int) = new PageNumberResolver(site, pageNumber).resolve()
    val currentUrl = urlAndPageNumber._1
    val currentPageNumber = urlAndPageNumber._2
    Logger.debug(s"Current url $currentUrl with image $currentPageNumber")

    val allImages = new ImagesFinder(currentUrl, site.configuration).find()
    val allImagesSize = allImages._1.size + allImages._2.size
    Logger.info(s"Load from src $currentUrl")
    Logger.debug(s"\tContent page => [pageNumber=$currentPageNumber,imagesSize=$allImagesSize]")

    val link = Link.get(currentUrl, site.name, currentPageNumber)
    new Page(site.slug, currentPageNumber, allImages._1, allImages._2, link)
  }

}

object PageContentFinder {

  def get(site: Site, pageNumber: Option[Int]): PageContentFinder = {
    new PageContentFinder(site, pageNumber)
  }

}

