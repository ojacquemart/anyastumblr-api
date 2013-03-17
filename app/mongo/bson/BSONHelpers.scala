package mongo.bson

import reactivemongo.bson.{BSONDateTime, BSONArray, BSONDocument, TraversableBSONDocument}
import reactivemongo.bson.handlers.{BSONWriter, BSONReader}
import org.joda.time.DateTime

trait BSONReaderHelper {

  def document[T](fieldName: String)(implicit doc: TraversableBSONDocument, reader: BSONReader[T]) = {
    reader.fromBSON(doc.getAs[BSONDocument](fieldName).get)
  }

  def listDocument[T](fieldName: String)(implicit doc: TraversableBSONDocument, reader: BSONReader[T]) = {
    doc.getAs[BSONArray](fieldName).get.toTraversable.toList.map {
      t =>
        reader.fromBSON(t.asInstanceOf[TraversableBSONDocument])
    }
  }

  def date(fieldName: String)(implicit doc: TraversableBSONDocument) = doc.getAs[BSONDateTime](fieldName).map(dt => new DateTime(dt.value))

}

trait BSONWriterHelper {

  def listDocument[T](xs: List[T])(implicit writer: BSONWriter[T]) = {
    BSONArray(xs.map {
      t => writer.toBSON(t)
    }: _*)
  }

  def document[T](doc: T)(implicit writer: BSONWriter[T]) = writer.toBSON(doc)

}