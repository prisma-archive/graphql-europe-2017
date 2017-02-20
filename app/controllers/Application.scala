package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.Configuration
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import repo.ContentRepo
import views.{Config, Speaker}

class Application @Inject() (config: Configuration, repo: ContentRepo) extends Controller {
  val baseUrl = config.getString("graphqlEurope.canonicalUrl").get + "/image"

  val conf = config.underlying.as[Config]("graphqlEurope")

  def index = Action { req ⇒
    Ok(views.html.index(conf, repo.speakers, req.queryString.contains("dark"), req.queryString.contains("topLogo")))
  }

  def codeOfConduct = Action(Ok(views.html.codeOfConduct(conf)))
  def imprint = Action(Ok(views.html.imprint(conf)))
  def team = Action(Ok(views.html.team(conf, repo.team)))
}
