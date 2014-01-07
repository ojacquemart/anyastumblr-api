import play.api._
import play.api.mvc._

import tumblr.admin.filters.AdminFilter
import tumblr.filters.CORSFilter

object Global extends WithFilters(AdminFilter, CORSFilter()) with GlobalSettings {

}
