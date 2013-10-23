package tumblr.admin.filters

import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

import play.api.mvc._
import play.api.libs.iteratee.Iteratee

import tumblr.admin.service.UserService

/**
 * A filter to avoid access to json api to not authenticated users.
 * The json api starts with /api/tumblr/admin.
 * Secure social allows to get the current user by the method SecureSocial#current(request).
 */
object AdminFilter extends EssentialFilter {

  val PathApiTumblrAdmin = "/api/tumblr/admin"

  def apply(next: EssentialAction) = new EssentialAction {

    def apply(request: RequestHeader): Iteratee[Array[Byte], SimpleResult] = {
      def result(auth: Boolean) = {
        if (auth) next(request)
        else Iteratee.ignore[Array[Byte]].map(_ => Results.Forbidden)
      }

      if (!request.path.startsWith(PathApiTumblrAdmin)) next(request)
      else {
        Iteratee.flatten({
          for {
            auth <- UserService.hasCurrentUser(request)
          } yield result(auth)
        })
      }
    }


  }
}