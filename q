[1mdiff --git a/app/twitter/Tweet.scala b/app/twitter/Tweet.scala[m
[1mindex 7a52a5c..9f75c93 100644[m
[1m--- a/app/twitter/Tweet.scala[m
[1m+++ b/app/twitter/Tweet.scala[m
[36m@@ -3,20 +3,46 @@[m [mpackage twitter[m
 import scala.concurrent._[m
 import ExecutionContext.Implicits.global[m
 [m
[31m-import play.api.libs.iteratee._[m
[31m-[m
[32m+[m[32mimport play.api.Play.current[m
 import play.api._[m
[31m-import play.api.libs._[m
[32m+[m[32mimport cache.Cache[m
 import play.api.libs.ws._[m
[32m+[m[32mimport play.api.libs.iteratee._[m
 [m
 import play.api.libs.json._[m
[31m-import play.api.libs.functional._[m
 import play.api.libs.functional.syntax._[m
 import play.api.libs.json.Reads._[m
[32m+[m
 import java.util.concurrent.TimeUnit[m
 [m
 case class Tweet(id: String, text: String, userName: String, avatar: String, source: String)[m
 [m
[32m+[m[32mobject TweetCache {[m
[32m+[m
[32m+[m[32m  val KeyMostRecentTweetId = "tweets.last_id"[m
[32m+[m
[32m+[m[32m  def setMostRecentTweetId(tweets: Seq[Tweet]) = {[m
[32m+[m[32m    if (!tweets.isEmpty) {[m
[32m+[m[32m      val mostRecentTweetId = tweets.head.id[m
[32m+[m[32m      Logger.debug(s"Put most tweet id in cache: $mostRecentTweetId")[m
[32m+[m[32m      // Put in cache last tweet id to stream since this id.[m
[32m+[m[32m      Cache.set(KeyMostRecentTweetId, mostRecentTweetId)[m
[32m+[m[32m    } else {[m
[32m+[m[32m      Logger.debug("No tweets")[m
[32m+[m[32m    }[m
[32m+[m[32m  }[m
[32m+[m
[32m+[m[32m  def getMostRecentTweetId() = {[m
[32m+[m[32m    val tweetId = Cache.getOrElse[String](KeyMostRecentTweetId) {[m
[32m+[m[32m      ""[m
[32m+[m[32m    }[m
[32m+[m[32m    Logger.debug(s"Get most recent tweet id: $tweetId")[m
[32m+[m
[32m+[m[32m    tweetId[m
[32m+[m[32m  }[m
[32m+[m
[32m+[m[32m}[m
[32m+[m
 object Tweet {[m
 [m
   implicit val writeTweetAsJson = Json.writes[Tweet][m
[36m@@ -25,10 +51,10 @@[m [mobject Tweet {[m
     case t => Json.toJson(t)[m
   }[m
 [m
[31m-  implicit val readTweet: Reads[Seq[Tweet]] =[m
[32m+[m[32m  implicit val readTweet: Reads[Seq[Tweet]] = {[m
     (__ \ "results").read([m
       seq([m
[31m-        (__ \ "id_str").read[String] and[m
[32m+[m[32m        (__ \ "id").read[String] and[m
           (__ \ "text").read[String] and[m
           (__ \ "from_user").read[String] and[m
           (__ \ "profile_image_url").read[String] and[m
[36m@@ -42,6 +68,7 @@[m [mobject Tweet {[m
         }[m
       )[m
     )[m
[32m+[m[32m  }[m
 [m
   /**[m
    * The Json Twitter API.[m
[36m@@ -55,27 +82,33 @@[m [mobject Tweet {[m
    * @param query the query.[m
    * @return[m
    */[m
[31m-  def fetch(query: String): Future[Seq[Tweet]] = {[m
[32m+[m[32m  def fetch(query: String, sinceTweetId: String = ""): Future[Seq[Tweet]] = {[m
     Logger.debug(s"Twitter query: $query")[m
 [m
     val futureTweets = WS.url(JsonApi).withQueryString([m
       "q" -> query,[m
       "include_entities" -> "false",[m
[31m-      "rpp" -> "40"[m
[32m+[m[32m      "rpp" -> "100"[m
     ).get().map(response => response.status match {[m
       case 200 => response.json.asOpt[Seq[Tweet]].getOrElse(Nil)[m
       case _ => Nil[m
     })[m
 [m
[32m+[m[32m//    futureTweets onSuccess {[m
[32m+[m[32m//      case tweets => Logger.debug(s"Tweets fetch: ${tweets.size}")[m
[32m+[m[32m//    }[m
[32m+[m
     futureTweets[m
   }[m
 [m
   def stream(query: String): Enumerator[JsValue] = {[m
[32m+[m
[32m+[m[32m    //fetch(query, TweetCache.getMostRecentTweetId())[m
     // Flatenize an Enumerator[Seq[Tweet]] as n Enumerator[Tweet][m
     val flatenize = Enumeratee.mapConcat[Seq[Tweet]](identity)[m
 [m
     // Schedule[m
[31m-    val schedule = Enumeratee.mapM[Tweet](t => play.api.libs.concurrent.Promise.timeout(t, 5, TimeUnit.SECONDS))[m
[32m+[m[32m    val schedule = Enumeratee.mapM[Tweet](t => play.api.libs.concurrent.Promise.timeout(t, 20, TimeUnit.SECONDS))[m
 [m
     // Create a stream of tweets from multiple twitter API calls[m
     val tweets = Enumerator.repeatM(fetch(query))[m
warning: LF will be replaced by CRLF in app/twitter/Tweet.scala.
The file will have its original line endings in your working directory.
