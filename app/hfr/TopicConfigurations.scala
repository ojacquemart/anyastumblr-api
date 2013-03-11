package hfr

trait ConfigurationBuilder {

  def getNavigationOrder(): NavigationOrder
  def getCssSelectors(): CssSelectors
  def getImageRule(): Option[ImageRule]

  def get() = new Configuration(getNavigationOrder(), getImageRule(), getCssSelectors())

}

object HfrConfiguration extends ConfigurationBuilder {

  def getNavigationOrder() = NavigationOrder.Descending
  def getImageRule() = {
    Some(new ImageRule("http://forum-images.hardware.fr/themes", List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")))
  }
  def getCssSelectors() = new CssSelectors("tr.message td.messCase2 img", None)

}

object JoiesDuCodeConfiguration extends ConfigurationBuilder {

  def getNavigationOrder(): NavigationOrder = NavigationOrder.Ascending
  def getImageRule() = None
  def getCssSelectors() = new CssSelectors(".post .c1 img", Some(".post h3"))

}