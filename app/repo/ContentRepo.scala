package repo

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import views.{Conference, Speaker}

@Singleton
class ContentRepo @Inject() (config: Configuration) {
  val baseUrl = config.getString("graphqlEurope.canonicalUrl").get + "/assets/image"

  val speakers = List(
    Speaker(
      name = "Lee Byron",
      photoUrl = Some(s"$baseUrl/speakers/lee-byron.jpg"),
      talkTitle = Some("Keynote"),
      company = Some("Facebook, GraphQL Co-creator"),
      twitter = Some("leeb"),
      github = Some("leebyron")
    ),

    Speaker(
      name = "Sashko Stubailo",
      photoUrl = Some(s"$baseUrl/speakers/sashko-stubailo.jpg"),
      talkTitle = None,
      company = Some("Apollo"),
      twitter = Some("stubailo"),
      github = Some("stubailo")
    ),

    Speaker(
      name = "This could be you!",
      photoUrl = Some(s"$baseUrl/speakers/you.png"),
      talkTitle = Some("Register today"),
      company = None,
      twitter = None,
      github = None
    )
  )

  val conference = Conference(
    name = "GraphQL-Europe",
    venue = None,
    dateStart = None,
    dateEnd = None,
    speakers = speakers,
    url = "https://graphql-europe.org")
}
