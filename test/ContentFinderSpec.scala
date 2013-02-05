import org.specs2.mutable._


import hfr._
import util.matching.Regex

class ContentFinderSpec extends Specification {

  "The ContentFinder class" should {

    "match indexes with no pageNumber defined" in {
      val topic: Topic = TopicRepository.getTopics().head
      val url = topic.url
      val contentFinder = new ContentFinder(topic, None)
      val currentPage: Int = contentFinder.getCurrentPage(url)
      val nbPages = contentFinder.getNbPages(url)
      currentPage must be equalTo (nbPages)

      val pageIndexes = contentFinder.getPageIndexes(url)
      pageIndexes._1 must equalTo(currentPage)
      pageIndexes._2 must equalTo(currentPage - 1)
      pageIndexes._3 must equalTo(-1)
    }

    "parition avatars and images" in {
      val imgs = List(
        "http://forum-images.hardware.fr/themes/dark/shit.gif",
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://i.minus.com/iKmav6Fr2vvBk.gif",
        "http://forum-images.hardware.fr/icones/redface.gif",
        "http://forum-images.hardware.fr/icones/redface.gif",
        "http://forum-images.hardware.fr/images/perso/mlc.gif",
        "http://i.imgur.com/XbuN4JK.gif?1",
        "http://forum-images.hardware.fr/images/perso/4/ticento.gif",
        "http://hfr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif",
        "http://hfr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif",
        "http://forum-images.hardware.fr/images/perso/ripthejacker.gif",
        "http://forum-images.hardware.fr/images/perso/ripthejacker.gif",
        "http://hfr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif"
      )
      val contentFinder: ContentFinder = new ContentFinder(TopicRepository.getTopics().head, None)
      val rearrangeImgs = contentFinder.rearrangeImages(imgs)

      val expected = List(
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://forum-images.hardware.fr/icones/redface.gif",
        "http://forum-images.hardware.fr/images/perso/mlc.gif",
        "http://forum-images.hardware.fr/images/perso/4/ticento.gif",
        "http://forum-images.hardware.fr/images/perso/ripthejacker.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://i.minus.com/iKmav6Fr2vvBk.gif",
        "http://i.imgur.com/XbuN4JK.gif?1",
        "http://hfr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif",
        "http://hfr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6")
      rearrangeImgs.size must be equalTo (expected.size)
      rearrangeImgs must be equalTo (expected)
    }

    "change pageNumber from url" in {
      val pageNumber = "100"
      val url = """_[0-9]+\.""".r.replaceAllIn("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_2244.htm", "_" + pageNumber + ".")
      url must equalTo("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_100.htm")
    }

  }

}