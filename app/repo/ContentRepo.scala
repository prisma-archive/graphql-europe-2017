package repo

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import views._

@Singleton
class ContentRepo @Inject() (config: Configuration) {
  val baseUrl = config.getString("graphqlEurope.canonicalUrl").get + "/assets/image"

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
          |everywhere – today and in the future.  Our agile, componentized architecture improves profitability by significantly
          |reducing development time and resources required to migrate to modern commerce technology and meet new customer demands.
          |
          |The innovative platform design enables commerce possibilities for the future by offering the option to either use
          |the platform's entire set of features or deploy individual services, á la carte over time.  This state-of-the-art
          |architecture is the perfect starting point for customized microservices, enabling retailers to significantly reduce
          |time-to-market for innovative commerce functionalities.
          |
          |With offices in Germany and the United States, B2C and B2B companies from across the globe including
          |well-known brands in fashion, E-Food, and DIY retail trust commercetools to power their digital commerce business.
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

  val conference = Conference(
    name = "GraphQL-Europe",
    venue = None,
    dateStart = None,
    dateEnd = None,
    speakers = speakers,
    sponsors = sponsors,
    team = team,
    url = "https://graphql-europe.org")
}
