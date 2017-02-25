package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.Configuration
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import repo.ContentRepo
import views.Config

class Application @Inject() (config: Configuration, repo: ContentRepo) extends Controller {
  val baseUrl = config.getString("graphqlEurope.canonicalUrl").get + "/image"

  val conf = config.underlying.as[Config]("graphqlEurope")

  def index = Action { implicit req ⇒
    Ok(views.html.index(actualConf, repo.speakers, repo.sponsors, req.queryString.contains("dark"), req.queryString.contains("topLogo")))
  }

  def codeOfConduct = Action(implicit req ⇒ Ok(views.html.codeOfConduct(actualConf)))
  def imprint = Action(implicit req ⇒ Ok(views.html.imprint(actualConf)))
  def team = Action(implicit req ⇒ Ok(views.html.team(actualConf, repo.team)))

  def actualConf(implicit req: Request[_]) =
    if (req.queryString.exists(_._1 == "preview"))
      conf.copy(preview = true)
    else
      conf
}
