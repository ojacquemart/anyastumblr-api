package hfr

trait ConfigurationBuilder {

  def getCssSelectors(): CssSelectors
  def getNavigationOrder(): NavigationOrder
  def getPageResolverInfos(): PageResolverInfos
  def getImageRule(): Option[ImageRule]

  def get() = new Configuration(getCssSelectors(), getNavigationOrder(), getPageResolverInfos(), getImageRule())

}

object HfrConfiguration extends ConfigurationBuilder {

  def getNavigationOrder() = NavigationOrder.Descending

  def getPageResolverInfos() ={
    val pageNumberInfos = new ForumPageNumberInfos("tr.cBackHeader.fondForum2PagesHaut div.left a:last-child", """([0-9]+)\.htm""")
    val changeUrlPageInfos = new ChangeUrlPageInfos("""_[0-9]+\.""", "_%d.")
    new PageResolverInfos(Some(pageNumberInfos), changeUrlPageInfos)
  }

  def getImageRule() = {
    Some(new ImageRule("http://forum-images.hardware.fr/themes", List("http://forum-images.hardware.fr/images/perso", "http://forum-images.hardware.fr/icones")))
  }
  def getCssSelectors() = new CssSelectors("tr.message td.messCase2 img", None)

}

object JoiesDuCodeConfiguration extends ConfigurationBuilder {

  def getNavigationOrder(): NavigationOrder = NavigationOrder.Ascending
  def getPageResolverInfos() ={
    val changeUrlPageInfos = new ChangeUrlPageInfos("""\/page\/[0-9]+""", "/page/%d")
    new PageResolverInfos(None, changeUrlPageInfos)
  }
  def getImageRule() = None
  def getCssSelectors() = new CssSelectors(".post p img", Some(".post h3"))

}