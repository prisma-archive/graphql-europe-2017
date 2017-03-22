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
          |Visit [www.commercetools.com](https://commercetools.com) for more information."""))),
    Sponsor(
      name = "Facebook",
      sponsorType = SponsorType.Partner,
      url = "https://www.facebook.com",
      logoUrl = s"$baseUrl/sponsor/facebook.svg",
      twitter = Some("facebook"),
      github = Some("facebook"),
      description = None),
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
      github = Some("graphcool"))
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
      photoUrl = Some(s"$baseUrl/team/oleg.jpg"),
      teamSection = TeamSection.Core,
      description = Some(
        "Oleg is a passionate software engineer and speaker who loves innovative ideas and technology, " +
        "challenging problems and working on things that help other people. " +
        "Oleg is an author of Sangria - a scala GraphQL implementation."),
      twitter = Some("easyangel"),
      github = Some("OlegIlyenko")),
    TeamMember(
      name = "Johannes Schickling",
      photoUrl = Some(s"$baseUrl/team/johannes.jpg"),
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
      photoUrl = Some(s"$baseUrl/team/emma.jpg"),
      teamSection = TeamSection.Core,
      description = Some(
        "Emma is co-founder of Honeypot, a developer-focused job platform. She loves bringing " +
        "the community together and previously was part of the organizing team of RustFest."),
      twitter = None,
      github = None),
    TeamMember(
      name = "Artyom Chelbayev",
      photoUrl = Some(s"$baseUrl/team/artyom.jpg"),
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
      photoUrl = Some(s"$baseUrl/team/johanna-new.jpg"),
      teamSection = TeamSection.Core,
      description = Some(
        "Johanna is Honeypot’s UI/UX Designer. A Finnish dog-lover with passion for " +
        "fine art, architecture and old black and white photos. She worked as a painter before moving to Berlin."),
      twitter = Some("batjohe"),
      github = Some("Batjohe")),
    TeamMember(
      name = "Dajana Günther",
      photoUrl = Some(s"$baseUrl/team/dajana.jpg"),
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
      price = "179 €",
      availableUntil = LocalDate.of(2017, Month.APRIL, 1),
      availableUntilText = "Limited number available",
      url = conf.ticketsUrl,
      soldOut = true,
      available = false),
    Ticket(
      name = "Regular",
      price = "229 €",
      availableUntil = LocalDate.of(2017, Month.MAY, 1),
      availableUntilText = "Available until beginning of May",
      url = conf.ticketsUrl,
      soldOut = false,
      available = true),
    Ticket(
      name = "Late Bird",
      price = "309 €",
      availableUntil = LocalDate.of(2017, Month.MAY, 21),
      availableUntilText = "Last chance to get a ticket",
      url = conf.ticketsUrl,
      soldOut = false,
      available = false)
  )

  val venue = Venue(
    name = "nHow Berlin",
    url = "https://www.nhow-berlin.com/en",
    phone = "+49 30 290 299 290",
    directions = List(
      Direction(DirectionType.Airport, "Tegel Airport", "From **Tegel Airport** take the Bus TXL towards Alexanderplatz. Change at Berlin Main Train Station and take the S5 towards Strausberg or the S75 towards Wartenberg. Exit at the Warschauer Straße station."),
      Direction(DirectionType.Airport, "Schönefeld Airport", "From **Schönefeld Airport** enter Flughafen Berlin-Schönefeld train station and take S9 towards Pankow. Exit at the Ostkreuz train station and transfer to S5 towards Westkreuz. Exit at Warschauer Straße. Trains leave every 20 minutes and cost 2,80€ per person."),
      Direction(DirectionType.TrainStation, "Main Train Station", "From Berlin **Main Train Station** you can catch a train (S5 towards Strausberg, S7 towards Ahrensfelde or S75 towards Wartenberg) to Warschauer Straße. Trains leave every 10 min and cost 2,10€ per person. "),
      Direction(DirectionType.TrainStation, "Ostbahnhof Train Station", "From **Ostbahnhof Train Station** you can catch a train (S3 towards Friedrichshagen, S7 towards Ahrensfelde or S75 towards Wartenberg) to Warschauer Straße. Trains leave every 10 min and cost 2,10€ per person. "),
      Direction(DirectionType.Car, "From North",
        "Coming from A10 Berliner Ring:  " +
        "Take Exit 35 Pankow onto A114/B109 towards Prenzlauer Berg.  " +
        "Turn left at B96a/Danziger Straße.  " +
        "Follow Danziger Straße and turn left into Stralauer Allee."),
      Direction(DirectionType.Car, "From South",
        "Coming from A100:  " +
        "Take Exit 23 Britzer Damm onto B179.  " +
        "Turn right at Hermannplatz to continue on B179.  " +
        "At the roundabout, take the first exit onto Skalitzer Straße.  " +
        "Continue on Skalitzer Straße, cross the bridge then turn right into Stralauer Allee."),
      Direction(DirectionType.Car, "From West",
        "Coming fom A100:  " +
        "Take Exit 20 Tempelhofer Damm onto B96.  " +
        "Turn right at B179 Tempelhofer Ufer onto B179.  " +
        "At the roundabout take the second exit into Skalitzer Straße.  " +
        "Cross the bridge, then turn right into Stralauer Allee."),
      Direction(DirectionType.Car, "From East",
        "Coming from B10 Berliner Ring:  " +
        "Take Exit B1/B5 towards Hellersdorf/Zentrum and follow that road for 20 km.  " +
        "Turn left into B96a/ Warschauer Straße.  " +
        "Follow Warschauer Straße before turning left onto Stralauer Allee.")),
    address = Address(
      country = "Germany",
      city = "Berlin",
      zipCode = "10245",
      streetName = "Stralauer Allee",
      houseNumber = "3",
      latitude = 52.501159D,
      longitude = 13.451280D))

  val conferences = List(
    Conference(
      name = "GraphQL-Europe",
      edition = Edition.Berlin2017,
      tagLine = "Join Europe’s first GraphQL conference",
      year = 2017,
      venue = Some(venue),
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
