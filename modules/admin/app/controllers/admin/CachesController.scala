package controllers.admin

import play.api.cache.Cache
import play.api.Logger
import play.api.Play.current
import play.api.libs.json.Json
import play.api.mvc._

import common.CacheKeys

object CachesController extends Controller {

  def get = Action { request =>
    Logger.debug("Get all cache keys")
    Ok(Json.toJson(CacheKeys.Keys)).as("application/json")
  }

  def remove(cacheKey: String) = Action { request =>
    Logger.debug(s"Remove cache key $cacheKey")
    Cache.remove(cacheKey)
    NoContent
  }

  def removeAll() = Action { request =>
    Logger.debug(s"Remove all cache keys")
    CacheKeys.Keys.foreach(cacheKey => Cache.remove(cacheKey.key))
    NoContent
  }

}