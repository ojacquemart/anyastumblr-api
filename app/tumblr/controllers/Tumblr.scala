package tumblr.controllers

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api._
import play.api.libs.json._
import play.api.mvc._
import cache.Cached

import tumblr.CacheKeys
import tumblr.services.SiteService

object Tumblr extends Controller {

  def getSites = Cached(CacheKeys.ActionSites) {
    Action.async {
      Logger.info("Getting sites grouped by site type...")

      SiteService.findAllGroupedBySiteType().map { sites =>
        Ok(Json.toJson(sites)(Writes.of(tumblr.model.SitesByType.sitesInfoWrites))).as("application/json")
      }
    }
  }

  def getSiteFirstPage(siteId: String) = getSitePage(siteId)

  def getSitePageByPageNumber(siteId: String, pageNumber: Int) = getSitePage(siteId, Some(pageNumber))

  def getSitePage(slug: String, pageNumber: Option[Int] = None) = Action.async {
    SiteService.getPage(slug, pageNumber).map { pageWithTotal =>
      Ok(Json.toJson(pageWithTotal)(Writes.of(tumblr.model.Page.simplePageWithTotalWriter))).as("application/json")
    }
  }

}