package tumblr.controllers

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.mvc._

import tumblr.dao._

object TumblrStats extends Controller {

  def index = Action {
    Async {
      PageDao.count().map { pagesCount =>
        Ok(Json.obj("count" -> pagesCount)).as("application/json")
      }
    }
  }

}
