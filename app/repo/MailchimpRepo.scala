package repo

import java.math.BigDecimal
import java.security.MessageDigest
import javax.inject.{Inject, Singleton}

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsNumber, JsString, Json}
import play.api.libs.ws.{WSAuthScheme, WSClient}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MailchimpRepo @Inject() (config: Configuration, client: WSClient) {
  val conf = config.underlying.as[MailchimpConfig]("mailchimp")
  val log = Logger(this.getClass)

  private def subscribedEnum(subscribed: Boolean) =
    if (subscribed) "subscribed" else "unsubscribed"

  def subscribe(email: String, name: String, subscribed: Boolean): Future[Option[String]] =
    client
      .url(s"https://${conf.dc}.api.mailchimp.com/3.0/lists/${conf.listId}/members")
      .withAuth("anystring", conf.apiKey, WSAuthScheme.BASIC)
      .post(Json.obj(
        "email_address" → JsString(email),
        "merge_fields" → Json.obj("FNAME" → name),
        "status" → JsString(subscribedEnum(subscribed))
      ))
      .map(res ⇒ Json.parse(res.body))
      .flatMap { res ⇒
        (res \ "status").toOption match {
          case Some(JsNumber(num)) if num.toInt == 400 ⇒
            subscribeExisting(email, subscribed)
          case Some(JsString("subscribed")) ⇒
            Future.successful((res \ "id").asOpt[String])
        }
      }

  def delete(email: String): Future[Unit] =
    client
      .url(s"https://${conf.dc}.api.mailchimp.com/3.0/lists/${conf.listId}/members/${hash(email)}")
      .withAuth("anystring", conf.apiKey, WSAuthScheme.BASIC)
      .delete()
      .map(resp ⇒())

  def unsubscribe(email: String): Future[Option[String]] =
    client
      .url(s"https://${conf.dc}.api.mailchimp.com/3.0/lists/${conf.listId}/members/${hash(email)}")
      .withAuth("anystring", conf.apiKey, WSAuthScheme.BASIC)
      .patch(Json.obj(
        "status" → "unsubscribed"
      ))
      .map(res ⇒ Json.parse(res.body))
      .map { res ⇒
        (res \ "id").asOpt[String] match {
          case s @ Some(_) ⇒ s
          case None ⇒
            log.error(s"Cant unsubscribe subscriber '${hash(email)}' from mailchimp: " + Json.prettyPrint(res))
            None
        }
      }

  def subscribeExisting(email: String, subscribed: Boolean): Future[Option[String]] =
    client
        .url(s"https://${conf.dc}.api.mailchimp.com/3.0/lists/${conf.listId}/members/${hash(email)}")
        .withAuth("anystring", conf.apiKey, WSAuthScheme.BASIC)
        .patch(Json.obj(
          "status" → JsString(subscribedEnum(subscribed))
        ))
        .map(res ⇒ Json.parse(res.body))
        .map { res ⇒
          (res \ "id").asOpt[String] match {
            case s @ Some(_) ⇒ s
            case None ⇒
              log.error(s"Cant unsubscribe subscriber '${hash(email)}' from mailchimp: " + Json.prettyPrint(res))
              None
          }
        }

  def getSubscriberId(email: String): Future[Option[String]] = {
    client
      .url(s"https://${conf.dc}.api.mailchimp.com/3.0/lists/${conf.listId}/members/${hash(email)}")
      .withAuth("anystring", conf.apiKey, WSAuthScheme.BASIC)
      .get()
      .map(res ⇒ Json.parse(res.body))
      .map(res ⇒ {
        (res \ "id").asOpt[String] match {
          case s @ Some(_) ⇒ s
          case None ⇒
            log.error("Cant find mailchimp user by email: " + Json.prettyPrint(res))
            None
        }
      })
  }

  private def hash(s: String) = {
    val m = java.security.MessageDigest.getInstance("MD5")
    val b = s.toLowerCase.getBytes("UTF-8")
    m.update(b, 0, b.length)
    new java.math.BigInteger(1, m.digest()).toString(16)
  }
}

case class MailchimpConfig(dc: String, apiKey: String, listId: String)
