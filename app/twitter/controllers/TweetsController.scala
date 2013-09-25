package twitter.controllers

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.mvc._

import twitter._

object TweetsController extends Controller {

  def index(query: String) = Action.async { request =>
    val futureTweets = Tweet.fetch(query)
    futureTweets map {
      tweets => Ok(Json.toJson(tweets)).as("application/json")
    }
  }

  def stream(query: String) = Action {
    Ok.chunked(Tweet.chunckCountRecents(query)).as("text/event-stream")
  }

}