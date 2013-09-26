package utils

import org.specs2.execute.AsResult
import org.specs2.mutable.Around

import play.api.Play.current
import play.api.Logger
import play.api.test.FakeApplication
import play.api.test.Helpers._

trait SimpleFakeApp extends Around with org.specs2.specification.Scope {

  def testAdditionalConfig(): Map[String, _] = Map()

  object FakeApp extends FakeApplication(
    additionalConfiguration = testAdditionalConfig
  ) {
  }

  def doAround(): Unit = {}

  def around[T: AsResult](t: => T) = running(FakeApp) {
    Logger.debug("Running test ==================================")
    doAround()

    // Run tests inside a fake application
    AsResult(t)
  }
}