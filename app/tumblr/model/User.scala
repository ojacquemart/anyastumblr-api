package tumblr.model

import play.api.libs.functional.syntax._
import play.api.libs.json._
import securesocial.core.AuthenticationMethod
import securesocial.core.Identity
import securesocial.core.OAuth1Info
import securesocial.core.OAuth2Info
import securesocial.core.PasswordInfo
import securesocial.core.IdentityId
import securesocial.core.providers.utils.PasswordHasher
import org.mindrot.jbcrypt.BCrypt

case class User(identityId: IdentityId,
                firstName: String,
                lastName: String,
                fullName: String,
                email: Option[String],
                avatarUrl: Option[String],
                authMethod: AuthenticationMethod,
                oAuth1Info: Option[OAuth1Info] = None,
                oAuth2Info: Option[OAuth2Info] = None,
                passwordInfo: Option[PasswordInfo] = None) extends Identity

object User {

  def get(id: IdentityId,
          firstName: String,
          lastName: String,
          fullName: String,
          email: Option[String],
          avatarUrl: Option[String],
          authMethod: AuthenticationMethod,
          oAuth1Info: Option[OAuth1Info] = None,
          oAuth2Info: Option[OAuth2Info] = None,
          plainPassword: String): User = {
    new User(
      id,
      firstName,
      lastName,
      fullName,
      email,
      avatarUrl,
      authMethod,
      oAuth1Info,
      oAuth2Info,
      Some(PasswordInfo(PasswordHasher.BCryptHasher, BCrypt.hashpw(plainPassword, BCrypt.gensalt(10))))
    )
  }

  implicit val userIdFormat = Json.format[IdentityId]
  implicit val oAuth1InfoFormat = Json.format[OAuth1Info]
  implicit val oAuth2InfoFormat = Json.format[OAuth2Info]
  implicit val passwordInfoFormat = Json.format[PasswordInfo]

  // Auth method is stored with "value" property.
  implicit val authenticationMethodFormat = (__ \ 'value)
    .format[String]
    .inmap(
    (m: String) => AuthenticationMethod(m),
    (a: AuthenticationMethod) => a.method)

  implicit val userFormat = Json.format[User]
}
