package common

import play.api.libs.json.{Json}

case class CacheKey(key: String, name: String)

object CacheKey {

  implicit val writes = Json.writes[CacheKey]
}

object CacheKeys {

  val ActionSites = "tumblr.action.sites"
  val Sites = "tumblr.sites"


  val Keys = List(
    CacheKey(Sites, "Sites"),
    CacheKey(ActionSites, "Action sites")
  )

}

