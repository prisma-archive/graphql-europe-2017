import javax.inject.Inject

import filter.{CacheControlFilter, LoginRedirectFilter}
import play.api.http.HttpFilters
import play.filters.cors.CORSFilter

class Filters @Inject()(corsFilter: CORSFilter, cacheFilter: CacheControlFilter, loginFilter: LoginRedirectFilter) extends HttpFilters {
  def filters = Seq(corsFilter, cacheFilter, loginFilter)
}
