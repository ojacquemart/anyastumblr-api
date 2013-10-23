package tumblr.admin.service

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.concurrent.future

import play.api._

import tumblr.dao.UserDao
import tumblr.model.User

import org.mindrot.jbcrypt.BCrypt
import play.api.mvc.{Security, RequestHeader}

/**
 * User service.
 */
object UserService {

  val HeaderAuthName = "Authorization"
  val HeaderAuthSperator = ":"

  /**
   * Checks if the current session contains a username.
   */
  def hasCurrentUser(request: RequestHeader): Future[Boolean] = request.headers.get(HeaderAuthName) match {
    case Some(base64) => {
      val user = User.decodeBase64(base64)
      authenticate(user.name, user.password)
    }
    case _ => future { false }
  }

  /**
   * Finds the user matching username and check password.
   */
  def authenticate(username: String, plainPassword: String): Future[Boolean] = {
    Logger.debug(s"Trying to authenticate $username")
    for {
      maybeUser <- UserDao.findByPK(username)
      existsAndPasswordMatches = checkPasswords(plainPassword, maybeUser)
    } yield existsAndPasswordMatches
  }

  def checkPasswords(plainPassword: String, maybeUser: Option[User]): Boolean = maybeUser match {
    case None => false
    case Some(user) => {
      val checkpw = BCrypt.checkpw(plainPassword, user.password)
      Logger.debug(s"Found one user '${user._id}' and checkpw = $checkpw")
      checkpw
    }
  }

}
