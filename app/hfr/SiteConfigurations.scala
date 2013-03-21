package hfr

trait ConfigurationBuilder {
  def getCssSelectors(): CssSelectors
  def getNavigationOrder(): NavigationOrder
  def getPageResolver(): PageResolver
  def getImageRule(): Option[ImageRule]

  def get() = new Configuration(getCssSelectors(), getNavigationOrder(), getPageResolver(), getImageRule())
}

object HfrConfiguration extends ConfigurationBuilder {
  def getNavigationOrder() = NavigationOrder.Descending

  def getPageResolver() ={
    val pageNumberDescriptor = new PageNumberDescriptor("tr.cBackHeader.fondForum2PagesHaut div.left a:last-child", """([0-9]+)\.htm""")
    val changePageDescriptor = new ChangePageDescriptor("""_[0-9]+\.""", "_%d.")
    new PageResolver(Some(pageNumberDescriptor), changePageDescriptor)
  }

  def getImageRule() = {
    Some(new ImageRule(exclude = "http://forum-images.hardware.fr/themes", startsWith = List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")))
  }
  def getCssSelectors() = new CssSelectors(new CssSelector("tr.message td.messCase2 img", "src"), None)
}

object JoiesDuCodeConfiguration extends ConfigurationBuilder {
  def getNavigationOrder(): NavigationOrder = NavigationOrder.Ascending
  def getPageResolver() ={
    val changeUrlPageInfos = new ChangePageDescriptor("""\/page\/[0-9]+""", "/page/%d")
    new PageResolver(None, changeUrlPageInfos)
  }
  def getImageRule() = None
  def getCssSelectors() = new CssSelectors( new CssSelector(".post .bodytype img", "src"), Some(new CssSelector(".post h3 a", "href")))
}