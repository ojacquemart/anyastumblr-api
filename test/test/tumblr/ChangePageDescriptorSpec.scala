package test.tumblr

import org.specs2.mutable._

import tumblr.model._

class ChangePageDescriptorSpec extends Specification {

  "The ChangePageDescriptor class" should {

    "replace page number for joiesducode" in {
      val token = "http://lesjoiesducode.tumblr.com/page/1"

      val changeUrlPageDescriptor = JoiesDuCodeConfiguration.get().pageResolver.changePageDescriptor
      val regex = changeUrlPageDescriptor.regex.r
      val newToken = regex.replaceFirstIn(token, changeUrlPageDescriptor.replacement.format(2))

      newToken must be equalTo("http://lesjoiesducode.tumblr.com/page/2")
    }

    "replace page number for tumblr" in {
      val token = "http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm"

      val changeUrlPageDescriptor = HfrConfiguration.get().pageResolver.changePageDescriptor
      val regex = changeUrlPageDescriptor.regex.r
      val newToken = regex.replaceFirstIn(token, changeUrlPageDescriptor.replacement.format(999))

      newToken must be equalTo("http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_999.htm")
    }


  }

}