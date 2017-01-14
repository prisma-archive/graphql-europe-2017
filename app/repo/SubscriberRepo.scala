package repo

import javax.inject.Inject

import com.google.inject.Singleton
import model.Subscriber
import play.api.Configuration
import play.api.libs.json.JsString
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SubscriberRepo @Inject() (client: GraphCoolClient) {
  def byId(id: String): Future[Option[Subscriber]] =
    client.request(
      """
        query ($id: ID!) {
          Subscriber(id: $id) {
            id
            name
            email
          }
        }
      """,
      "id" → JsString(id)
    ).map(res ⇒ (res \ "data" \ "Subscriber").asOpt[Subscriber])
}
