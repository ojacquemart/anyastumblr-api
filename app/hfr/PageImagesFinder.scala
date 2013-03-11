package hfr

import jsoup.DocumentWrapper

case class PageImagesFinder(url: String, configuration: Configuration) {

  val ImgSrcAttribute = "src"

  def find() = {
    val imagesSelector = configuration.cssSelectors.imagesSelector
    val images: List[String] = new DocumentWrapper(url).listElements(imagesSelector, ImgSrcAttribute)
    rearrangeImages(images)
  }

  def rearrangeImages(images: List[String]): (List[String], List[String]) = {
    val distinct = images.distinct

    val imageRule = configuration.imageRule
    imageRule match {
      case None => (List(), distinct)
      case Some(rule: ImageRule) => {
        val rearrange = distinct
          .filterNot(_.startsWith(rule.exclude))
          .partition(i => rule.firstsStartsWith.exists(i.startsWith))

        (rearrange._1, rearrange._2.reverse)
      }
    }
  }

}