package tumblr.model

import play.api.libs.json._

import reactivemongo.bson._
import play.modules.reactivemongo.json.BSONFormats._

case class SiteType(_id: Option[BSONObjectID], var name: String) extends OptionBSONObjectIdModel {
}

object SiteType {

  def get(name: String) = new SiteType(Some(BSONObjectID.generate), name)

  implicit val formats: Format[SiteType] = Json.format[SiteType]
  implicit val writes: Writes[SiteType] = Json.writes[SiteType]

  val HFR = SiteType.get("Hfr")
  val LESJOIES = SiteType.get("Les joies")
  val MISC = SiteType.get("Divers")

}