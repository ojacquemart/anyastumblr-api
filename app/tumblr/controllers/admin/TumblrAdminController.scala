package tumblr.controllers.admin

import play.api.mvc._

object TumblrAdminController extends Controller with securesocial.core.SecureSocial {

  def index = SecuredAction { request =>
      Ok(views.html.admin())
  }

}