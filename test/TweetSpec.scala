import org.specs2.mutable._

import concurrent._
import ExecutionContext.Implicits.global

import concurrent.Await
import concurrent.duration.Duration
import java.util.concurrent.TimeUnit

import twitter._

import TestHelpers._

class TweetSpec extends Specification {

  "The Tweet class" should {

    "fetch tweets" in new FakeApp {
      val futureTweets = Tweet.fetch("#java, #scala")
      Await.ready(futureTweets, Duration(5, TimeUnit.SECONDS))

      val tweets = seq[Tweet](futureTweets)
      tweets must not be equalTo(Nil)
      tweets.size must be >(1)
    }
  }

  "The TweetSinceIdCache class" should {

    "generate different cache keys according to query parameter" in new FakeApp {
      val javaCacheKey = TweetSinceIdCache.getCacheKey("java")
      val scalaCacheKey = TweetSinceIdCache.getCacheKey("scala")
      javaCacheKey must not be equalTo(scalaCacheKey)
    }

    "put in cache the first tweet id, used to query since_id" in new FakeApp {
      val tweet1 = new Tweet("1", "foo", "bar", "avatar", "source")
      val tweet2 = new Tweet("2", "foo", "bar", "avatar", "source")

      val tweets = List(tweet2, tweet1)
      TweetSinceIdCache.put("java", tweets)
      val sinceId = TweetSinceIdCache.get("java")
      sinceId must be equalTo(tweet2.id)
    }

  }

}
