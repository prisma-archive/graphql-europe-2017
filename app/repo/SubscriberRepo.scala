package repo

import javax.inject.Inject

import com.google.inject.Singleton
import model.{Subscriber, SubscriberFull}
import play.api.Configuration
import play.api.libs.json.JsString
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
          }
        }
      """,
      "id" → JsString(id)
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

  def unsubscribe(id: String)(implicit client: GraphCoolClient): Future[Unit] =
    client.request(
      """
        mutation($id: ID!) {
          updateSubscriber(id: $id, unsubscribed: true) {
            id
          }
        }
      """,
      "id" → JsString(id)
    ).map(res ⇒ ())

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
            createdAt
            updatedAt
          }
        }
      """
    ).map(res ⇒ (res \ "data" \ "allSubscribers").as[Seq[SubscriberFull]])
}
