package tumblr.model

import play.api.Logger

import play.api.libs.functional.syntax._
import play.api.libs.json._

import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson._

case class CssSelector(cssQuery: String, htmlAttribute: Option[String] = None)

case class CssSelectors(images: CssSelector, text: Option[CssSelector])

case class ImageRule(exclude: String, startsWith: List[SimpleValue])

case class SimpleValue(value: String)

case class ChangePageDescriptor(regex: String, replacement: String)

case class PageNumberDescriptor(cssSelector: CssSelector, regex: String)

case class PageResolver(pageNumberDescriptor: Option[PageNumberDescriptor],
                        changePageDescriptor: ChangePageDescriptor)

case class Configuration(cssSelectors: CssSelectors,
                         lastPageByCss: Boolean,
                         navigationAcending: Boolean,
                         pageResolver: PageResolver,
                         imageRule: Option[ImageRule])

case class Site(_id: Option[BSONObjectID],
                siteType: String,
                name: String,
                url: String,
                configuration: Configuration) extends OptionBSONObjectIdModel

object AdminSiteJSON {
  implicit val cssSelectorWrite = (
    (__ \ "cssQuery").write[String] and
      (__ \ "htmlAttribute").writeNullable[String]
    )(unlift(CssSelector.unapply))
  implicit val cssSelectorRead = (
    (__ \ "cssQuery").read[String] and
      (__ \ "htmlAttribute").readNullable[String]
    )(CssSelector.apply _)

  implicit val cssSelectorsWrite = (
    (__ \ "images").write[CssSelector] and
      (__ \ "text").writeNullable[CssSelector]
    )(unlift(CssSelectors.unapply))
  implicit val cssSelectorsRead = (
    (__ \ "images").read[CssSelector] and
      (__ \ "text").readNullable[CssSelector]
    )(CssSelectors.apply _)

  implicit val simpleValueWrite = Json.writes[SimpleValue]
  implicit val simpleValueRead = Json.reads[SimpleValue]
  implicit val pageNumberDescWrite = Json.writes[PageNumberDescriptor]
  implicit val pageNumberDescRead = Json.reads[PageNumberDescriptor]

  implicit val changePageDescWrite = Json.writes[ChangePageDescriptor]
  implicit val changePageDescRead = Json.reads[ChangePageDescriptor]
  implicit val imageRuleWrite = Json.writes[ImageRule]
  implicit val imageRuleRead = Json.reads[ImageRule]

  implicit val pageResolverWrite = (
    (__ \ "pageNumberDescriptor").writeNullable[PageNumberDescriptor] and
      (__ \ "changePageDescriptor").write[ChangePageDescriptor]
    )(unlift(PageResolver.unapply))
  implicit val pageResolverRead = (
    (__ \ "pageNumberDescriptor").readNullable[PageNumberDescriptor] and
      (__ \ "changePageDescriptor").read[ChangePageDescriptor]
    )(PageResolver.apply _)

  implicit val configurationWrite = (
    (__ \ "cssSelectors").write[CssSelectors] and
      (__ \ "lastPageByCss").write[Boolean] and
      (__ \ "navigationAscending").write[Boolean] and
      (__ \ "pageResolver").write[PageResolver] and
      (__ \ "imageRule").writeNullable[ImageRule]
    )(unlift(Configuration.unapply))
  implicit val configurationRead = (
    (__ \ "cssSelectors").read[CssSelectors] and
      (__ \ "lastPageByCss").read[Boolean] and
      (__ \ "navigationAscending").read[Boolean] and
      (__ \ "pageResolver").read[PageResolver] and
      (__ \ "imageRule").readNullable[ImageRule]
    )(Configuration.apply _)

  implicit val formats: Format[Site] = Json.format[Site]
  implicit val writes: Writes[Site] = Json.writes[Site]
}

object Site {

  implicit object SiteWrites extends Writes[Site] {

    def writes(site: Site): JsValue = {
      JsObject(
        List(
          "type" -> JsString(site.siteType),
          "id" -> JsString(site._id.get.stringify),
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
    new Site(Some(BSONObjectID.generate), siteType.name, name, url, configuration)
  }
}