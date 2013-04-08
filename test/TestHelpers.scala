import concurrent.Future

object TestHelpers {

  def option[T](futureTryOption: Future[Option[T]]) = {
    val tryOption = futureTryOption.value.get
    tryOption.get
  }

  def seq[T](futureTryOption: Future[Seq[T]]) = {
    val tryOption = futureTryOption.value.get
    tryOption.get
  }

}
