package tumblr.model

import play.api.Logger

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CssSelector(cssQuery: String, htmlAttribute: Option[String] = None)

case class CssSelectors(images: CssSelector, text: Option[CssSelector])

case class ImageRule(exclude: String, startsWith: List[String])

case class ChangePageDescriptor(regex: String, replacement: String)

case class PageNumberDescriptor(cssSelector: CssSelector, regex: String)

case class PageResolver(pageNumberDescriptor: Option[PageNumberDescriptor],
                        changePageDescriptor: ChangePageDescriptor)

case class Configuration(cssSelectors: CssSelectors,
                         lastPageByCss: Boolean,
                         navigationAcending: Boolean,
                         pageResolver: PageResolver,
                         imageRule: Option[ImageRule])

case class Site(id: String,
                siteType: String,
                name: String,
                url: String,
                configuration: Configuration)

object AdminSiteJSON {
  implicit val cssSelectorWrites = (
    (__ \ "cssQuery").write[String] and
      (__ \ "htmlAttribute").write[Option[String]]
    )(unlift(CssSelector.unapply))
  implicit val cssSelectorReads = (
    (__ \ "cssQuery").read[String] and
      (__ \ "htmlAttribute").read[Option[String]]
    )(CssSelector.apply _)

  implicit val cssSelectorsWrites = (
    (__ \ "images").write[CssSelector] and
      (__ \ "text").write[Option[CssSelector]]
    )(unlift(CssSelectors.unapply))
  implicit val cssSelectorsReads = (
    (__ \ "images").read[CssSelector] and
      (__ \ "text").read[Option[CssSelector]]
    )(CssSelectors.apply _)

  implicit val pageNumberDescWrites = Json.writes[PageNumberDescriptor]
  implicit val pageNumberDescReads = Json.reads[PageNumberDescriptor]

  implicit val changePageDescWrites = Json.writes[ChangePageDescriptor]
  implicit val changePageDescReads = Json.reads[ChangePageDescriptor]
  implicit val imageRuleWrites = Json.writes[ImageRule]
  implicit val imageRuleReads = Json.reads[ImageRule]

  implicit val pageResolverWrites = (
    (__ \ "pageNumberDescriptor").write[Option[PageNumberDescriptor]] and
      (__ \ "changePageDescriptor").write[ChangePageDescriptor]
    )(unlift(PageResolver.unapply))
  implicit val pageResolverReads = (
    (__ \ "pageNumberDescriptor").read[Option[PageNumberDescriptor]] and
      (__ \ "changePageDescriptor").read[ChangePageDescriptor]
    )(PageResolver.apply _)

  implicit val configurationWrites = (
    (__ \ "cssSelectors").write[CssSelectors] and
      (__ \ "lastPageByCss").write[Boolean] and
      (__ \ "navigationAcending").write[Boolean] and
      (__ \ "pageResolver").write[PageResolver] and
      (__ \ "imageRule").write[Option[ImageRule]]
    )(unlift(Configuration.unapply))
  implicit val configurationReads = (
    (__ \ "cssSelectors").read[CssSelectors] and
      (__ \ "lastPageByCss").read[Boolean] and
      (__ \ "navigationAcending").read[Boolean] and
      (__ \ "pageResolver").read[PageResolver] and
      (__ \ "imageRule").read[Option[ImageRule]]
    )(Configuration.apply _)

  implicit val siteWrites = (
    (__ \ "id").write[String] and
    (__ \ "siteType").write[String] and
    (__ \ "name").write[String] and
    (__ \ "url").write[String] and
      (__ \ "configuration").write[Configuration]
    )(unlift(Site.unapply))
  implicit val siteReads = (
    (__ \ "id").read[String] and
      (__ \ "siteType").read[String] and
      (__ \ "name").read[String] and
      (__ \ "url").read[String] and
      (__ \ "configuration").read[Configuration]
    )(Site.apply _)
}

object Site {

  implicit object SiteWrites extends Writes[Site] {

    def writes(site: Site): JsValue = {
      JsObject(
        List(
          "type" -> JsString(site.siteType),
          "id" -> JsString(site.id),
          "name" -> JsString(site.name)
        ))
    }

  }

  def get(siteType: SiteType, name: String, url: String, configuration: Configuration) = {
    // Id in sha1...
    // TODO: use mongodb on another nosql collectionImages to store sites and maybe more... like every images loaded...
    val md = java.security.MessageDigest.getInstance("SHA-1")
    val id = new sun.misc.BASE64Encoder().encode(md.digest((url + name).getBytes)).replace("/", "").replace("+", "")

    Logger.info(s"Site siteId=$id for $name")
    new Site(id, siteType.name, name, url, configuration)
  }
}