package test.utils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestAwait {

  def result[T](awaitable : scala.concurrent.Awaitable[T]) : T = {
    Await.result(awaitable, defaultWaittime)
  }

  val defaultWaittime = Duration.create(20, "sec")

}
