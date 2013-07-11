import org.specs2.mutable._

import TestAwait._

import securesocial.core._

import tumblr.dao._
import tumblr.model._

class UserDaoSpec extends Specification {

  "The UserDao class" should {

    "save and find*" in new FakeApp {
        val newUser =  User.get(
          UserId("testtest", "userpass"),
          "testtest",
          "testtest",
          "testtest",
          Some("test@est.fr"),
          None,
          AuthenticationMethod.UserPassword,
          None,
          None,
          "test"
        )
      UserDao.save(newUser)

      result(UserDao.count()) must be>=1

      val foundUser: User = result(UserDao.findAll()).head
      newUser == foundUser must beTrue

      val maybeByIdUser = result(UserDao.findById(newUser.id))
      maybeByIdUser must not be equalTo(None)
      newUser == maybeByIdUser.get must beTrue
    }

  }

}