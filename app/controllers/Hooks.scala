package controllers

import javax.inject.Inject

import model.Subscriber
import play.api.libs.json.{JsString, Json}
import play.api.{Configuration, Logger}
import play.api.mvc.{Action, Controller, Result}
import repo.{GraphCoolClientProvider, MailClient, MailchimpRepo, SubscriberRepo}
import views.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class Hooks @Inject() (config: Configuration, subscribers: SubscriberRepo, mailClient: MailClient, clientProvider: GraphCoolClientProvider, mailchimpRepo: MailchimpRepo) extends Controller {
  val conf = config.underlying.as[Config]("graphqlEurope")
  val log = Logger(this.getClass)

  implicit val client = clientProvider.internal

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
              InternalServerError("Oops.")
          }
      case None ⇒
        Future.successful(NotFound(s"Subscriber with ID '$id' not found."))
    })

  def unsubscribe(id: String) =
    Action.async {
      subscribers.unsubscribe(id)
        .flatMap {
          case Some(email) ⇒
            mailchimpRepo.unsubscribe(email).map(_ ⇒ ())

          case None ⇒
            log.error(s"Failed to unsubscribe ID '$id'!")
            Future.successful(())
        }
        .map(_ ⇒ Ok(views.html.unsubscribed(conf)))
    }

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

  def unsunscribeMcGet = Action(Ok("Everything is fine!"))

  def unsunscribeMc = Action.async { req ⇒
    req.body.asFormUrlEncoded.map { body ⇒
      body.get("data[email]") match {
        case Some(emails) ⇒
          val email = emails.head

          subscribers.byEmail(email).flatMap {
            case Some(s) ⇒
              subscribers.unsubscribe(s.id).map { _ ⇒
                log.info(s"Subscriber unsubscribed from mailchimp ${s.id}")

                Ok("Done")
              }

            case None ⇒
              log.info("Subscriber not found of email: " + email)

              Future.successful(Ok("Done"))
          }

        case None ⇒
          Future.successful(BadRequest("No email found!"))
      }
    }.getOrElse(Future.successful(BadRequest("Expecting form data")))
  }

  def test = Action.async { req ⇒
    log.info("queryString: " + req.queryString)

    req.body.asJson.map { body ⇒
      log.info("Test endpoint is called! (POST) " + Json.prettyPrint(body))

      Future.successful(Ok("Thanks!"))
    }.getOrElse {
      req.body.asFormUrlEncoded.map { body ⇒
        log.info("Test endpoint is called! (POST form) " + body)

        Future.successful(Ok("Thanks!"))
      }.getOrElse(
        req.body.asText.map { txt ⇒
          log.info("Test endpoint is called! (POST text) " + txt)

          Future.successful(Ok("Thanks!"))
        }.getOrElse(Future.successful(BadRequest("Expecting Json data")))
      )
    }
  }

  def testGet = Action.async { req ⇒
    req.body.asJson.map { body ⇒
      log.info("Log endpoint is called! (GET) " + Json.prettyPrint(body))

      Future.successful(Ok("Thanks!"))
    }.getOrElse {
      log.info("Log endpoint is called without body! (GET) ")

      Future.successful(Ok("Thanks"))
    }
  }

  def subscriberCreatedMailchimp = Action.async { req ⇒
    req.body.asJson.map { body ⇒
      (body \ "createdNode" \ "id").toOption match {
        case Some(JsString(id)) ⇒
          syncMailchimp(id)
          .recover {
            case e: Exception ⇒
              log.error(s"Can't send subscription info to mailchimp for ID '$id'.", e)
              Ok("Oops. Something went wrong.")
          }

        case _ ⇒ Future.successful(BadRequest("Wrong Json data shape"))
      }
    }.getOrElse {
      Future.successful(BadRequest("Expecting Json data"))
    }
  }

  def subscriberDelete = Action.async { req ⇒
    req.body.asJson.map { body ⇒
      (body \ "deletedNode" \ "email").toOption match {
        case Some(JsString(email)) ⇒
          mailchimpRepo.delete(email).map(_ ⇒ Ok("Done"))
          .recover {
            case e: Exception ⇒
              log.error(s"Can't delete subscription in mailchimp.", e)
              Ok("Oops. Something went wrong.")
          }

        case _ ⇒ Future.successful(BadRequest("Wrong Json data shape"))
      }
    }.getOrElse {
      Future.successful(BadRequest("Expecting Json data"))
    }
  }

  private def syncMailchimp(id: String): Future[Result] = {
    subscribers.byId(id).flatMap {
      case Some(subscriber) if !subscriber.mailchimpExported ⇒
        mailchimpRepo.subscribe(subscriber.email, subscriber.name, !subscriber.unsubscribed)
        .flatMap {
          case Some(id) ⇒ subscribers.mcExported(subscriber.id, id)
          case None ⇒ Future.successful(())
        }
        .map(_ ⇒ Ok("Done"))

      case _ ⇒
        Future.successful(Ok("Done"))

    }
  }

  private def sendSubscriptionConfirmation(id: String): Future[Result] = {
    subscribers.byId(id).flatMap {
      case Some(subscriber) if !subscriber.notified && !subscriber.unsubscribed && mailClient.isConfigured ⇒
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
      log.info("Sending an email to subscriber with id" + sub.id)

      Future.fromTry(Try {
        mailClient.send(
          to = sub.email,
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
