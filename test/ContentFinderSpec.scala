import org.specs2.mutable._

import model._
import dao._
import tumblr._

class ContentFinderSpec extends Specification {

  "The ContentFinder class" should {

    "partition avatars and images" in {
      val images = List(
        Image("http://forum-images.hardware.fr/themes/dark/shit.gif"),
        Image("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image("http://i.minus.com/iKmav6Fr2vvBk.gif"),
        Image("http://forum-images.hardware.fr/icones/redface.gif"),
        Image("http://forum-images.hardware.fr/icones/redface.gif"),
        Image("http://forum-images.hardware.fr/images/perso/mlc.gif"),
        Image("http://i.imgur.com/XbuN4JK.gif?1"),
        Image("http://forum-images.hardware.fr/images/perso/4/ticento.gif"),
        Image("http://tumblr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif"),
        Image("http://tumblr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif"),
        Image("http://forum-images.hardware.fr/images/perso/ripthejacker.gif"),
        Image("http://forum-images.hardware.fr/images/perso/ripthejacker.gif"),
        Image("http://tumblr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif")
      )
      val site = SiteDao.getFirstSite
      val imagesFinder: PageImagesFinder = new PageImagesFinder(site.url, site.configuration)
      val rearrangeImgs = imagesFinder.rearrangeImages(images)
      val concatImgs = rearrangeImgs._1 ++ rearrangeImgs._2

      val expected = List(
        Image("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image("http://forum-images.hardware.fr/icones/redface.gif"),
        Image("http://forum-images.hardware.fr/images/perso/mlc.gif"),
        Image("http://forum-images.hardware.fr/images/perso/4/ticento.gif"),
        Image("http://forum-images.hardware.fr/images/perso/ripthejacker.gif"),
        Image("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image("http://tumblr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6"),
        Image("http://tumblr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif"),
        Image("http://i.imgur.com/XbuN4JK.gif?1"),
        Image("http://i.minus.com/iKmav6Fr2vvBk.gif")
      )

      concatImgs.size must be equalTo (expected.size)
      concatImgs must be equalTo (expected)
    }

    "change pageNumber from src" in {
      val pageNumber = "100"
      val url = """_[0-9]+\.""".r.replaceAllIn("http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_2244.htm", "_" + pageNumber + ".")
      url must equalTo("http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_100.htm")
    }

  }

}