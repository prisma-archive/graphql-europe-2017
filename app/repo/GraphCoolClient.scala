package repo

import javax.inject.Inject

import com.google.inject.Singleton
import play.api.Configuration
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class GraphCoolClient @Inject() (client: WSClient, config: Configuration) {
  val apiToken = config.getString("graphqlEurope.graphCoolToken").get
  val projectKey = config.getString("graphqlEurope.graphCoolProjectKey").get
  val url = s"https://api.graph.cool/simple/v1/$projectKey"

  def request(query: String, variables: (String, JsValue)*) =
    client.url(url)
      .withHeaders("Authorization" → s"Bearer $apiToken")
      .post(JsObject(Seq("query" → JsString(query), "variables" → JsObject(variables))))
      .map(res ⇒ Json.parse(res.body))

}
