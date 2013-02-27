package hfr

import jsoup.DocumentWrapper

case class PageImagesFinder(url: String) {

  val HfrImagesCssSelector = "tr.message td.messCase2 img"
  val ImgSrcAttribute = "src"

  val HfrFilters = List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")
  val HfrThemes = "http://forum-images.hardware.fr/themes"

  def find() = {
    val images: List[String] = new DocumentWrapper(url).listElements(HfrImagesCssSelector, ImgSrcAttribute)
    rearrangeImages(images)
  }

  def rearrangeImages(images: List[String]): (List[String], List[String]) = {
    val rearrange = images
      .distinct
      .filterNot(_.startsWith(HfrThemes))
      .partition(i => HfrFilters.exists(i.startsWith))
    (rearrange._1, rearrange._2.reverse)
  }

}