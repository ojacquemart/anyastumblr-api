package twitter

import play.api.Play.current
import play.api.Play.configuration

object ConfigurationKeys {
  val TwConsumerKey         = "twitter.consumer.key"
  val TwConsumerSecret      = "twitter.consumer.secret"
  val TwAccessToken         = "twitter.access.token"
  val TwAccessTokenSecret   = "twitter.access.tokensecret"
}

object ConfigurationReader {

  val consumerKey = read(ConfigurationKeys.TwConsumerKey)
  val consumerSecret = read(ConfigurationKeys.TwConsumerSecret)
  val accessToken = read(ConfigurationKeys.TwAccessToken)
  val accessTokenSecret = read(ConfigurationKeys.TwAccessTokenSecret)

  def read(key: String): String = {
    configuration.getString(key).getOrElse {
      throw new IllegalArgumentException(s"No value found for $key")
    }
  }

}
