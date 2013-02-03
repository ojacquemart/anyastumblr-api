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
    Ok(views.html.index(TopicRepository.getTopics())).withSession("url" -> "http://forum.hardware.fr/hfr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_7682.htm")
  }

  def topics = Action {
    Logger.info("Topics...")
    Ok(TopicRepository.getTopicsAsJson()).as("application/json")
  }

  def topicGifs(id: String) = Action {
    val topic: Topic = TopicRepository.findTopic(id)
    Logger.info("Get topic from id: %s".format(id))

    val url = topic.url
    val json = Cache.getOrElse[JsValue](url) {
      Logger.info("Load gifs")
      new ContentFinder(url).getContentAsJson()
    }
    Ok(json).as("application/json")
  }

  def topicChangePage(id: String, page: Int) = Action {
    TODO
  }

  def gifs = Action { request =>
    Logger.info("Gifs for " + request.session.get("url"))
    val url = "http://forum.hardware.fr/hfr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_7682.htm"
    val json = Cache.getOrElse[JsValue](url) {
      Logger.info("Load gifs")
      new ContentFinder(url).getContentAsJson()
    }
    Ok(json).as("application/json")
  }

}