package controllers

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.mvc._

import twitter._

object Tweets extends Controller {

  def index(query: String) = Action {
    implicit request =>
      Async {
        val futureTweets = Tweet.fetch(query)
        futureTweets map {
          tweets => Ok(Json.toJson(tweets)).as("application/json")
        }
      }
  }

  def stream(query: String) = Action {
    Ok.stream(Tweet.streamCountRecents(query)).as("text/event-stream")
  }

}