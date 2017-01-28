package model

import play.api.libs.json.Json


case class Subscriber(id: String, email: String, name: String, notified: Boolean, unsubscribed: Boolean, mailchimpExported: Boolean, mailchimpId: Option[String])

object Subscriber {
  implicit val json = Json.format[Subscriber]
}

case class SubscriberFull(id: String, email: String, name: String, notified: Boolean, unsubscribed: Boolean, createdAt: String, updatedAt: String, mailchimpExported: Boolean, mailchimpId: Option[String])

object SubscriberFull {
  implicit val json = Json.format[SubscriberFull]
}