package tumblr

import org.specs2.mutable._

import tumblr.model._
import tumblr._

import tumblr.dao.LocalSiteDao._

class ContentFinderSpec extends Specification {

  "The ContentFinder class" should {

    "partition avatars and images" in {
      val images = List(
        Image.get("http://forum-images.hardware.fr/themes/dark/shit.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image.get("http://i.minus.com/iKmav6Fr2vvBk.gif"),
        Image.get("http://forum-images.hardware.fr/icones/redface.gif"),
        Image.get("http://forum-images.hardware.fr/icones/redface.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/mlc.gif"),
        Image.get("http://i.imgur.com/XbuN4JK.gif?1"),
        Image.get("http://forum-images.hardware.fr/images/perso/4/ticento.gif"),
        Image.get("http://tumblr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif"),
        Image.get("http://tumblr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/ripthejacker.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/ripthejacker.gif"),
        Image.get("http://tumblr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6"),
        Image.get("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/2/ixam.gif")
      )
      val site = LocalSites.head
      val imagesFinder: ImagesFinder = new ImagesFinder(site.url, site.configuration)
      val rearrangeImgs = imagesFinder.rearrangeImages(images)
      val concatImgs = rearrangeImgs._1 ++ rearrangeImgs._2

      val expected = List(
        Image.get("http://forum-images.hardware.fr/images/perso/cerveau ouch.gif"),
        Image.get("http://forum-images.hardware.fr/icones/redface.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/mlc.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/4/ticento.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/ripthejacker.gif"),
        Image.get("http://forum-images.hardware.fr/images/perso/2/ixam.gif"),
        Image.get("http://tumblr-rehost.net/gif/46c39e78876acadea512dd8399bf4db47eb6"),
        Image.get("http://tumblr-rehost.net/http://cdn.uproxx.com/wp-content/uploads/2013/02/many-bill-murray.gif"),
        Image.get("http://i.imgur.com/XbuN4JK.gif?1"),
        Image.get("http://i.minus.com/iKmav6Fr2vvBk.gif")
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