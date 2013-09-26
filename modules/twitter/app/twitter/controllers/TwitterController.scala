package twitter.controllers

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.mvc._
import play.api.libs.json.Json

import twitter.services.TweetFetcher

object TwitterController extends Controller {

  def index(query: String) = Action.async { request =>
    val futureTweets = TweetFetcher.fetch(query)
    futureTweets map {
      tweets => Ok(Json.toJson(tweets)).as("application/json")
    }
  }

  def stream(query: String) = Action {
    Ok.chunked(TweetFetcher.chunckCountRecents(query)).as("text/event-stream")
  }

}
