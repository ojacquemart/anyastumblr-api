package tumblr.filters

import play.api.mvc._
import play.api.libs.iteratee.Iteratee

import securesocial.core.SecureSocial

/**
 * Tumblr admin filter avoid access to json api to not authetnicated users.
 * The json api starts with /api/tumblr/admin.
 * Secure social allows to get the current user by the method SecureSocial#current(request).
 */
object TumblrAdminFilter extends EssentialFilter {

  val PathApiTumblrAdmin = "/api/tumblr/admin"

  def apply(next: EssentialAction) = new EssentialAction {

    def apply(request: RequestHeader): Iteratee[Array[Byte], Result] = {
      def isForbidden(): Boolean = request.path.startsWith(PathApiTumblrAdmin) && !SecureSocial.currentUser(request).isDefined

      if (isForbidden()) Iteratee.ignore[Array[Byte]].map(_ => Results.Forbidden)
      else next(request)
    }
  }
}