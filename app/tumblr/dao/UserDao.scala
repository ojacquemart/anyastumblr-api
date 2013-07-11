package tumblr.dao

import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

import play.api.Logger
import play.api.libs.json._

import securesocial.core._

import tumblr.model.User
import tumblr.model.User._

object UserDao extends MongoDao[User, UserId] {

  val collectionName: String = "users"

  def findById(id: UserId): Future[Option[User]] = {
    Logger.debug(s"$collectionName - findById($id")
    collection.find(Json.obj("id" -> id)).one[User]
  }

}
