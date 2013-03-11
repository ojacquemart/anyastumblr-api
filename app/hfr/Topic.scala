package hfr

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import org.joda.time.DateTime

case class CssSelectors(imagesSelector: String, textSelector: Option[String])
case class ImageRule(exclude: String, firstsStartsWith: List[String])
case class Configuration(navOrder: NavigationOrder, imageRule: Option[ImageRule], cssSelectors: CssSelectors)
case class Topic(id: String, name: String, url: String, configuration: Configuration)

object Topic {

  def apply(name: String, url: String, configuration: Configuration) = {
    // Id in sha1...
    // TODO: use mongodb on another nosql collection to store topics and maybe more... like every images loaded...
    val md = java.security.MessageDigest.getInstance("SHA-1")
    val id = new sun.misc.BASE64Encoder().encode(md.digest((url + name).getBytes)).replace("/", "")
    new Topic(id, name, url, configuration)
  }
}

object TopicJSON {

  implicit object TopicJSONHandlers extends Format[Topic] {

    def reads(json: JsValue): JsResult[Topic] = {
      val id = (json \ "id").as[String]
      JsSuccess(TopicRepository.findTopic(id))
    }

    def writes(topic: Topic): JsValue = {
      JsObject(
        List("id" -> JsString(topic.id),
          "name" -> JsString(topic.name)
        ))
    }

  }

}

