package tumblr.controllers.admin

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import tumblr.UserService

import views._

object TumblrAdminController extends Controller  {

  val loginForm = Form(
    tuple(
      "name"      -> text,
      "password"  -> text
    )
  )
  def index = Action { request =>
    if (request.session.isEmpty) {
      Redirect(routes.TumblrAdminController.login).flashing("error" -> "You must sign in.")
    } else Ok(views.html.admin())
  }

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def authenticate = Action.async { implicit request =>
    val (username, password) = loginForm.bindFromRequest.get
    UserService.authenticate(username, password).map { exists =>
      if (exists) Redirect(routes.TumblrAdminController.index).withSession(Security.username -> username)
      else Redirect(routes.TumblrAdminController.login).flashing("error" -> "Invalid username or password.")
    }
  }

  def logout = Action {
    Redirect(routes.TumblrAdminController.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

}