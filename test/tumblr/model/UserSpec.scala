package tumblr.model

import org.specs2.mutable._

class UserSpec extends Specification {

  "The User class" should {

    "map base64 to an user instance" in {
      val user = User.decodeBase64("Zm9vOmJhcg==")
      user.name must be equalTo("foo")
      user.password must be equalTo("bar")
    }

    "encode base64" in {
      val base64 = User.encodeBase64("foo", "bar")
      base64 must be equalTo("Zm9vOmJhcg==")
    }

  }

}
