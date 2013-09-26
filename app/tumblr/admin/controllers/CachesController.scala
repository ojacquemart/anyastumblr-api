package tumblr.admin.controllers

import play.api.cache.Cache
import play.api.Logger
import play.api.Play.current
import play.api.libs.json.Json
import play.api.mvc._

import tumblr.CacheKeys
import tumblr.CacheKey.writes

object CachesController extends Controller {

  def list = Action { request =>
    Logger.debug("Get all cache keys")
    Ok(Json.toJson(CacheKeys.keys)).as("application/json")
  }

  def remove(cacheKey: String) = Action { request =>
    Logger.debug(s"Remove cache key $cacheKey")
    Cache.remove(cacheKey)
    NoContent
  }

  def removeAll() = Action { request =>
    Logger.debug(s"Remove all cache keys")
    CacheKeys.keys.foreach(cacheKey => Cache.remove(cacheKey.key))
    NoContent
  }

}