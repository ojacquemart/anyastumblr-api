package model

case class SiteType(name: String)

object SiteType {
  val HFR = new SiteType("Hfr")
  val LESJOIES = new SiteType("Les joies")
  val MISC = new SiteType("Divers")

}