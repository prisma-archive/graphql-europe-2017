package repo

import javax.inject.Inject

import com.google.inject.Singleton
import model.Subscriber
import play.api.libs.json.{JsString, Json}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserRepo {
  def login(email: String, password: String)(implicit client: GraphCoolClient): Future[Option[String]] =
    client.request(
      """
        mutation Signin($email: String!, $password: String!) {
          signinUser(email: {email: $email, password: $password}) {
            token
          }
        }
      """,
      "email" → JsString(email),
      "password" → JsString(password)
    ).map(res ⇒ (res \ "data" \ "signinUser" \ "token").asOpt[String])

}
