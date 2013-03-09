import concurrent.duration.Duration
import concurrent.{Await, Future}
import java.util.concurrent.TimeUnit

object TestHelpers {

  def option[T](futureTryOption: Future[Option[T]]) = {
    val tryOption = futureTryOption.value.get
    tryOption.get
  }

}
