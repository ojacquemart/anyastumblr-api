package hfr

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import org.joda.time.DateTime

case class Topic(id: String, name: String, url: String)

object Topic {

  def apply(name: String, url: String) = {
    // Id in sha1...
    // TODO: use mongodb on another nosql database to store topics and maybe more... like every images loaded...
    val md = java.security.MessageDigest.getInstance("SHA-1")
    val id = new sun.misc.BASE64Encoder().encode(md.digest((url + name).getBytes))
    new Topic(id, name, url)
  }
}

object TopicFormats {

  implicit object TopicFormat extends Format[Topic] {

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

