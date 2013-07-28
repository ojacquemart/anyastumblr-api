package tumblr.controllers

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.mvc._

import tumblr.PageStats

object TumblrStats extends Controller {

  def index = Action {
    Async {
      PageStats.generate.map(stat => {
        Ok(Json.toJson(stat)).as("application/json")
      }).recover { case e => BadRequest(e.getMessage) }
    }
  }
}
