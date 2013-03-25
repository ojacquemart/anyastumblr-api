package mongo

import reactivemongo.bson.BSONArray
import reactivemongo.bson.handlers.BSONWriter

package object Implicits {

  implicit def listToBSONArray[T](list: List[T])(implicit writer: BSONWriter[T]): BSONArray =  {
    BSONArray(list.map { t => writer.toBSON(t) }: _*)
  }



}
