import javax.inject.Inject

import filter.CacheControlFilter
import play.api.http.HttpFilters
import play.filters.cors.CORSFilter

class Filters @Inject()(corsFilter: CORSFilter, cacheFilter: CacheControlFilter) extends HttpFilters {
  def filters = Seq(corsFilter, cacheFilter)
}
