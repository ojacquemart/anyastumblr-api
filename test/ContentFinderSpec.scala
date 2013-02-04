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
      currentPage must be equalTo(nbPages)

      val pageIndexes = contentFinder.getPageIndexes(url)
      pageIndexes._1 must equalTo(currentPage)
      pageIndexes._2 must equalTo(currentPage - 1)
      pageIndexes._3 must equalTo(-1)

    }

    "match indexes with given pageNumber" in {
      val topic: Topic = TopicRepository.getTopics().head
      val url = topic.url
      val contentFinder = new ContentFinder(topic, Some(2244))
      val currentPage: Int = contentFinder.getCurrentPage(url)
      val nbPages = contentFinder.getNbPages(url)
      currentPage must be<(nbPages)

      val pageIndexes = contentFinder.getPageIndexes(url)
      pageIndexes._1 must be equalTo(2244)
      pageIndexes._2 must equalTo(2243)
      pageIndexes._3 must equalTo(2245)
    }

    "change pageNumber from url" in {
      val pageNumber = "100"
      val url = """_[0-9]+\.""".r.replaceAllIn("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_2244.htm", "_" + pageNumber + ".")
      url must equalTo("http://forum.hardware.fr/hfr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_100.htm")
    }

  }

}