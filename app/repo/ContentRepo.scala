package repo

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import views.{Conference, Speaker, TeamMember, TeamSection}

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

  val team = List(
    TeamMember(
      name = "Oleg Ilyenko",
      photoUrl = Some(s"$baseUrl/team/oleg.png"),
      teamSection = TeamSection.Core,
      description = Some(
        "Oleg is a passionate software engineer and speaker who loves innovative ideas and technology, " +
        "challenging problems and working on things that help other people. " +
        "Oleg is an author of Sangria - a scala GraphQL implementation."),
      twitter = Some("easyangel"),
      github = Some("OlegIlyenko")),
    TeamMember(
      name = "Johannes Schickling",
      photoUrl = Some(s"$baseUrl/team/johannes.png"),
      teamSection = TeamSection.Core,
      description = Some(
        "Johannes is a Berlin/SF-based entrepreneur and founder of Graphcool, " +
        "a flexible backend platform combining GraphQL + AWS Lambda. He previously " +
        "built and sold the VR company \"Optonaut\". Johannes studied computer science at KIT, " +
        "Germany and loves cutting-edge mobile/web tech-nologies."),
      twitter = Some("_schickling"),
      github = Some("schickling")),
    TeamMember(
      name = "Emma Tracey",
      photoUrl = Some(s"$baseUrl/team/emma.png"),
      teamSection = TeamSection.Core,
      description = Some(
        "Emma is co-founder of Honeypot, a developer-focused job platform. She loves bringing " +
        "the community together and previously was part of the organizing team of RustFest."),
      twitter = None,
      github = None),
    TeamMember(
      name = "Artyom Chelbayev",
      photoUrl = Some(s"$baseUrl/team/artyom.png"),
      teamSection = TeamSection.Core,
      description = Some(
        "Artyom is bringing years of business experience from advertising, consumer, " +
        "and developer technology startups to Graphcool, a flexible backend platform " +
        "combining GraphQL + AWS Lambda. On any given day you will find him learning " +
        "about new technologies, searching for good music, or trying his best to pretend " +
        "he can be on the same footing with the programmers."),
      twitter = None,
      github = Some("artyomc")),
    TeamMember(
      name = "Johanna Dahlroos",
      photoUrl = Some(s"$baseUrl/team/johanna.png"),
      teamSection = TeamSection.Core,
      description = Some(
        "Johanna is Honeypot’s UI/UX Designer. A Finnish dog-lover with passion for " +
        "fine art, architecture and old black and white photos. She worked as a painter before moving to Berlin."),
      twitter = Some("batjohe"),
      github = Some("Batjohe")),
    TeamMember(
      name = "Dajana Günther",
      photoUrl = Some(s"$baseUrl/team/dajana.png"),
      teamSection = TeamSection.SpecialThanks,
      description = Some("Dajana is GraphQL-Europe's advisor."),
      twitter = Some("dajanaguenther"),
      github = Some("dajana"))
  )

  val conference = Conference(
    name = "GraphQL-Europe",
    venue = None,
    dateStart = None,
    dateEnd = None,
    speakers = speakers,
    team = team,
    url = "https://graphql-europe.org")
}
