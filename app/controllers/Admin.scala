package controllers

import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

import akka.util.ByteString
import play.api.mvc.Controller
import views.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.mvc._
import play.api.{Configuration, Logger}
import play.api.data._
import play.api.data.Forms._
import play.api.http.Writeable
import repo._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal


class Admin @Inject() (config: Configuration, userRepo: UserRepo, subsRepo: SubscriberRepo, clientProvider: GraphCoolClientProvider, mailchimpRepo: MailchimpRepo) extends Controller {
  val log = Logger(this.getClass)

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

  def exportToMailchimp = Action.async { implicit req ⇒
    subsRepo.subscriberNotExportedToMailchimp.flatMap { subs ⇒
      subs.foldLeft(Future.successful(0)) {
        case (acc, s) ⇒
          acc.flatMap { count ⇒
            mailchimpRepo.subscribe(s.email, s.name, !s.unsubscribed).flatMap {
              case Some(id) ⇒
                subsRepo.mcExported(s.id, id).map(_ ⇒ count + 1).recover {
                  case NonFatal(e) ⇒
                    log.error(s"Failed to export subscriber with id '${s.id}'.", e)
                    count
                }
              case None ⇒ Future.successful(count)
            }.recover {
              case NonFatal(e) ⇒
                log.error(s"Failed to export subscriber with id '${s.id}'.", e)
                count
            }
          }
      }.map(count ⇒ Ok(views.html.admin.info(conf, "Done", "exported " + count)))
    }
  }

  def notifications = Action.async { implicit req ⇒
    val date = new SimpleDateFormat("dd-mm-YYYY-HH-MM-SS").format(new Date)
    val countF = subsRepo.subscriberCount
    val notNotifiedF = subsRepo.subscriberNotNotifiedCount
    val notExportedF = subsRepo.subscriberNotExportedToMailchimpCount
    val unsubscribedF = subsRepo.subscriberUnsubscribedCount

    for {
      count ← countF
      notNotified ← notNotifiedF
      notExported ← notExportedF
      unsubscribed ← unsubscribedF
    } yield Ok(views.html.admin.notifications(conf, count, notExported, notNotified, unsubscribed, date))
  }

  def exportSubs(suffix: String) = Action.async { implicit req ⇒
    if (suffix.endsWith(".csv")) {
      subsRepo.subscriberList.map { subs ⇒
        val list = subs.map(s ⇒ s.name + "," + s.email + "," + s.createdAt).mkString("\n")

        Ok("name,email,date\n" + list)(Writeable(ByteString.apply, Some("text/csv")))
      }
    } else {
      Future.successful(BadRequest("Only CSV are supported!"))
    }
  }

  implicit def secureClient(implicit req: Request[_]): GraphCoolClient =
    clientProvider.forToken(req.session("token"))
}
