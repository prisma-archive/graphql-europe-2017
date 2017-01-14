package controllers

import javax.inject.Inject

import model.Subscriber
import play.api.libs.json.{JsString, Json}
import play.api.{Configuration, Logger}
import play.api.mvc.{Action, Controller, Result}
import repo.{MailClient, SubscriberRepo}
import views.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class Hooks @Inject() (config: Configuration, subscribers: SubscriberRepo, mailClient: MailClient) extends Controller {
  val conf = config.underlying.as[Config]("graphqlEurope")
  val log = Logger(this.getClass)

  def subscribed(id: String) =
    Action.async(subscribers.byId(id).map {
      case Some(s) ⇒ Ok(views.html.subscribed(conf, s))
      case None ⇒ NotFound(s"Subscriber with ID '$id' not found.")
    })

  def testNotification(id: String) =
    Action.async(subscribers.byId(id).flatMap {
      case Some(s) ⇒
        sendEmail(s)
          .map(_ ⇒ Ok("Done!"))
          .recover {
            case e: Exception ⇒
              log.error("Can't send an email!", e)
              InternalServerError("Opps.")
          }
      case None ⇒
        Future.successful(NotFound(s"Subscriber with ID '$id' not found."))
    })

  def unsubscribe(id: String) =
    Action.async(subscribers.unsubscribe(id).map(_ ⇒ Ok(views.html.unsubscribed(conf))))

  def subscriberCreated = Action.async { req ⇒
    req.body.asJson.map { body ⇒
      (body \ "createdNode" \ "id").toOption match {
        case Some(JsString(id)) ⇒
          sendSubscriptionConfirmation(id)
          .recover {
            case e: Exception ⇒
              log.error(s"Can't send subscription notification for ID '$id'.", e)
              Ok("Oops. Something went wrong.")
          }

        case _ ⇒ Future.successful(BadRequest("Wrong Json data shape"))
      }
    }.getOrElse {
      Future.successful(BadRequest("Expecting Json data"))
    }
  }

  private def sendSubscriptionConfirmation(id: String): Future[Result] = {
    subscribers.byId(id).flatMap {
      case Some(subscriber) if !subscriber.notified && !subscriber.unsubscribed ⇒
        sendEmail(subscriber)
          .flatMap(_ ⇒ subscribers.notified(id))
          .map(_ ⇒ Ok("Done"))

      case _ ⇒
        Future.successful(Ok("Done"))

    }
  }

  private def sendEmail(sub: Subscriber): Future[Unit] = {
    if (sub.email.endsWith("test123.com")) {
      log.info("Test email: " + sub.email + ". No notification was sent.")

      Future.successful(())
    } else {
      log.info("Sending an email to " + sub.email)

      Future.fromTry(Try {
        mailClient.send(
          to = "oleg.ilyenko@gmail.com",
          subject = "Thanks for subscribing to GraphQL-Europe updates!",
          text =
            s"""Thanks for subscribing, <strong>@subscriber.name</strong>!
              |You've been added to our newsletter list and now be among the first to hear about updates around the conference.
              |
              |Visit GraphQL-Europe website for more info:
              |
              |${conf.canonicalUrl}
              |
              |You can unsubscribe with following URL: ${conf.canonicalUrl}/unsubscribe/${sub.id}
            """.stripMargin,
          html = views.html.subscribed(conf, sub).toString)
      })
    }
  }
}
