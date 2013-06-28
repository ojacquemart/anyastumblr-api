package controllers

import scala.concurrent.{ExecutionContext, Future}
import scala.Some

import play.api.Play.current
import play.api._
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import cache.Cached

import tumblr.model._
import tumblr.model.Page.writes
import tumblr.dao._
import tumblr._

object Application extends Controller with MongoController {

  def index = Action { request =>
    Ok(views.html.index())
  }

  def sites = Cached("app.sites") {
    Action {
      Logger.info("Getting sites...")
      Ok(SiteDao.getSitesAsJson()).as("application/json")
    }
  }

  def stats = Action {
    Async {
      val futurePagesCount = PageDao.count()
      futurePagesCount.map { pagesCount =>
        Ok(Json.obj("count" -> pagesCount))
      }
    }
  }

  def getSiteTotalPages(siteId: String) = Action {
    Ok(SiteLastPageInfos.getAsJson(siteId))
  }

  def getSiteFirstPage(siteId: String) = getSitePage(siteId)

  def getSitePageByPageNumber(siteId: String, pageNumber: Int) = getSitePage(siteId, Some(pageNumber))

  def getSitePage(siteId: String, pageNumber: Option[Int] = None) = Action {
    Async {
      val futurePage = PageContentFinder(siteId,pageNumber).getContent()
      futurePage.map { optionPage =>
        Ok(Json.toJson(optionPage.get)).as("application/json")
      }
    }
  }

}