package twitter

import scala.concurrent._
import ExecutionContext.Implicits.global
import java.net.URLEncoder

import play.api.Logger
import play.api.libs.oauth._
import play.api.libs.json._
import play.api.libs.ws.{WS, Response}

import ConfigurationReader._

class TweetFetcher(query: String, sinceId: String)  {

  /**
   * The consumer key.
   */
  val consumerKey = ConsumerKey(ConfigurationReader.consumerKey,ConfigurationReader.consumerSecret)

  /**
   * The access token.
   */
  val accessToken = RequestToken(ConfigurationReader.accessToken, ConfigurationReader.accessTokenSecret)

  /**
   * The Json Twitter API.
   */
  val TwitterV1JsonApi: String = "https://api.twitter.com/1.1/search/tweets.json"

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

    WS.url(TwitterV1JsonApi + parameters)
      .sign(OAuthCalculator(consumerKey, accessToken))
      .get()
  }

}