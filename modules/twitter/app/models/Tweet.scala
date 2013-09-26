package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

case class TwitterUser(name: String, profile_image_url: String)

case class Tweet(id: String, text: String, source: String, user: TwitterUser)

object Tweet {

  implicit val twitterUserWrites = Json.writes[TwitterUser]
  implicit val writeTweetAsJson = Json.writes[Tweet]

  implicit val twitterUserReads = Json.format[TwitterUser]
  implicit val readTweet: Reads[Seq[Tweet]] =
    (__ \ "statuses").read(
      seq(
        (__ \ "id_str").read[String] and
          (__ \ "text").read[String] and
          (__ \ "source").read[String] and
          (__ \ "user").read[TwitterUser]
          tupled
      )
        .map(
        _.collect {
          case (id, text, source, user) => {
            Tweet(id, text, source, user)
          }
        }
      )
    )

}