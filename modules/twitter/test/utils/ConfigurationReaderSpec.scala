package utils

import org.specs2.mutable._

import models.{TwitterUser, Tweet}

object ConfigurationReaderSpec extends Specification {

  "The ConfigurationReader class" should {

    "read twitter oauth conf" in new SimpleFakeApp {
      ConfigurationReader.consumerKey must not be empty
      ConfigurationReader.consumerSecret must not be empty
      ConfigurationReader.accessToken must not be empty
      ConfigurationReader.accessTokenSecret must not be empty
    }

  }

}
