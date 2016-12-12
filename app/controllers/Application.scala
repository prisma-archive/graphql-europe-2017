package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.Configuration
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import views.{Config, Speaker}

class Application @Inject() (config: Configuration) extends Controller {
  val speakers = List(
    Speaker(
      name = "Lee Byron",
      photo = Some("lee-byron.jpg"),
      description = Some("GraphQL and Development speaker"),
      company = Some("Facebook"),
      twitterUrl = Some("leeb"),
      githubUrl = Some("leebyron")
    )
  )

  val conf = config.underlying.as[Config]("graphqlEurope")

  def index = Action { req ⇒
    Ok(views.html.index(conf, speakers, req.queryString.contains("dark"), req.queryString.contains("topLogo")))
  }
}
