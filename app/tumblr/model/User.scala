package tumblr.model

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.mindrot.jbcrypt.BCrypt

case class User(_id: String, password: String) extends MongoModel[String] {
  def name: String = _id

  override def toString(): String = _id
}

object User {

  implicit val userFormat = Json.format[User]

  def generatePassword(plainPassword: String) = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10))

}
