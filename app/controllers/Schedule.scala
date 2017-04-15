package controllers

import javax.inject.Inject

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.Configuration
import play.api.mvc._
import repo.ContentRepo
import views.Config

class Schedule @Inject()(config: Configuration, repo: ContentRepo) extends Controller {
  val baseUrl = config.getString("graphqlEurope.canonicalUrl").get + "/image"

  val conf = config.underlying.as[Config]("graphqlEurope")

  def index = Action { implicit req ⇒
    Ok(views.html.schedule.schedule(actualConf, repo))
  }
  
  def speakers = Action { implicit req ⇒
    Ok(views.html.schedule.speakers(actualConf, repo))
  }

  def speaker(slug: String) = Action { implicit req ⇒
    repo.speakerBySlug(slug).fold(NotFound(views.html.notFound(actualConf)))(s ⇒ Ok(views.html.schedule.speaker(actualConf, s, repo)))
  }

  def talk(slug: String) = Action { implicit req ⇒
    repo.talkBySlug(slug).fold(NotFound(views.html.notFound(actualConf)))(t ⇒ Ok(views.html.schedule.talk(actualConf, t, repo)))
  }

  def actualConf(implicit req: Request[_]): Config =
    if (req.queryString.exists(_._1 == "preview"))
      conf.copy(preview = true)
    else
      conf
}
