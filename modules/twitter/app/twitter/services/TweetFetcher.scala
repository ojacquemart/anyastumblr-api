package twitter.services

import java.net.URLEncoder
import java.util.concurrent.TimeUnit

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Enumerator}
import play.api.libs.oauth._
import play.api.libs.json._
import play.api.libs.ws.{WS, Response}

import play.api.Logger

import twitter.models.Tweet
import twitter.utils.ConfigurationReader

class TweetFetcher(query: String, sinceId: String)  {

  /**
   * The consumer key.
   */
  val consumerKey = ConsumerKey(ConfigurationReader.consumerKey, ConfigurationReader.consumerSecret)

  /**
   * The access token.
   */
  val accessToken = RequestToken(ConfigurationReader.accessToken, ConfigurationReader.accessTokenSecret)

  /**
   * The Json Twitter API.
   */
  val TwitterApi: String = "https://api.twitter.com/1.1/search/tweets.json"

  val responsePerPage = if (sinceId.isEmpty) "" else "100"


  val responseToSeqTweets = (jsValue: JsValue) => {
    val tweets = jsValue.asOpt[Seq[Tweet]].getOrElse(Nil)
    Logger.debug(s"Tweets fetched: ${tweets.size}")
    TweetCacheHandler.put(query, tweets)
    tweets
  }

  val responseToCount = (jsValue: JsValue) => {
    val results = (jsValue \ "statuses").as[List[JsObject]]
    val count = results.size
    Logger.debug(s"Tweets count: $count")
    count
  }

  def fetch(): Future[Seq[Tweet]] = getResponse[Seq[Tweet]](responseToSeqTweets, Nil)

  def count(): Future[Int] = getResponse[Int](responseToCount, 0)

  def getResponse[T](mapJsValue: JsValue => T, valueOnFail: T): Future[T] = {
    val futureResults: Future[T] = fetchResponse().map(response => {
      response.status match {
        case 200 => mapJsValue(response.json)
        case _ => {
          Logger.debug(s"Fail with ${response.status} ${response.statusText}")
          valueOnFail
        }
      }
    })

    futureResults
  }

  def fetchResponse(): Future[Response] = {
    Logger.debug(s"Fetch tweets for $query with rpp $responsePerPage")

    val parameters = Map(
      "q"                 -> URLEncoder.encode(query, "UTF-8"),
      "include_entities"  -> "false",
      "since_id"          -> sinceId,
      "result_type"       -> "recent",
      "rpp"               -> responsePerPage)
      .map(param => s"${param._1}=${param._2}")
      .mkString("?", "&", "")
    Logger.debug(s"Parameters $parameters")

    WS.url(TwitterApi + parameters)
      .sign(OAuthCalculator(consumerKey, accessToken))
      .get()
  }

}

object TweetFetcher {

  def fetch(query: String, sinceId: String = ""): Future[Seq[Tweet]] = {
    new TweetFetcher(query, sinceId).fetch()
  }

  def count(query: String, sinceId: String = ""): Future[Int] = {
    new TweetFetcher(query, sinceId).count()
  }

  def chunckCountRecents(query: String): Enumerator[String] = {
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