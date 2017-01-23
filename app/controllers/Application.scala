package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import play.api.mvc._
import play.api.Configuration
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import repo.{GraphCoolClient, SubscriberRepo}
import views.{Config, Speaker}

class Application @Inject() (config: Configuration) extends Controller {
  val speakers = List(
    Speaker(
      name = "Lee Byron",
      photo = Some("lee-byron.jpg"),
      talkTitle = Some("Keynote"),
      company = Some("Facebook, GraphQL Co-creator"),
      twitterUrl = Some("leeb"),
      githubUrl = Some("leebyron")
    ),

    Speaker(
      name = "Sashko Stubailo",
      photo = Some("sashko-stubailo.jpg"),
      talkTitle = None,
      company = Some("Apollo"),
      twitterUrl = Some("stubailo"),
      githubUrl = Some("stubailo")
    ),

    Speaker(
      name = "This could be you!",
      photo = Some("you.png"),
      talkTitle = Some("Register today"),
      company = None,
      twitterUrl = None,
      githubUrl = None
    )
  )

  val conf = config.underlying.as[Config]("graphqlEurope")

  def index = Action { req ⇒
    Ok(views.html.index(conf, speakers, req.queryString.contains("dark"), req.queryString.contains("topLogo")))
  }

  def codeOfConduct = Action(Ok(views.html.codeOfConduct(conf)))
  def imprint = Action(Ok(views.html.imprint(conf)))
}
