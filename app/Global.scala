import play.api._
import play.api.mvc._

import tumblr.filters._

object Global extends WithFilters(TumblrAdminFilter) with GlobalSettings {

}
