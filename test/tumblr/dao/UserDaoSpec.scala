package tumblr.dao

import org.specs2.mutable._

import play.api.test.Helpers.defaultAwaitTimeout

import tumblr.dao._
import tumblr.model._

class UserDaoSpec extends Specification {

  "The UserDao class" should {

    "save and find" in new FakeDaoApp {
      val newUser =  new User("foo", "foo")
      UserDao.save(newUser)

      play.api.test.Helpers.await(UserDao.count()) must be>=1

      val foundUser: User = play.api.test.Helpers.await(UserDao.findAll()).head
      newUser == foundUser must beTrue

      val maybeByIdUser = play.api.test.Helpers.await(UserDao.findByPK("foo"))
      maybeByIdUser must not be equalTo(None)
      newUser == maybeByIdUser.get must beTrue
    }

  }

}
