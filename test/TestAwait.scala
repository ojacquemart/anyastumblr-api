import java.util.concurrent.TimeUnit

import concurrent.duration.Duration
import concurrent.Await

object TestAwait {

  def result[T](awaitable : scala.concurrent.Awaitable[T]) : T = {
    Await.result(awaitable, defaultWaittime)
  }

  val defaultWaittime = Duration(5, TimeUnit.SECONDS)

}
