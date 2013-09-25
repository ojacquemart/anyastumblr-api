package controllers.admin

import play.api.mvc._

object AdminController extends Controller {

  def index = Action { request =>
    Ok("ADMIN")
  }

}
