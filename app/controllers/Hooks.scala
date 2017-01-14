package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.{Configuration, Logger}
import play.api.mvc.{Action, Controller}

class Hooks @Inject() (config: Configuration) extends Controller {
  val log = Logger(this.getClass)

  def subscriberCreated = Action { req ⇒
    req.body.asJson.map { body ⇒
      log.info("Body json: " + Json.stringify(body))

      Ok("Thanks!")
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }
}
