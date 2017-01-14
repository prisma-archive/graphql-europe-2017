package model

import play.api.libs.json.Json


case class Subscriber(id: String, email: String, name: String)

object Subscriber {
  implicit val json = Json.format[Subscriber]
}
