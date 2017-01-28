package repo

import javax.inject.Inject

import com.google.inject.Singleton
import model.{Subscriber, SubscriberFull}
import play.api.Configuration
import play.api.libs.json.{JsString, Json}
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SubscriberRepo {
  def byId(id: String)(implicit client: GraphCoolClient): Future[Option[Subscriber]] =
    client.request(
      """
        query ($id: ID!) {
          Subscriber(id: $id) {
            id
            name
            email
            notified
            unsubscribed
            mailchimpExported
            mailchimpId
          }
        }
      """,
      "id" → JsString(id)
    ).map(res ⇒ (res \ "data" \ "Subscriber").asOpt[Subscriber])

  def byEmail(email: String)(implicit client: GraphCoolClient): Future[Option[Subscriber]] =
    client.request(
      """
        query ($email: String!) {
          Subscriber(email: $email) {
            id
            name
            email
            notified
            unsubscribed
            mailchimpExported
            mailchimpId
          }
        }
      """,
      "email" → JsString(email)
    ).map(res ⇒ (res \ "data" \ "Subscriber").asOpt[Subscriber])

  def notified(id: String)(implicit client: GraphCoolClient): Future[Unit] =
    client.request(
      """
        mutation($id: ID!) {
          updateSubscriber(id: $id, notified: true) {
            id
          }
        }
      """,
      "id" → JsString(id)
    ).map(res ⇒ ())

  def mcExported(id: String, mcId: String)(implicit client: GraphCoolClient): Future[Unit] =
    client.request(
      """
        mutation($id: ID!, $mcId: String!) {
          updateSubscriber(id: $id, mailchimpExported: true, mailchimpId: $mcId) {
            id
          }
        }
      """,
      "id" → JsString(id),
      "mcId" → JsString(mcId)
    ).map(res ⇒ ())

  /**
    * @return email of unsubscribed
    */
  def unsubscribe(id: String)(implicit client: GraphCoolClient): Future[Option[String]] =
    client.request(
      """
        mutation($id: ID!) {
          updateSubscriber(id: $id, unsubscribed: true) {
            email
          }
        }
      """,
      "id" → JsString(id)
    ).map { res ⇒
      (res \ "data" \ "updateSubscriber" \ "email").asOpt[String]
    }

  def subscriberCount(implicit client: GraphCoolClient): Future[Long] =
    client.requestRelay(
      """
        {
          viewer {
            allSubscribers {
              count
            }
          }
        }
      """
    ).map(res ⇒ (res \ "data" \ "viewer" \ "allSubscribers" \ "count").as[Long])

  def subscriberNotExportedToMailchimpCount(implicit client: GraphCoolClient): Future[Long] =
    client.requestRelay(
      """
        {
          viewer {
            allSubscribers(filter: {mailchimpExported: false}) {
              count
            }
          }
        }
      """
    ).map(res ⇒ (res \ "data" \ "viewer" \ "allSubscribers" \ "count").as[Long])

  def subscriberNotNotifiedCount(implicit client: GraphCoolClient): Future[Long] =
    client.requestRelay(
      """
        {
          viewer {
            allSubscribers(filter: {notified: false}) {
              count
            }
          }
        }
      """
    ).map(res ⇒ (res \ "data" \ "viewer" \ "allSubscribers" \ "count").as[Long])

  def subscriberUnsubscribedCount(implicit client: GraphCoolClient): Future[Long] =
    client.requestRelay(
      """
        {
          viewer {
            allSubscribers(filter: {unsubscribed: true}) {
              count
            }
          }
        }
      """
    ).map(res ⇒ (res \ "data" \ "viewer" \ "allSubscribers" \ "count").as[Long])

  def subscriberList(implicit client: GraphCoolClient): Future[Seq[SubscriberFull]] =
    client.request(
      """
        query {
          allSubscribers {
            id
            name
            email
            notified
            unsubscribed
            mailchimpExported
            mailchimpId
            createdAt
            updatedAt
          }
        }
      """
    ).map(res ⇒ (res \ "data" \ "allSubscribers").as[Seq[SubscriberFull]])

  def subscriberNotExportedToMailchimp(implicit client: GraphCoolClient): Future[Seq[SubscriberFull]] =
    client.request(
      """
        query {
          allSubscribers(filter: {mailchimpExported: false}) {
            id
            name
            email
            notified
            unsubscribed
            mailchimpExported
            mailchimpId
            createdAt
            updatedAt
          }
        }
      """
    ).map(res ⇒ {
      println(Json.prettyPrint(res))
      (res \ "data" \ "allSubscribers").as[Seq[SubscriberFull]]
    })
}
