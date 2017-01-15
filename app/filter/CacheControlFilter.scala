package filter

import language.postfixOps
import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.mvc.{EssentialAction, EssentialFilter}
import play.api.libs.iteratee.Execution.Implicits.trampoline

import scala.concurrent.duration._

@Singleton
class CacheControlFilter @Inject() (config: Configuration) extends EssentialFilter {
  val cacheEnabled = config.getBoolean("cacheEnabled") getOrElse true

  def header(path: String): (String, String) =
    if (path startsWith "/assets/lib")
      CacheControl → maxAge(30 minutes)
    else if (path startsWith "/assets")
      CacheControl → maxAge(1 day)
    else if (path == "code-of-conduct" || path == "imprint")
      CacheControl → maxAge(1 day)
    else if (path == "/")
      CacheControl → maxAge(30 minutes)
    else
      CacheControl → noCache

  def apply(next: EssentialAction) = EssentialAction { req ⇒
    if (cacheEnabled)
      next(req).map(_.withHeaders(header(req.path)))
    else
      next(req)
  }

  def maxAge(duration: Duration) = s"max-age=${duration.toSeconds}"
  val noCache = "no-cache"
  val CacheControl = "Cache-Control"
}
