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

  def topics = Action {
    Logger.info("Topics...")
    Ok(TopicRepository.getTopicsAsJson()).as("application/json")
  }

  def getLastPageFromTopic(topicId: String) = Action {
    Async {
      val futurePage = PageContentFinder(topicId, None).getContent()
      futurePage map {
        case page => {
          Ok(Json.toJson(page.get)).as("application/json")
        }
      }
    }
  }

  def getPageFromTopicAndPageNumber(topicId: String, pageNumber: Int) = Action {
    val result = PageContentFinder(topicId, Some(pageNumber)).getContent()
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
