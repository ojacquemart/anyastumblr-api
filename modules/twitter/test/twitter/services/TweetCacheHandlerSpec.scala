package twitter.services

import org.specs2.mutable._

import play.api.test.WithApplication

import twitter.models.{TwitterUser, Tweet}

object TweetCacheHandlerSpec extends Specification {

  "The TweetCacheHandler class" should {

    "generate different cache keys according to query parameter" in new WithApplication {
      val javaCacheKey = TweetCacheHandler.getCacheKey("java")
      val scalaCacheKey = TweetCacheHandler.getCacheKey("scala")
      javaCacheKey must not be equalTo(scalaCacheKey)
    }

    "put in cache the first tweet id, used to query since_id" in new WithApplication {
      val tweet1 = new Tweet("1", "foo", "bar", TwitterUser("avatar", "source"))
      val tweet2 = new Tweet("2", "foo", "bar", TwitterUser("avatar", "source"))

      val tweets = List(tweet2, tweet1)
      TweetCacheHandler.put("java", tweets)
      val sinceId = TweetCacheHandler.get("java")
      sinceId must be equalTo(tweet2.id)
    }

  }

}
