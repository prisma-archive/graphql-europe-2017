package repo

import java.time.{LocalDate, Month}
import javax.inject.{Inject, Singleton}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import play.api.Configuration
import views._

@Singleton
class ContentRepo @Inject() (config: Configuration) {
  val baseUrl = config.getString("graphqlEurope.canonicalUrl").get + "/assets/image"

  val conf = config.underlying.as[Config]("graphqlEurope")

  def cleanupText(s: String) = s.stripMargin.replaceAll("\r\n", "\n")

  val sponsors = List(
    Sponsor(
      name = "Honeypot",
      sponsorType = SponsorType.Organiser,
      url = "https://www.honeypot.io",
      logoUrl = s"$baseUrl/sponsor/honeypot.svg",
      description = Some(
        "Honeypot is a developer-focused job platform, on a mission to get every developer a great job. " +
        "We believe developers should have all the information they need to choose a job they love: whether " +
        "that’s based on a cutting-edge tech stack, an inspiring team or just good old-fashioned salary. In our " +
        "world, that means no more spam and empty promises from headhunters, no more sending the same application to " +
        "multiple companies - just one profile and the choice to receive  honest offers related to your job " +
        "preferences direct from companies."),
      twitter = Some("honeypotio"),
      github = Some("honeypotio")),
    Sponsor(
      name = "Graphcool",
      sponsorType = SponsorType.Organiser,
      url = "https://www.graph.cool",
      logoUrl = s"$baseUrl/sponsor/graphcool.svg",
      description = Some(
        "Graphcool is a powerful backend-as-a-service platform for GraphQL used by companies " +
        "like Twitter to quickly iterate on new products. In just 5 minutes you can setup a complete " +
        "backend that works with frontend frameworks such as Angular, Apollo, Relay, React & React Native. " +
        "Includes built-in integrations for popular services."),
      twitter = Some("graphcool"),
      github = Some("graphcool")),
    Sponsor(
      name = "commercetools",
      sponsorType = SponsorType.Gold,
      url = "https://commercetools.com",
      logoUrl = s"$baseUrl/sponsor/commercetools.svg",
      twitter = Some("commercetools"),
      github = Some("commercetools"),
      description = Some(cleanupText(
        """commercetools is a next generation software technology company that offers a true cloud commerce platform,
          |providing the building blocks for the new digital commerce age.  Our leading-edge API approach helps retailers
          |create brand value by empowering commerce teams to design unique and engaging digital commerce experiences
          |everywhere – today and in the future.  Our agile, componentized architecture improves profitability by
          |significantly reducing development time and resources required to migrate to modern commerce technology
          |and meet new customer demands.
          |
          |Visit [www.commercetools.com](https://commercetools.com) for more information.""")))
  )

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
      github = None,
      stub = true
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
      photoUrl = Some(s"$baseUrl/team/johanna-new.png"),
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
      description = Some(
        "Dajana is GraphQL-Europe's advisor. She is conference manager of GOTO Berlin. " +
        "Dajana has a background in system electronics, pedagogy and psychology and worked on " +
        "different international teams to create inclusive and diverse IT events."),
      twitter = Some("dajanaguenther"),
      github = Some("dajana"))
  )

  val tickets = List(
    Ticket(
      name = "Early Bird",
      price = "149 €",
      availableUntil = LocalDate.of(2017, Month.APRIL, 1),
      availableUntilText = "Available until end of March",
      url = conf.ticketsUrl,
      available = true),
    Ticket(
      name = "Regular",
      price = "189 €",
      availableUntil = LocalDate.of(2017, Month.MAY, 7),
      availableUntilText = "Available until beginning of May",
      url = conf.ticketsUrl,
      available = true),
    Ticket(
      name = "Late Bird",
      price = "199 €",
      availableUntil = LocalDate.of(2017, Month.MAY, 21),
      availableUntilText = "Last chance to get a ticket",
      url = conf.ticketsUrl,
      available = false)
  )

  val conferences = List(
    Conference(
      name = "GraphQL-Europe",
      edition = Edition.Berlin2017,
      year = 2017,
      venue = None,
      dateStart = Some(LocalDate.of(2017, Month.MAY, 21)),
      dateEnd = Some(LocalDate.of(2017, Month.MAY, 21)),
      speakers = speakers,
      sponsors = sponsors,
      team = team,
      tickets = tickets,
      url = "https://graphql-europe.org")
  )

  val conferencesByEdition = conferences.groupBy(_.edition).mapValues(_.head)
}
