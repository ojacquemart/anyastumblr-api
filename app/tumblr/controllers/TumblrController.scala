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
import tumblr.model.Link

object TumblrController extends Controller {

  def index = Action {
    request =>
      Ok(views.html.index())
  }

  def sites = Cached(CacheKeys.ActionSites) {
    Action {
      Async {
        Logger.info("Getting sites...")

        import tumblr.model.AdminSiteJSON.format
        SiteDao.findAll().map { sites =>
          Ok(Json.toJson(sites)(Writes.seq(tumblr.model.Site.SimpleWrites))).as("application/json")
        }
      }
    }
  }

  def stats = Action {
    Async {
      PageDao.count().map { pagesCount =>
        Ok(Json.obj("count" -> pagesCount))
      }
    }
  }

  def getSiteTotalPages(siteId: String) = Action {
    Async {
      import Link.writes
      SiteLastPageInfos.get(siteId).map(maybeLink => {
        // The site may not permit to give the last page. In that case, an empty json object is returned.
        val jsValue = maybeLink match {
          case Some(link) => Json.toJson(link)
          case None       => Json.obj()
        }

        Ok(jsValue).as("application/json")
      })
    }
  }

  def getSiteFirstPage(siteId: String) = getSitePage(siteId)

  def getSitePageByPageNumber(siteId: String, pageNumber: Int) = getSitePage(siteId, Some(pageNumber))

  def getSitePage(slug: String, pageNumber: Option[Int] = None) = Action {
    Async {
      Logger.debug(s"Get page $pageNumber for site $slug")
      val futurePage = for {
        finder <- PageContentFinder.get(slug, pageNumber)
        content <- finder.getContent()
      } yield content

      futurePage.map { optionPage =>
        Ok(Json.toJson(optionPage.get)(Writes.of(tumblr.model.Page.simpleWriter))).as("application/json")
      }
    }
  }

}