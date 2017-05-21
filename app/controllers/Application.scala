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

  def index = Action { implicit req ⇒
    val speakers = repo.speakers.sortBy(_.slug).filterNot(_.stub)
    
    Ok(views.html.index(actualConf, speakers, repo.sponsors, repo.tickets, repo.venue, repo: ContentRepo, req.queryString.contains("dark"), req.queryString.contains("topLogo")))
  }

  def qa = Action { implicit req ⇒
    Ok(views.html.qa(actualConf))
  }

  def codeOfConduct = Action(implicit req ⇒ Ok(views.html.codeOfConduct(actualConf)))
  def imprint = Action(implicit req ⇒ Ok(views.html.imprint(actualConf)))
  def team = Action(implicit req ⇒ Ok(views.html.team(actualConf, repo.team)))
  def sponsors = Action(implicit req ⇒ Ok(views.html.sponsors(actualConf, repo.sponsors)))
  def location = Action(implicit req ⇒ Ok(views.html.location(actualConf, repo.venue)))

  def actualConf(implicit req: Request[_]): Config = {
    val withPath = conf.copy(path = req.path)

    if (req.queryString.exists(_._1 == "preview"))
      withPath.copy(preview = true)
    else
      withPath
  }
}
