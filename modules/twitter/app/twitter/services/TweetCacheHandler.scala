package twitter.services

import play.api.Play.current
import play.api.cache.Cache

import play.api.Logger

import twitter.models.Tweet

/**
 * Handler to cache the last since_id from twitter.
 */
object TweetCacheHandler {

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