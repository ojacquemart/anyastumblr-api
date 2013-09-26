package twitter.utils

import org.specs2.mutable._

import play.api.test.WithApplication

import twitter.models.{TwitterUser, Tweet}

object ConfigurationReaderSpec extends Specification {

  "The ConfigurationReader class" should {

    "read twitter oauth conf" in new WithApplication  {
      ConfigurationReader.consumerKey must not be empty
      ConfigurationReader.consumerSecret must not be empty
      ConfigurationReader.accessToken must not be empty
      ConfigurationReader.accessTokenSecret must not be empty
    }

  }

}
