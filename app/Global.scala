import play.api._
import play.api.mvc._

import tumblr.admin.filters.AdminFilter

object Global extends WithFilters(AdminFilter) with GlobalSettings {

}
