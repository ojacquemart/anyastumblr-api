import org.specs2.mutable._

import hfr._

class SiteConfigurationsSpec extends Specification {

  "The SiteConfigurations class" should {

    "build hfr configuration" in {
      val config = HfrConfiguration.get()
      config.navigationOrder must be equalTo (NavigationOrder.Descending)

      config.cssSelectors.images.cssQuery must be equalTo ("tr.message td.messCase2 img")
      config.cssSelectors.text must be equalTo (None)

      config.imageRule must not be equalTo(None)
      val imageRule: ImageRule = config.imageRule.get
      imageRule.exclude must be equalTo ("http://forum-images.hardware.fr/themes")
      imageRule.startsWith must be equalTo (List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones"))
    }

    "build joiesducode configuration" in {
      val config = JoiesDuCodeConfiguration.get()
      config.navigationOrder must be equalTo (NavigationOrder.Ascending)

      config.cssSelectors.images.cssQuery must be equalTo (".post .bodytype img")
      val textSelector: Option[CssSelector] = config.cssSelectors.text
      textSelector must not be equalTo (None)
      textSelector.get.cssQuery must be equalTo(".post h3 a")

      config.imageRule must be equalTo(None)
    }

  }

}