package tumblr.model

import play.api.libs.json.{Writes, Json, Format}

case class SiteType(name: String)

object SiteType {
  implicit val format: Format[SiteType] = Json.format[SiteType]
  implicit val writes: Writes[SiteType] = Json.writes[SiteType]

  val HFR = new SiteType("Hfr")
  val LESJOIES = new SiteType("Les joies")
  val MISC = new SiteType("Divers")

}