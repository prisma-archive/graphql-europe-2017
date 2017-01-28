package filter

import javax.inject.Inject

import controllers.routes
import play.api.Configuration
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.Future

class LoginRedirectFilter @Inject() (config: Configuration) extends EssentialFilter with Results {
  def apply(next: EssentialAction) = EssentialAction { req ⇒
    if (req.path.startsWith("/admin") && !req.path.startsWith("/admin/login") && req.path != "/admin")
      req.session.get("token") match {
        case None ⇒ Accumulator.done(Future.successful(Redirect(routes.Admin.login(Some(req.path)))))
        case Some(_) ⇒ next(req)
      }
    else
      next(req)
  }
}
