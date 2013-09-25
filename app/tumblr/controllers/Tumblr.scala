package tumblr.controllers

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api._
import play.api.libs.json._
import play.api.mvc._
import cache.Cached

import tumblr._
import tumblr.dao._
import tumblr.model.{PageWithTotal, Link}

object Tumblr extends Controller {

  def getSites = Cached(CacheKeys.ActionSites) {
    Action.async {
      Logger.info("Getting sites grouped by site type...")

      SiteDao.findAllGroupedBySiteType().map { sites =>
        Ok(Json.toJson(sites)(Writes.of(tumblr.model.SitesByType.sitesInfoWrites))).as("application/json")
      }
    }
  }

  def getSiteFirstPage(siteId: String) = getSitePage(siteId)

  def getSitePageByPageNumber(siteId: String, pageNumber: Int) = getSitePage(siteId, Some(pageNumber))

  def getSitePage(slug: String, pageNumber: Option[Int] = None) = Action.async {
    Logger.debug(s"Get page $pageNumber for site $slug")
    val futurePageWithTotal = for {
      finder <- PageContentFinder.get(slug, pageNumber)
      content <- finder.getContent()
      totalPage <- SiteLastPageInfos.get(slug)
    } yield PageWithTotal(content.get, totalPage)

    futurePageWithTotal.map { optionPageWithTotal =>
      Ok(Json.toJson(optionPageWithTotal)(Writes.of(tumblr.model.Page.simplePageWithTotalWriter))).as("application/json")
    }
  }

}