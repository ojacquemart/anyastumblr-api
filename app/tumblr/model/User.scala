package tumblr.model

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.mindrot.jbcrypt.BCrypt

case class User(_id: String, password: String) extends MongoModel[String] {
  def name: String = _id

  override def toString(): String = _id
}

object User {

  val base64toArrayOfStrings: (String) => User =
    base4 => {
      val credentials = new String(new sun.misc.BASE64Decoder().decodeBuffer(base4)).split(":")
      User(credentials(0), credentials(1))
    }

  implicit val userFormat = Json.format[User]

  def generatePassword(plainPassword: String) = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10))

  def decodeBase64(base64: String) = base64toArrayOfStrings(base64)

}
