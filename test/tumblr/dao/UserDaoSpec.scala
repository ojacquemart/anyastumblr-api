package tumblr.dao

import org.specs2.mutable._

import securesocial.core._

import play.api.test.Helpers._

import tumblr.dao._
import tumblr.model._

class UserDaoSpec extends Specification {

  "The UserDao class" should {

    "save and find*" in new FakeDaoApp {
        val newUser =  User.get(
          IdentityId("testtest", "userpass"),
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

      await(UserDao.count()) must be>=1

      val foundUser: User = await(UserDao.findAll()).head
      newUser == foundUser must beTrue

      val maybeByIdUser = await(UserDao.findById(newUser.identityId))
      maybeByIdUser must not be equalTo(None)
      newUser == maybeByIdUser.get must beTrue
    }

  }

}
