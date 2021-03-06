package tumblr.model

import play.api.libs.functional.syntax._
import play.api.libs.json._

import play.modules.reactivemongo.json.BSONFormats._
import reactivemongo.bson._

import tumblr.model.SiteType.writes
import tumblr.model.SiteType.formats

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
                         navigationAscending: Boolean,
                         pageResolver: PageResolver,
                         imageRule: Option[ImageRule])

case class Site(_id: Option[BSONObjectID],
                siteType: SiteType,
                name: String,
                slug: String,
                url: String,
                ordinal: Int,
                enabled: Boolean,
                configuration: Configuration)
  extends GenericMongoModel
    with Slugifiable
    with Sortable
    with Enabled

case class SitesByType(siteType: SiteType, sites: List[Site])

case class SitesInfo(sitesByType: List[SitesByType], sites: List[Site])

object SitesByType {

  implicit object SiteSimpleWrites extends Writes[Site] {

    def writes(site: Site): JsValue = {
      Json.obj(
        "type" -> site.siteType.name,
        "id" -> site.slug,
        "name" -> site.name,
        "favicon" -> s"http://www.google.com/s2/favicons?domain=${site.url}"
      )
    }

  }

  implicit object SitesByTypeSimpleWrites extends Writes[SitesByType] {

    def writes(sitesBySiteType: SitesByType): JsValue = {
      Json.obj(
        "type" -> sitesBySiteType.siteType.name,
        "sites" -> sitesBySiteType.sites
      )
    }

  }

  implicit val sitesInfoWrites = Json.writes[SitesInfo]

}

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

  implicit val format: Format[Site] = Json.format[Site]
  implicit val writes: Writes[Site] = Json.writes[Site]

}
