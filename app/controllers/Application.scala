package controllers

import play.api.mvc._
import play.api.Logger

import hfr._

object Application extends Controller {

  def index = Action {
    request =>
      Logger.info("Index...")
      Ok(views.html.index())
  }

  def topics = Action {
    Logger.info("Topics...")
    Ok(TopicRepository.getTopicsAsJson()).as("application/json")
  }

  def topicGifs(topicId: String) = Action {
    Ok(JsonContentFinder.getContentFromFirstPageAsJson(topicId)).as("application/json")
  }

  def topicChangePage(topicId: String, pageNumber: Int) = Action {
    Ok(JsonContentFinder.getContentFromPageNumberAsJson(topicId, pageNumber)).as("application/json")
  }

}
