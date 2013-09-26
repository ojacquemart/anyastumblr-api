package tumblr.controllers

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.mvc._

import tumblr.services.SiteStatsService

object TumblrStatsController extends Controller {

  def index = Action.async {
    SiteStatsService.generate.map(stats => {
      Ok(Json.toJson(stats)).as("application/json")
    }).recover { case e => BadRequest(e.getMessage) }
  }

}
