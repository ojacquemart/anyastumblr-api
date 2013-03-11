import org.specs2.mutable._


import hfr._

class TopicConfigurationSpec extends Specification {

  "The TopicConfiguration class" should {

    "build hfr configuration" in {
      val config = HfrConfiguration.get()
      config.navOrder must be equalTo (NavigationOrder.Descending)

      config.cssSelectors.imagesSelector must be equalTo ("tr.message td.messCase2 img")
      config.cssSelectors.textSelector must be equalTo (None)

      config.imageRule must not be equalTo(None)
      val imageRule: ImageRule = config.imageRule.get
      imageRule.exclude must be equalTo ("http://forum-images.hardware.fr/themes")
      imageRule.firstsStartsWith must be equalTo (List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones"))
    }

    "build joiesducode configuration" in {
      val config = JoiesDuCodeConfiguration.get()
      config.navOrder must be equalTo (NavigationOrder.Ascending)

      config.cssSelectors.imagesSelector must be equalTo (".post .c1 img")
      val textSelector: Option[String] = config.cssSelectors.textSelector
      textSelector must not be equalTo (None)
      textSelector.get must be equalTo(".post h3")

      config.imageRule must be equalTo(None)
    }

  }

}