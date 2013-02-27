package hfr

import play.api.libs.json._
import play.api.Logger

object TopicRepository {

  import TopicFormats._

  def getTopics() = {
    List(
      Topic("Images étonnantes", "http://forum.hardware.fr/hfr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm"),
      Topic("Gifs: Femmes, Caca, Chutes&Co", "http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm"))
  }

  def getFirstTopicUrl() = getTopics().head.url

  def getTopicsAsJson() = Json.toJson(getTopics())

  def getDefaultTopic() = getTopics().head

  def findTopic(id: String): Topic = {
    val topic = getTopics().find(_.id == id).getOrElse(getDefaultTopic())
    Logger.info("Get topic %s from id %s".format(topic.name, id))
    topic
  }

  def findTopicUrl(id: String) = findTopic(id).url

}
