package tumblr.model

import play.api.libs.json._

import reactivemongo.bson._
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

case class SiteType(name: String, slug: String, ordinal:  Int, enabled: Boolean)
    extends Slugifiable
    with Sortable
    with Enabled

object SiteType {

  def get(name: String) = new SiteType(name, "???", 1, true)

  implicit val formats: Format[SiteType] = Json.format[SiteType]
  implicit val writes: Writes[SiteType] = Json.writes[SiteType]

  val HFR = SiteType.get("Hfr")
  val LESJOIES = SiteType.get("Les joies")
  val MISC = SiteType.get("Divers")

}