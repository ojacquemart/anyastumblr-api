package utils

import play.api.Logger

import com.typesafe.config.ConfigFactory

object ConfigurationKeys {
  val TwConfigurationFile   = "twitter.oauth.conf"
  val TwConsumerKey         = "twitter.consumer.key"
  val TwConsumerSecret      = "twitter.consumer.secret"
  val TwAccessToken         = "twitter.access.token"
  val TwAccessTokenSecret   = "twitter.access.tokensecret"
}

object ConfigurationReader {

  val configuration =  ConfigFactory.load(ConfigurationKeys.TwConfigurationFile)

  val consumerKey = read(ConfigurationKeys.TwConsumerKey)
  val consumerSecret = read(ConfigurationKeys.TwConsumerSecret)
  val accessToken = read(ConfigurationKeys.TwAccessToken)
  val accessTokenSecret = read(ConfigurationKeys.TwAccessTokenSecret)

  def read(key: String): String = {
    val value = configuration.getString(key)
    Logger.debug(s"Read [$key=$value]")
    value
  }

}
