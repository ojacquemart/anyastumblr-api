package tumblr.dao

import scala.concurrent.Future
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global

import play.api.Play.current

import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.{Reads, Json}
import reactivemongo.core.commands.Count

trait MongoDao[T] {

  def db = ReactiveMongoPlugin.db
  def collection: JSONCollection = db.collection(collectionName())
  def collectionName(): String

  /**
   * Find all documents from the collection.
   *
   * @return a Future list of documents.
   */
  def findAll()(implicit reader: Reads[T]): Future[List[T]]= {
    collection
      .find(Json.obj())
      .cursor[T]
      .toList
  }

  /**
   * Count the number of documents from the collection.
   */
  def count(): Future[Int] = db.command(Count(collectionName))

}