import play.api._
import play.api.mvc._

object Global extends GlobalSettings {


  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    proxify()
    super.onRouteRequest(request)
  }

  def proxify() {
    System.setProperty("proxyHost", "proxy")
    System.setProperty("proxyPort", "8888")
  }

}
