package hfr

import jsoup.DocumentWrapper

case class PageImagesFinder(url: String, configuration: Configuration) {

  val ImgSrcAttribute = "src"

  def find() = {
    val imagesSelector = configuration.cssSelectors.imagesSelector
    val images: List[String] = new DocumentWrapper(url).listElements(imagesSelector, ImgSrcAttribute)
    rearrangeImages(images)
  }

  def rearrangeImages(images: List[String]): (List[Image], List[Image]) = {
    val distinct = images.distinct

    val imageRule = configuration.imageRule
    imageRule match {
      case None => (List(), map(distinct))
      case Some(rule: ImageRule) => {
        val rearrange = distinct
          .filterNot(_.startsWith(rule.exclude))
          .partition(i => rule.firstsStartsWith.exists(i.startsWith))

        (map(rearrange._1), map(rearrange._2.reverse))
      }
    }
  }

  def map(strings: List[String]) = strings.map(s => new Image(s, ""))

}