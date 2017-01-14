package controllers

import javax.inject.Inject

import model.Subscriber
import play.api.libs.json.{JsString, Json}
import play.api.{Configuration, Logger}
import play.api.mvc.{Action, Controller, Result}
import repo.SubscriberRepo
import views.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Hooks @Inject() (config: Configuration, sunscribers: SubscriberRepo) extends Controller {
  val conf = config.underlying.as[Config]("graphqlEurope")
  val log = Logger(this.getClass)

  def unsubscribe(id: String) =
    Action.async(sunscribers.unsubscribe(id).map(_ ⇒ Ok(views.html.unsubscribed(conf))))

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
    sunscribers.byId(id).flatMap {
      case Some(subscriber) if !subscriber.notified && !subscriber.unsubscribed ⇒
        sendEmail(subscriber)
          .flatMap(_ ⇒ sunscribers.notified(id))
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

      Future.successful(())
    }
  }
}
