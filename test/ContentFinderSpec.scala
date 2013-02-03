import org.specs2.mutable._


import hfr._

class ContentFinderSpec extends Specification {

  "The ContentFinder class" should {

    "get currentPage and nbPages" in {
      val contentFinder = new ContentFinder("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1000.htm")
      val currentPage: Int = contentFinder.getCurrentPage()
      val nbPages = contentFinder.getNbPages()
      currentPage must equalTo(1000)
      currentPage must be<(nbPages)

      val pageIndexes = contentFinder.getPageIndexes()
      pageIndexes._1 must equalTo(1000)
      pageIndexes._2 must equalTo(999)
      pageIndexes._3 must equalTo(1001)

    }

    "give content" in {
      val content = new ContentFinder("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_2244.htm").getContent
      content.currentPage must be equalTo(2244)
      content.previousPage must be equalTo(2243)
      content.nextPage must be equalTo(2245)
      content.images should not be None
      content.images.
      //content.linkPages should not be None
    }
  }


}