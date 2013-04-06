package model

trait ConfigurationBuilder {
  def getCssSelectors(): CssSelectors
  def getNavigationOrder(): NavigationOrder
  def getImageRule(): Option[ImageRule]

  def getPageNumberDescriptor(): Option[PageNumberDescriptor]
  def getChangePageDescriptor(): ChangePageDescriptor

  def getPageResolver() = new PageResolver(getPageNumberDescriptor(), getChangePageDescriptor())

  def get() = new Configuration(getCssSelectors(), getNavigationOrder(), getPageResolver(), getImageRule())
}

object HfrConfiguration extends ConfigurationBuilder {
  def getCssSelectors() = new CssSelectors(new CssSelector("tr.message td.messCase2 img", Some("src")), None)

  def getNavigationOrder() = NavigationOrder.Descending

  def getImageRule() = {
    Some(new ImageRule(exclude = "http://forum-images.hardware.fr/themes", startsWith = List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")))
  }

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("tr.cBackHeader.fondForum2PagesHaut div.left a:last-child", Some("href")), """([0-9]+)\.htm"""))
  def getChangePageDescriptor() = new ChangePageDescriptor("""_[0-9]+\.""", "_%d.")
}

trait TumblrConfiguraiton extends ConfigurationBuilder {
  def getCssSelectors() = new CssSelectors(new CssSelector(".post .bodytype img", Some("src")), Some(new CssSelector(".post h3 a", Some("href"))))

  def getNavigationOrder(): NavigationOrder = NavigationOrder.Ascending

  def getImageRule() = None

  def getChangePageDescriptor() = new ChangePageDescriptor("""\/page\/[0-9]+""", "/page/%d")
}

object JoiesDuCodeConfiguration extends TumblrConfiguraiton {
  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".pagination strong"), """([0-9]+)$"""))
}

object JoiesDuSysadminConfiguration extends TumblrConfiguraiton {
  def getPageNumberDescriptor() = Some( new PageNumberDescriptor(new CssSelector(".pagination"), """([0-9]+)$"""))
}

object JoiesDuScrumConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post p img", Some("src")), Some(new CssSelector(".post a h3", Some("href"))))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("#prev-next"), """([0-9]+)$"""))
}

object JoiesDuTestConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post p img", Some("src")), Some(new CssSelector(".post h3 a", Some("href"))))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".page-number"), """([0-9]+)$"""))
}

object DontForgetCondomConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".pagination .count"), """([0-9]+)$"""))
}

object FailBlogFrConfiguration extends ConfigurationBuilder {

  def getNavigationOrder(): NavigationOrder = NavigationOrder.Ascending

  def getImageRule() = None

  def getChangePageDescriptor() = new ChangePageDescriptor("""/page-[0-9]+.html""", "/page-%d.html")

  override def getCssSelectors() = new CssSelectors(new CssSelector(".contenu a img", Some("src")), Some(new CssSelector(".contenu h1 a", None)))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".page p > a:last-child"), """([0-9]+)$"""))
}