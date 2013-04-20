package twitter

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.Play.current
import play.api._
import cache.Cache
import play.api.libs.ws._
import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Enumerator}

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

import java.util.concurrent.TimeUnit

case class Tweet(id: String, text: String, userName: String, avatar: String, source: String)

/**
 * Companion to cache the last since_id from twitter.
 */
object TweetSinceIdCache {

  def PrefixCacheKey = "tweets.last_id"

  /**
   * Generate key cache by query.
   *
   * @param query the query.
   * @return the key cache.
   */
  def getCacheKey(query: String) = PrefixCacheKey + query

  def put(query: String, tweets: Seq[Tweet]) = {
    if (!tweets.isEmpty) {
      // since_id will be the first tweet found.
      val sinceId = tweets.head.id
      Logger.debug(s"Put most recent tweet id to cache: $sinceId")

      Cache.set(getCacheKey(query), sinceId)
    }
  }

  def get(query: String) = {
    val sinceId = Cache.getOrElse[String](getCacheKey(query))("")
    Logger.debug(s"Get most recent tweet id from cache: $sinceId")

    sinceId
  }

}

class TweetFetcher(query: String, sinceId: String) {

  /**
   * The Json Twitter API.
   * TODO: see to use 1.1 version with oauth.
   */
  val TwitterV1JsonApi: String = "http://search.twitter.com/search.json"

  val responsePerPage = if (sinceId.isEmpty) "" else "100"

  def fetch(): Future[Seq[Tweet]] = {
     val futureTweets = fetchResponse().map(response => response.status match {
       case 200 => {
         val tweets = response.json.asOpt[Seq[Tweet]].getOrElse(Nil)
         Logger.debug(s"Tweets fetched: ${tweets.size}")
         TweetSinceIdCache.put(query, tweets)
         tweets
       }
       case _ => Nil
     })


    futureTweets
  }

  def count(): Future[Int] = {
     val futureCountTweets = fetchResponse().map(response => response.status match {
       case 200 => {
         val results = (response.json \ "results").as[List[JsObject]]
         val count = results.size
         Logger.debug(s"Tweets count: $count")
         count
       }
       case _ => 0
     })

    futureCountTweets
  }

  def fetchResponse(): Future[Response] = {
    Logger.debug(s"Fetch tweets for $query with rpp $responsePerPage")

    WS.url(TwitterV1JsonApi).withQueryString(
      "q" -> query,
      "include_entities" -> "false",
      "since_id" -> sinceId,
      "result_type" -> "recent",
      "rpp" -> responsePerPage
    ).get()
  }
}

object Tweet {

  implicit val writeTweetAsJson = Json.writes[Tweet]

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

  def fetch(query: String, sinceId: String = ""): Future[Seq[Tweet]] = {
    new TweetFetcher(query, sinceId).fetch()
  }

  def count(query: String, sinceId: String = ""): Future[Int] = {
    new TweetFetcher(query, sinceId).count()
  }

  def streamCountRecents(query: String): Enumerator[String] = {
    val countRecents = Enumerator.repeatM[Int]({
      count(query, TweetSinceIdCache.get(query))
    })

    val schedule = Enumeratee.mapM[Int](t => play.api.libs.concurrent.Promise.timeout(t, 10, TimeUnit.SECONDS))

    val countRecentsAsJson: Enumeratee[Int, JsValue] = Enumeratee.map {
      case t => Json.obj("recents" -> t)
    }

    countRecents &> schedule &> countRecentsAsJson &> EventSource()
  }
}