package twitter

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Enumerator}

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

import java.util.concurrent.TimeUnit

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

  def fetch(query: String, sinceId: String = ""): Future[Seq[Tweet]] = {
    new TweetFetcher(query, sinceId).fetch()
  }

  def count(query: String, sinceId: String = ""): Future[Int] = {
    new TweetFetcher(query, sinceId).count()
  }

  def streamCountRecents(query: String): Enumerator[String] = {
    val countRecents = Enumerator.repeatM[Int]({
      count(query, TweetCacheHandler.get(query))
    })

    val schedule = Enumeratee.mapM[Int](t => play.api.libs.concurrent.Promise.timeout(t, 10, TimeUnit.SECONDS))

    val countRecentsAsJson: Enumeratee[Int, JsValue] = Enumeratee.map {
      case t => Json.obj("recents" -> t)
    }

    countRecents &> schedule &> countRecentsAsJson &> EventSource()
  }
}