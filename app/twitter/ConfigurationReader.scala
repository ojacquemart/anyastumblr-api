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

  def consumerKey = read(ConfigurationKeys.TwConsumerKey)
  def consumerSecret = read(ConfigurationKeys.TwConsumerSecret)
  def accessToken = read(ConfigurationKeys.TwAccessToken)
  def accessTokenSecret = read(ConfigurationKeys.TwAccessTokenSecret)

  def read(key: String): String = {
    configuration.getString(key).getOrElse {
      throw new IllegalArgumentException(s"No value found for $key")
    }
  }

}
