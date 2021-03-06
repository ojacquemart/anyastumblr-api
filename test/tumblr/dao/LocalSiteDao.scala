package tumblr.dao

import reactivemongo.bson.BSONObjectID

import tumblr.model._

object LocalSiteDao {

  private val hfrConfiguration: Configuration = HfrConfiguration.get()

  val LocalSites = List(
    get(SiteType.HFR, "Images étonnantes", "hfr", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm", 1, hfrConfiguration),
    get(SiteType.HFR, "Gifs: Femmes, Caca, Chutes&Co", "hfr", "http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm", 1, hfrConfiguration),
    get(SiteType.LESJOIES, "Joiesducode", "joiesducode", "http://lesjoiesducode.tumblr.com/page/1", 1, JoiesDuCodeConfiguration.get),
    get(SiteType.LESJOIES, "Joiesdusysadmin", "joisdusysadmin", "http://lesjoiesdusysadmin.tumblr.com/page/1", 1, JoiesDuSysadminConfiguration.get),
    get(SiteType.LESJOIES, "Joiesdutest", "joiesdutest", "http://lesjoiesdutest.tumblr.com/page/1", 1, JoiesDuTestConfiguration.get),
    get(SiteType.LESJOIES, "Joiesduscrum", "joiesduscrum", "http://lesjoiesduscrum.tumblr.com/page/1", 1, JoiesDuScrumConfiguration.get),
    get(SiteType.MISC, "ActressesWithoutTeeth", "actresseswithoutteeth", "http://actresseswithoutteeth.net/page/1", 1, ActressesWithoutTeeth.get),
    get(SiteType.MISC, "ChersVoisins", "chersvoisins", "http://chersvoisins.tumblr.com/page/1", 1, ChersVoisinsConfiguration.get),
    get(SiteType.MISC, "CommitStrip", "commitstrip", "http://www.commitstrip.com/page/1", 1, CommitStripConfiguration.get),
    get(SiteType.MISC, "DataAnxiety", "dataanxiety", "http://dataanxiety.tumblr.com/page/1", 1, DataAnxietyConfiguration.get),
    get(SiteType.MISC, "Failbog.fr", "failbogfr", "http://failblog.fr/fail/page-1.html", 1, FailBlogFrConfiguration.get),
    get(SiteType.MISC, "N'oubliez jamais la capote", "noubliez-jamais-la-capote", "http://noubliezjamaislacapote.tumblr.com/page/1/", 1, DontForgetCondomConfiguration.get),
    get(SiteType.MISC, "Sportballsreplacedwithcats", "sportballsreplacedwithcats", "http://sportballsreplacedwithcats.tumblr.com/page/1", 1, SportBallsReplacedWithCatsConfiguration.get)
  )

  def get(siteType: SiteType, name: String, slug: String, url: String, order: Int, configuration: Configuration) = {
    new Site(Some(BSONObjectID.generate), siteType, name, slug, url, -1, true, configuration)
  }

}

// Local Sites Configurations

trait ConfigurationBuilder {
  val regexLastPage = """([0-9]+)$"""

  def getCssSelectors(): CssSelectors
  def isLastPageByCss(): Boolean = true
  def getNavigationAscending(): Boolean
  def getImageRule(): Option[ImageRule]

  def getPageNumberDescriptor(): Option[PageNumberDescriptor]
  def getChangePageDescriptor(): ChangePageDescriptor

  def getPageResolver() = new PageResolver(getPageNumberDescriptor(), getChangePageDescriptor())

  def get() = new Configuration(getCssSelectors(), isLastPageByCss(), getNavigationAscending(), getPageResolver(), getImageRule())
}

object HfrConfiguration extends ConfigurationBuilder {
  def getCssSelectors() = new CssSelectors(new CssSelector("tr.message td.messCase2 img", Some("src")), None)

  def getNavigationAscending() = false

  def getImageRule() = {
    Some(new ImageRule(exclude = "http://forum-images.hardware.fr/themes", startsWith = List(SimpleValue("http://forum-images.hardware.fr/images/perso"), SimpleValue("http://forum-images.hardware.fr/icones"))))
  }

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("tr.cBackHeader.fondForum2PagesHaut div.left a:last-child", Some("href")), """([0-9]+)\.htm"""))
  def getChangePageDescriptor() = new ChangePageDescriptor("""_[0-9]+\.""", "_%d.")
}

trait TumblrConfiguraiton extends ConfigurationBuilder {
  def getCssSelectors() = new CssSelectors(new CssSelector(".post .bodytype img", Some("src")), Some(new CssSelector(".post h3 a", Some("href"))))

  def getNavigationAscending() = true

  def getImageRule() = None

  def getChangePageDescriptor() = new ChangePageDescriptor("""\/page\/[0-9]+""", "/page/%d")
}

object JoiesDuCodeConfiguration extends TumblrConfiguraiton {
  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".footer i"), regexLastPage))
}

object JoiesDuSysadminConfiguration extends TumblrConfiguraiton {
  def getPageNumberDescriptor() = Some( new PageNumberDescriptor(new CssSelector(".pagination"), regexLastPage))
}

object JoiesDuScrumConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post p img", Some("src")), Some(new CssSelector(".post a h3", Some("href"))))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("#prev-next"), regexLastPage))
}

object JoiesDuTestConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post p img", Some("src")), Some(new CssSelector(".post h3 a", Some("href"))))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".count"), regexLastPage))
}

object ChersVoisinsConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("#page-location"), regexLastPage))
}


object CommitStripConfiguration extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".entry-content img", Some("src")), Some(new CssSelector(".post h1 a")))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), regexLastPage))
}

object DataAnxietyConfiguration extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".post-content img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), regexLastPage))
}

object SportBallsReplacedWithCatsConfiguration extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".photo_post img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), regexLastPage))
}

object DontForgetCondomConfiguration extends TumblrConfiguraiton {
  override def getCssSelectors() = new CssSelectors(new CssSelector(".post img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".pagination .count"), regexLastPage))
}

object FailBlogFrConfiguration extends ConfigurationBuilder {

  def getNavigationAscending() = true

  def getImageRule() = None

  def getChangePageDescriptor() = new ChangePageDescriptor("""/page-[0-9]+.html""", "/page-%d.html")

  override def getCssSelectors() = new CssSelectors(new CssSelector(".contenu a img", Some("src")), Some(new CssSelector(".contenu h1 a", None)))

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector(".page p > a:last-child"), regexLastPage))
}

object ActressesWithoutTeeth extends TumblrConfiguraiton {
  override def isLastPageByCss(): Boolean = false

  override def getCssSelectors() = new CssSelectors(new CssSelector(".content .photo img", Some("src")), None)

  def getPageNumberDescriptor() = Some(new PageNumberDescriptor(new CssSelector("a"), """([0-9]+)$"""))
}