package controllers

import java.net.URI
import javax.inject.Inject

import play.api.mvc.Controller
import views.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.mvc._
import play.api.Configuration
import play.api.data._
import play.api.data.Forms._
import repo.{GraphCoolClient, GraphCoolClientProvider, SubscriberRepo, UserRepo}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Admin @Inject() (config: Configuration, userRepo: UserRepo, subsRepo: SubscriberRepo, clientProvider: GraphCoolClientProvider) extends Controller {
  case class Login(email: String, password: String, origPath: String)

  val loginForm = Form(mapping("email" → text, "password" → text, "origPath" → text)(Login.apply)(Login.unapply))

  val conf = config.underlying.as[Config]("graphqlEurope")

  def index = Action(Redirect(routes.Admin.notifications()))

  def login(origPath: Option[String]) = Action(
    Ok(views.html.admin.login(conf, origPath getOrElse routes.Admin.notifications().path(), Nil)))

  def doLogin = Action.async { implicit req ⇒
    loginForm.bindFromRequest.fold(
      _ ⇒ {
        Future.successful(Ok(views.html.admin.login(conf, routes.Admin.notifications().path(), List("Wrong fields!"))))
      },

      login ⇒ {
        userRepo.login(login.email, login.password)(clientProvider.unauthenticated).map {
          case Some(token) ⇒
            Redirect(login.origPath).withSession("token" → token)
          case None ⇒
            Ok(views.html.admin.login(conf, routes.Admin.notifications().path(), List("E-Mail or password is invalid!")))
        }

      }
    )
  }

  def logout = Action(Redirect(routes.Admin.login(None)).withNewSession)

  def notifications = Action.async { implicit req ⇒
    subsRepo.subscriberCount.map(count ⇒ Ok(views.html.admin.notifications(conf, count)))
  }

  implicit def secureClient(implicit req: Request[_]): GraphCoolClient =
    clientProvider.forToken(req.session("token"))
}
