package tumblr.dao

import tumblr.model.User

object UserDao extends MongoDao[User, String] {

  val collectionName: String = "users"

}
