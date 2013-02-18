package controllers

import play.api.Play.current
import play.api.mvc._
import play.api.Logger
import play.api.libs.json._

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers._

import play.modules.reactivemongo._
import play.modules.reactivemongo.PlayBsonImplicits._

import hfr._

object Application extends Controller with MongoController {

  val db = ReactiveMongoPlugin.db
  lazy val collection = db("test")

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

  def findAll = Action {
    Async {
      val query = QueryBuilder().query(Json.obj(  ))
      val found = collection.find[JsValue](query).toList

      found.map({ all =>
        Ok(all.foldLeft(JsArray(List()))( (obj, person) => obj ++ Json.arr(person) ))
      })
    }
  }

}
