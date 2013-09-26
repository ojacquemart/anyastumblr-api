package tumblr.admin.filters

import scala.concurrent.ExecutionContext
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
      def isForbidden(): Boolean = request.path.startsWith(PathApiTumblrAdmin) && !UserService.hasCurrentUser(request)

      if (isForbidden()) Iteratee.ignore[Array[Byte]].map(_ => Results.Forbidden)
      else next(request)
    }
  }
}