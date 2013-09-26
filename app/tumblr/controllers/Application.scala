package tumblr.controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action { request =>
    Ok(views.html.index())
  }

}
