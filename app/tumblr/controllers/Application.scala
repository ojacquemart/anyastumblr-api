package tumblr.controllers

import play.api.Play.current
import play.api.cache.Cache

import play.api.mvc._

import play.api.Logger

object Application extends Controller {

  def index = Action { request =>
    Logger.debug("Cache value from admin  " + Cache.get("ok"))
    Ok(views.html.index())
  }

}
