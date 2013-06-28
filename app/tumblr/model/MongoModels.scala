package tumblr.model

import reactivemongo.bson.BSONObjectID

trait MongoModel[PK] {

  def _id: PK

}

trait OptionBSONObjectIdModel extends MongoModel[Option[BSONObjectID]] {
  def id = _id
}
