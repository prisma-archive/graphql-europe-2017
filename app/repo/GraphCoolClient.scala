package repo

import javax.inject.Inject

import com.google.inject.Singleton
import play.api.Configuration
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class GraphCoolClientProvider @Inject() (client: WSClient, config: Configuration) {
  val projectToken = config.getString("graphqlEurope.graphCoolToken").get

  lazy val internal = new GraphCoolClient(client, config, Some(projectToken))

  lazy val unauthenticated = new GraphCoolClient(client, config, None)

  def forToken(token: String) = new GraphCoolClient(client, config, Some(token))
}

class GraphCoolClient(client: WSClient, config: Configuration, token: Option[String]) {
  val projectKey = config.getString("graphqlEurope.graphCoolProjectKey").get
  val url = s"https://api.graph.cool/simple/v1/$projectKey"
  val urlRelay = s"https://api.graph.cool/relay/v1/$projectKey"

  def requestRelay(query: String, variables: (String, JsValue)*) =
    requestUrl(urlRelay)(query, variables: _*)

  def request(query: String, variables: (String, JsValue)*) =
    requestUrl(url)(query, variables: _*)

  private def requestUrl(url: String)(query: String, variables: (String, JsValue)*) = {
    val headers = token match {
      case Some(t) ⇒ Seq("Authorization" → s"Bearer $t")
      case None ⇒ Seq.empty
    }

    client.url(url)
      .withHeaders(headers: _*)
      .post(JsObject(Seq("query" → JsString(query), "variables" → JsObject(variables))))
      .map(res ⇒ Json.parse(res.body))
  }


}
