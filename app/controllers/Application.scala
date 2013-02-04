package controllers

import play.api.Play.current
import play.api.mvc._
import play.api.cache.Cache
import play.api.Logger

import hfr._
import play.api.libs.json._

object Application extends Controller {

  def index = Action {  request =>
    Logger.info("Index...")
    Ok(views.html.index())
  }

  def topics = Action {
    Logger.info("Topics...")
    Ok(TopicRepository.getTopicsAsJson()).as("application/json")
  }

  def topicGifs(id: String) = Action {
    Ok(ContentFinder(id).getContentAsJson()).as("application/json")
  }

  def topicChangePage(id: String, page: Int) = Action {
    Ok(ContentFinder(id, page).getContentAsJson()).as("application/json")
  }

}
