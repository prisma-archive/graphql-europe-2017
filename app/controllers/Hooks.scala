package controllers

import javax.inject.Inject

import play.api.libs.json.{JsString, Json}
import play.api.{Configuration, Logger}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class Hooks @Inject() (config: Configuration) extends Controller {
  val log = Logger(this.getClass)

  def subscriberCreated = Action.async { req ⇒
    req.body.asJson.map { body ⇒
      (body \ "createdNode" \ "id").toOption match {
        case Some(JsString(id)) ⇒
          try {
            sendSubscriptionConfirmation(id)

            Future.successful(Ok("Done"))
          } catch {
            case e: Exception ⇒
              log.error(s"Can't send subscription notification for ID '$id'.", e)
              Future.successful(Ok("Oops. Something went wrong."))
          }

        case _ ⇒ Future.successful(BadRequest("Wrong Json data shape"))
      }
    }.getOrElse {
      Future.successful(BadRequest("Expecting Json data"))
    }
  }

  private def sendSubscriptionConfirmation(id: String): Unit = {
    log.info("ID: " + id)
  }
}
