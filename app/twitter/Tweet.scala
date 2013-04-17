package twitter

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.libs.iteratee._

import play.api._
import play.api.libs._
import play.api.libs.ws._

import play.api.libs.json._
import play.api.libs.functional._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import java.util.concurrent.TimeUnit

case class Tweet(id: String, text: String, userName: String, avatar: String, source: String)

object Tweet {

  implicit val writeTweetAsJson = Json.writes[Tweet]

  val asJson: Enumeratee[Tweet, JsValue] = Enumeratee.map {
    case t => Json.toJson(t)
  }

  implicit val readTweet: Reads[Seq[Tweet]] =
    (__ \ "results").read(
      seq(
        (__ \ "id_str").read[String] and
          (__ \ "text").read[String] and
          (__ \ "from_user").read[String] and
          (__ \ "profile_image_url").read[String] and
          (__ \ "source").read[String]

          tupled
      )
        .map(
        _.collect {
          case (id, text, userName, avatar, source) => Tweet(id, text, userName, avatar, source)
        }
      )
    )

  /**
   * The Json Twitter API.
   * TODO: see to use 1.1 version with oauth.
   */
  val JsonApi: String = "http://search.twitter.com/search.json"

  /**
   * Retrieve tweets from a given query.
   *
   * @param query the query.
   * @return
   */
  def fetch(query: String): Future[Seq[Tweet]] = {
    Logger.debug(s"Twitter query: $query")

    val futureTweets = WS.url(JsonApi).withQueryString(
      "q" -> query,
      "include_entities" -> "false",
      "rpp" -> "40"
    ).get().map(response => response.status match {
      case 200 => response.json.asOpt[Seq[Tweet]].getOrElse(Nil)
      case _ => Nil
    })

    futureTweets
  }

  def stream(query: String): Enumerator[JsValue] = {
    // Flatenize an Enumerator[Seq[Tweet]] as n Enumerator[Tweet]
    val flatenize = Enumeratee.mapConcat[Seq[Tweet]](identity)

    // Schedule
    val schedule = Enumeratee.mapM[Tweet](t => play.api.libs.concurrent.Promise.timeout(t, 5, TimeUnit.SECONDS))

    // Create a stream of tweets from multiple twitter API calls
    val tweets = Enumerator.repeatM(fetch(query))

    // Compose the final stream
    tweets &> flatenize &> schedule &> asJson
  }
}