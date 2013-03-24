package model

import play.api.libs.json._
import play.api.Logger
import dao.SiteDao

case class CssSelector(cssQuery: String, htmlAttribute: String)
case class CssSelectors(images: CssSelector, text: Option[CssSelector])

case class ImageRule(exclude: String, startsWith: List[String])
case class ChangePageDescriptor(regex: String, replacement: String)
case class PageNumberDescriptor(cssSelector: String, regex: String)

case class PageResolver(pageNumberDescriptor: Option[PageNumberDescriptor], changePageDescriptor: ChangePageDescriptor)

case class Configuration(cssSelectors: CssSelectors, navigationOrder: NavigationOrder, pageResolver: PageResolver, imageRule: Option[ImageRule])
case class Site(id: String, name: String, url: String, configuration: Configuration)

object Site {

  def apply(name: String, url: String, configuration: Configuration) = {
    // Id in sha1...
    // TODO: use mongodb on another nosql collection to store sites and maybe more... like every images loaded...
    val md = java.security.MessageDigest.getInstance("SHA-1")
    val id = new sun.misc.BASE64Encoder().encode(md.digest((url + name).getBytes)).replace("/", "").replace("+", "")

    Logger.info(s"Site siteId=$id for $name")
    new Site(id, name, url, configuration)
  }
}

object SiteJSON {

  implicit object SiteJSONHandlers extends Format[Site] {

    def reads(json: JsValue): JsResult[Site] = {
      val id = (json \ "id").as[String]
      JsSuccess(SiteDao.findSite(id))
    }

    def writes(site: Site): JsValue = {
      JsObject(
        List("id" -> JsString(site.id),
          "name" -> JsString(site.name)
        ))
    }

  }

}

