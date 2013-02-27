import org.specs2.mutable._


import hfr._
import util.matching.Regex

class ContentFinderSpec extends Specification {

  "The ContentFinder class" should {

    "resolve urlAndPageNumber from firstPage without pageNumber" in {
      val topicUrl: String = TopicRepository.getFirstTopicUrl()
      val resolver = new TopicUrlAndPageNumberResolver(topicUrl, None)
      val urlAndPageNumber = resolver.resolve
      val url = urlAndPageNumber._1
      val pageNumber = urlAndPageNumber._2

      url must not be equalTo(topicUrl)
      pageNumber must be > (1)
    }

    "resolve urlAndPageNumber from firstPage with pageNumber" in {
      val topicUrl: String = TopicRepository.getFirstTopicUrl()
      val resolver = new TopicUrlAndPageNumberResolver(topicUrl, Some(1000))
      val urlAndPageNumber = resolver.resolve
      val url = urlAndPageNumber._1
      val pageNumber = urlAndPageNumber._2

      url must not be equalTo(topicUrl)
      pageNumber must be equalTo (1000)
    }

    "partition avatars and images" in {
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
      val imagesFinder: PageImagesFinder = new PageImagesFinder(TopicRepository.getFirstTopicUrl)
      val rearrangeImgs = imagesFinder.rearrangeImages(imgs)
      val concatImgs = rearrangeImgs._1 ++ rearrangeImgs._2

      val expected = List(
        "http://forum-images.hardware.fr/images/perso/cerveau ouch.gif",
        "http://forum-images.hardware.fr/icones/redface.gif",
        "http://forum-images.hardware.fr/images/perso/mlc.gif",
        "http://forum-images.hardware.fr/images/perso/4/ticento.gif",
        "http://forum-images.hardware.fr/images/perso/ripthejacker.gif",
        "http://forum-images.hardware.fr/images/perso/2/ixam.gif",
        "http://hfr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6",
        "http://hfr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif",
        "http://i.imgur.com/XbuN4JK.gif?1",
        "http://i.minus.com/iKmav6Fr2vvBk.gif"
      )
      concatImgs.size must be equalTo (expected.size)
      concatImgs must be equalTo (expected)
    }

    "change pageNumber from url" in {
      val pageNumber = "100"
      val url = """_[0-9]+\.""".r.replaceAllIn("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_2244.htm", "_" + pageNumber + ".")
      url must equalTo("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_100.htm")
    }

  }

}