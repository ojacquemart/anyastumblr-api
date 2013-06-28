package tumblr.model

trait MongoModel[T] {
  def _id: T

  def id = _id
}
