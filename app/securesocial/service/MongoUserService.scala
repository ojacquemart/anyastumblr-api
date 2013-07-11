package securesocial.service

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import play.api.Play.current
import play.api.cache.Cache
import play.api._

import securesocial.core._
import securesocial.core.providers.Token
import securesocial.core.UserId

import tumblr.dao.UserDao

/**
 * Mongo user service.
 */
class MongoUserService(application: Application) extends UserServicePlugin(application) {

  def generateCacheKey(id: UserId) = s"user.${id.id}.${id.providerId}"

  /**
   * Finds the user matching the id and the provider id.
   */
  def find(id: UserId): Option[Identity] = {
    val cacheKey = generateCacheKey(id)
    Cache.getOrElse[Option[Identity]](cacheKey) {
      // User service doesn't return futures, needs to use Await.result...
      // @see: https://github.com/jaliss/securesocial/issues/193
      Await.result(UserDao.findById(id), Duration.create(1, "min"))
    }
  }

  def save(user: Identity): Identity = {
    Logger.debug(s"Save user: $user")
    user
  }

  // None or empty implementations...

  def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = None

  def save(token: Token) {}

  def findToken(token: String): Option[Token] = None

  def deleteToken(uuid: String) {}

  def deleteTokens() {}

  def deleteExpiredTokens() {}

}