package controllers

import play.api._
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import scala.concurrent.{ExecutionContext, Future}

import hfr._
import hfr.PageJSON._

object Application extends Controller with MongoController {

  def index = Action {
    request =>
      Ok(views.html.index())
  }

  def sites = Action {
    Logger.info("Getting sites...")
    Ok(SiteRepository.getSitesAsJson()).as("application/json")
  }

  def getLastSitePage(siteId: String) = Action {
    Async {
      val futurePage = PageContentFinder(siteId, None).getContent()
      futurePage map {
        case page => {
          Ok(Json.toJson(page.get)).as("application/json")
        }
      }
    }
  }

  def getSitePageByPageNumber(siteId: String, pageNumber: Int) = Action {
    val result = PageContentFinder(siteId, Some(pageNumber)).getContent()
    Async {
      result match {
        case futurePage =>
          futurePage.map {
            optionPage =>
              Ok(Json.toJson(optionPage.get)).as("application/json")
          }
      }
    }
  }


}
