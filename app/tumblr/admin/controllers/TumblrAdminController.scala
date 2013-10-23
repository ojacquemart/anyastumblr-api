package tumblr.admin.controllers

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import tumblr.admin.service.UserService
import tumblr.model.User

object TumblrAdminController extends Controller  {

  val loginForm = Form(
    tuple(
      "name"      -> text,
      "password"  -> text
    )
  )

  def authenticate = Action.async { implicit request =>
    val (username, password) = loginForm.bindFromRequest.get
    UserService.authenticate(username, password).map { exists =>
      if (exists) Ok(User.encodeBase64(username, password))
      else Unauthorized
    }
  }

}