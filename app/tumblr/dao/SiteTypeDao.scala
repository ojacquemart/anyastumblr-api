package tumblr.dao

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import tumblr.model._
import reactivemongo.bson.BSONObjectID

object SiteTypeDao extends MongoDao[SiteType, BSONObjectID] {

  val collectionName = "site_types"

}
