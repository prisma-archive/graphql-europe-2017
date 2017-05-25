package repo

import java.time.{Duration, LocalDate, LocalTime, Month}
import javax.inject.{Inject, Singleton}

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.Configuration
import views._

@Singleton
class ContentRepo @Inject() (config: Configuration) {
  val baseUrl = config.getString("graphqlEurope.canonicalUrl").get

  val conf = config.underlying.as[Config]("graphqlEurope")

  def cleanupText(s: String) = s.stripMargin.replaceAll("\r\n", "\n")

  def url(path: String) = baseUrl + path
  def assetUrl(path: String) = url("/assets/image" + path)

  val sponsors = List(
    Sponsor(
      name = "commercetools",
      sponsorType = SponsorType.Gold,
      url = "https://commercetools.com",
      logoUrl = assetUrl("/sponsor/commercetools.svg"),
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
      name = "Neo4j",
      sponsorType = SponsorType.Gold,
      url = "https://neo4j.com",
      logoUrl = assetUrl("/sponsor/neo4j.svg"),
      twitter = Some("neo4j"),
      github = Some("neo4j"),
      description = Some(cleanupText(
        """Neo4j is an open-source, native graph database that leverages data and its relationships.
          |When GraphQL makes you realize that your domain is a graph, then Neo4j can store your application data graph without resolver or data mapping pains.
          |Come by our booth during the conference to see how you can build friction-free GraphQL APIs backed by a scalable graph database -we’d love to hear your feedback.
          |
          |We would also love to invite you to hack on data representing the GraphQL community from Twitter, StackOverflow, Meetup and GitHub."""))),
    Sponsor(
      name = "Zalando",
      sponsorType = SponsorType.Gold,
      url = "https://tech.zalando.com",
      logoUrl = assetUrl("/sponsor/zalando.svg"),
      twitter = Some("ZalandoTech"),
      github = Some("zalando"),
      description = Some(cleanupText(
        """Zalando is Europe's leading multi-service online fashion platform that provides
          |fashion as a service. We make it our mission to predict the infinite points of
          |interaction between fashion and people - and make them possible using cutting
          |edge, open-source technologies.
          |
          |Join our fast-growing team here: [https://tech.zalando.com](https://tech.zalando.com/)"""))),
    Sponsor(
      name = "Intuit",
      sponsorType = SponsorType.Silver,
      url = "https://www.intuit.com",
      logoUrl = assetUrl("/sponsor/intuit.svg"),
      twitter = Some("Intuit"),
      github = Some("intuit"),
      description = None),
    Sponsor(
      name = "OK GROW!",
      sponsorType = SponsorType.Silver,
      url = "https://www.okgrow.com",
      logoUrl = assetUrl("/sponsor/okgrow.svg"),
      twitter = Some("ok_grow"),
      github = Some("okgrow"),
      description = None),
    Sponsor(
      name = "N26",
      sponsorType = SponsorType.Bronze,
      url = "https://n26.com",
      logoUrl = assetUrl("/sponsor/n26.png"),
      twitter = Some("n26"),
      github = None,
      description = None),
    Sponsor(
      name = "GitHub",
      sponsorType = SponsorType.Opportunity,
      url = "https://github.com",
      logoUrl = assetUrl("/sponsor/github.svg"),
      twitter = Some("github"),
      github = Some("github"),
      description = None),
    Sponsor(
      name = "Facebook",
      sponsorType = SponsorType.Partner,
      url = "https://www.facebook.com",
      logoUrl = assetUrl("/sponsor/facebook.svg"),
      twitter = Some("facebook"),
      github = Some("facebook"),
      description = None),
    Sponsor(
      name = "Apollo",
      sponsorType = SponsorType.Partner,
      url = "http://www.apollodata.com",
      logoUrl = assetUrl("/sponsor/apollo.svg"),
      twitter = Some("apollographql"),
      github = Some("apollographql"),
      description = None),
    Sponsor(
      name = "Honeypot",
      sponsorType = SponsorType.Organiser,
      url = "https://www.honeypot.io",
      logoUrl = assetUrl("/sponsor/honeypot.svg"),
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
      logoUrl = assetUrl("/sponsor/graphcool.svg"),
      description = Some(
        "Graphcool is a powerful backend-as-a-service platform for GraphQL used by companies " +
        "like Twitter to quickly iterate on new products. In just 5 minutes you can setup a complete " +
        "backend that works with frontend frameworks such as Angular, Apollo, Relay, React & React Native. " +
        "Includes built-in integrations for popular services."),
      twitter = Some("graphcool"),
      github = Some("graphcool"))
  )

  object speaker {
    val AlisonJohnston = Speaker(
      name = "Alison Johnston",
      photoUrl = Some(assetUrl("/speakers/alison-johnston.jpg")),
      company = Some("Cardano"),
      twitter = Some("AlizonJohnston"),
      github = Some("chookie"),
      description = Some(cleanupText(
        """Living in London, at the heart of the silicon roundabout allows me to feed my addiction for software development.  It is my creative outlet and is what gets me out of bed in the morning (most of the time) and into the lush green fields at my new role with Cardano.  It’s one of those chance of a lifetime jobs to take an established company with a whole new business, so more like a startup, and lead the leap onto the web using my experience in many areas from physics, to my own tech startup, consulting, to software for the arts, training and investment banking.
          |
          |Please wave hello if you see me in the London Lord Mayors parade as a Freeman of the Worshipful Company of Information Technologists or escaping it all scuba diving around the globe."""
      )))

    val LeeByron = Speaker(
      name = "Lee Byron",
      photoUrl = Some(assetUrl("/speakers/lee-byron.jpg")),
      company = Some("Facebook, GraphQL co-creator"),
      twitter = Some("leeb"),
      github = Some("leebyron"),
      description = None) // TODO: description

    val SashkoStubailo = Speaker(
      name = "Sashko Stubailo",
      photoUrl = Some(assetUrl("/speakers/sashko-stubailo.jpg")),
      company = Some("Meteor / Apollo"),
      twitter = Some("stubailo"),
      github = Some("stubailo"),
      description = Some(
        "Sashko has been working on open source developer tools for the last 4 years, " +
        "and most recently has been leading the open source projects from the Apollo team, " +
        "after writing the very first versions of Apollo Client. Previously, he has worked on " +
        "JavaScript build tooling and reactive data visualization."))

    val JonasHelfer = Speaker(
      name = "Jonas Helfer",
      photoUrl = Some(assetUrl("/speakers/jonas-helfer.jpg")),
      company = Some("Meteor / Apollo"),
      twitter = Some("helferjs"),
      github = Some("helfer"),
      description = Some(
        "Jonas is the tech lead for Apollo Client and a maintainer of several other popular " +
        "open source GraphQL libraries like graphql-server and graphql-tools. He is passionate " +
        "about creating a great developer experience around building modern web applications."))

    val TasveerSingh = Speaker(
      name = "Tasveer Singh",
      photoUrl = Some(assetUrl("/speakers/tasveer-singh.jpg")),
      company = Some("Mainframe"),
      twitter = Some("tazsingh"),
      github = Some("tazsingh"),
      description = Some(
        "Taz is a software architect with over 15 years of experience in various aspects of " +
        "computing. He's originally from Toronto where he organized Toronto JavaScript, one " +
        "of the largest developer communities in North America, in addition to founding two " +
        "startups and working with many others. He recently moved to London and has been working " +
        "in the React and Elixir ecosystems. When he’s not tinkering with a computer, he can be " +
        "found with his car at the local race track or rock climbing gym."))

    val JakubRiedl = Speaker(
      name = "Jakub Riedl",
      photoUrl = Some(assetUrl("/speakers/jakub-riedl.jpg")),
      company = Some("iflix"),
      twitter = Some("jakubriedl"),
      github = None,
      description = Some(
        "Jakub is from Prague and computers are his passion from early age. For three years he " +
        "worked as a System Engineer and built large server rooms and got experience with " +
        "networking and infrastructure. Later he started to focus more on application development " +
        "mostly backend services, APIs and overall system architecture. Over the years he has build " +
        "multiple large scale rest APIs and some GraphQL based as well. Currently he works as a Senior " +
        "Software Engineer and leads a project with new GraphQL API Gateway in iflix. " +
        "Where he is solving a lot of interesting issues and where he can benefit from combined knowledge " +
        "and experience with networking, software architecture patterns and API design."))

    val NettoFarah = Speaker(
      name = "Netto Farah",
      photoUrl = Some(assetUrl("/speakers/netto-farah.jpg")),
      company = Some("Segment"),
      twitter = Some("nettofarah"),
      github = Some("nettofarah"),
      description = Some(
        "Netto Farah is a Lead Fullstack Engineer at Segment. A lover of everything Web and Javascript, " +
        "Netto is always looking for creative ways to build great user experiences on the web."))

    val JohannesSchickling = Speaker(
      name = "Johannes Schickling",
      photoUrl = Some(assetUrl("/speakers/johannes-schickling.jpg")),
      company = Some("Graphcool"),
      twitter = Some("_schickling"),
      github = Some("schickling"),
      description = Some(
        "Johannes is a Berlin/SF-based entrepreneur and founder of Graphcool, " +
        "a flexible backend platform combining GraphQL + AWS Lambda. He previously " +
        "built and sold the VR company \"Optonaut\". Johannes studied computer science at KIT, " +
        "Germany and loves cutting-edge mobile/web tech-nologies."))

    val MichaelHunger = Speaker(
      name = "Michael Hunger",
      photoUrl = Some(assetUrl("/speakers/michael-hunger.jpg")),
      company = Some("Neo4j"),
      twitter = Some("mesirii"),
      github = Some("jexp"),
      description = Some(cleanupText(
        """Michael Hunger has been passionate about software development for a very long time.
          |
          |For the last few years he has been working with Neo Technology on the open source Neo4j graph database filling many roles. As caretaker of the Neo4j community and ecosystem he especially loves to work with graph-related projects, users and contributors.
          |
          |As a developer Michael enjoys many aspects of programming languages, learning new things every day, participating in exciting and ambitious open source projects and contributing and writing software related books and articles.""")))

    val MinaSmart = Speaker(
      name = "Mina Smart",
      photoUrl = Some(assetUrl("/speakers/mina-smart.jpg")),
      company = Some("Shopify"),
      twitter = Some("frabnicate"),
      github = Some("minasmart"),
      description = Some(
        "Mina is the Storefront API Lead at Shopify, where she oversees the " +
        "development of the Storefront API, and its connected SDKs. Outside " +
        "of work, you can catch Mina skating with Toronto Roller Derby’s " +
        "Gore-Gore Rollergirls."))

    val TommyLillehagen = Speaker(
      name = "Tommy Lillehagen",
      photoUrl = Some(assetUrl("/speakers/tommy-lillehagen.jpg")),
      company = Some("Hudl"),
      twitter = Some("tommylil"),
      github = Some("tlil"),
      description = Some(
        "Tommy is an Engineering Director at Hudl where he introduced GraphQL " +
        "to the product team early on. He has been contributing to the GraphQL .NET project since the end of 2015."))

    val BrooksSwinnerton = Speaker(
      name = "Brooks Swinnerton",
      photoUrl = Some(assetUrl("/speakers/brooks-swinnerton.jpg")),
      company = Some("GitHub"),
      twitter = Some("bswinnerton"),
      github = Some("bswinnerton"),
      description = Some(
        "Brooks Swinnerton is a Platform Engineer at GitHub where he " +
        "works on their REST API, webhooks and GraphQL API."))

    val DanielSchafer = Speaker(
      name = "Daniel Schafer",
      photoUrl = Some(assetUrl("/speakers/daniel-schafer.jpg")),
      company = Some("Facebook, GraphQL co-creator"),
      twitter = Some("dlschafer"),
      github = Some("dschafer"),
      description = Some(
        "Dan Schafer is a software engineer at Facebook and a co-creator of GraphQL. " +
        "He worked on the implementations of both GraphQL Mutations and Subscriptions, " +
        "and built the original Android and iOS client libraries for them. Currently, " +
        "he's a tech lead on the Android Product Layer at Facebook, focused on using " +
        "GraphQL to develop a compelling and cohesive Android SDK."))

    val BjoernRochel = Speaker(
      name = "Björn Rochel",
      photoUrl = Some(assetUrl("/speakers/bjoern-rochel.jpg")),
      company = Some("XING AG"),
      twitter = Some("bjoernrochel"),
      github = Some("bjro"),
      description = Some(
        "Björn is the project lead of XING One, XINGs internal GraphQL project. " +
        "He loves to build software with people for people and is especially fascinated " +
        "by the organisational leaps that are possible if you combine passionate people, " +
        "useful technology and lean processes together. "))

    val ChadFowler = Speaker(
      name = "Chad Fowler",
      photoUrl = Some(assetUrl("/speakers/chad-fowler.jpg")),
      company = Some("Microsoft/BlueYard"),
      twitter = Some("chadfowler"),
      github = Some("chad"),
      description = Some("Venture Partner with @BlueYard. Divisional CTO with @Microsoft. Musician."))
  }

  val speakers = List(
    speaker.AlisonJohnston,
    speaker.LeeByron,
    speaker.SashkoStubailo,
    speaker.JonasHelfer,
    speaker.TasveerSingh,
    speaker.JakubRiedl,
    speaker.NettoFarah,
    speaker.JohannesSchickling,
    speaker.MichaelHunger,
    speaker.MinaSmart,
    speaker.TommyLillehagen,
    speaker.BrooksSwinnerton,
    speaker.DanielSchafer,
    speaker.BjoernRochel,
    speaker.ChadFowler)

  val schedule = List[ScheduleEntry](
    Registration(LocalTime.of(8, 0), LocalTime.of(9, 30), Duration.ofHours(1).plusMinutes(30)),
    Talk(
      title = "Opening",
      description = "Opening of the GraphQL-Europe conference",
      cardUrl = assetUrl("/share-graphql-europe.png"),
      speakers = List(speaker.ChadFowler),
      format = TalkFormat.Special,
      startTime = LocalTime.of(9, 30),
      endTime = LocalTime.of(9, 40),
      duration = Duration.ofMinutes(10)),
    Talk(
      title = "GraphQL: Evolution or Revolution?",
      description = cleanupText(
        """In this talk I will present a thorough comparison between SOAP, WSDL, oData, REST(ful), Falcor and GraphQL. I will show a small code sample for each of the technologies, present how/where they are being used, and compare them to GraphQL on a number of metrics:
          |
          |* Ease-of-use
          |* Type-safety
          |* Documentation
          |* Standardization
          |* Caching
          |* Efficiency
          |* Adoption
          |* (maybe more?)
          |
          |# Motivation
          |
          |Sometimes it’s hard to see through all the hype and do a calm and objective assessment of a new technology. Particularly in the space of API technologies there are so many different acronyms and buzzwords that it’s easy to get confused: SOAP, RPC, WSDL, oData, REST, RESTful, Swagger, Open API, RAML, JSON API, Falcor, not to mention Firebase and Parse which are not the same, but also in that space. It takes a lot of effort to see the forrest for all the trees. One could easily spend several days researching and comparing the different options, but most people don’t have time for that.
          |
          |I hope that my talk will provide people with the basic knowledge they need to have in order to choose the right tool for each project. Anyone excited about GraphQL will come away from this talk with a list of good reasons for their choice, just in case they encounter a professional curmudgeon who tells them that GraphQL is just oData plus hype.
          |
          |# The twist
          |
          |Many people like to say that GraphQL is a replacement for REST. So a revolution of sorts, not evolution. But what if I told you that GraphQL actually meets all the architectural constraints of REST laid out in Roy Fielding’s PhD dissertation?"""),
      shortDescription = Some(cleanupText(
        """In this talk I will present a thorough comparison between SOAP, WSDL, oData, REST(ful), Falcor and GraphQL. I will show a small code sample for each of the technologies, present how/where they are being used, and compare them to GraphQL on a number of metrics:
          |
          |* Ease-of-use
          |* Type-safety
          |* Documentation
          |* Standardization
          |* Caching
          |* Efficiency
          |* Adoption""")),
      cardUrl = assetUrl("/talks/jonas-helfer.png"),
      speakers = List(speaker.JonasHelfer),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(9, 40),
      endTime = LocalTime.of(10, 10),
      duration = Duration.ofMinutes(30)),
    Talk(
      title = "Adding GraphQL to your existing architecture",
      description =
        "If you’re a product developer in today’s world, you have to wear a lot of hats. " +
        "You signed up to create great experiences, but you're also spending a ton of time " +
        "writing complex data loading, state management, and API gateway code. You’ve heard " +
        "that GraphQL can help by enabling a flexible and self-documenting API on top of your " +
        "data, but it can seem like a big investment just to try it out. I’m going to talk " +
        "about how you can add GraphQL to your existing architecture without having to change " +
        "your existing technology investments.",
      cardUrl = assetUrl("/talks/sashko-stubailo.png"),
      slidesUrl = Some("https://www.slideshare.net/sashko1/adding-graphql-to-your-existing-architecture"),
      speakers = List(speaker.SashkoStubailo),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(10, 10),
      endTime = LocalTime.of(10, 40),
      duration = Duration.ofMinutes(30)),
    Break(LocalTime.of(10, 40), LocalTime.of(11, 0), Duration.ofMinutes(20)),
    Talk(
      title = "Realtime GraphQL from the Trenches",
      description = cleanupText(
        """We can learn a lot from organizations such as Facebook and Apollo in terms of how to work with Realtime GraphQL but how does it work for most of us? What can we learn from a product company iterating fast on new features at scale? And perhaps most importantly, did GraphQL work for Mainframe opposed to going with some other technology?
          |
          |Taz will provide an overview of Mainframe’s client-side stack and how they’ve tackled cyclic data requirements while ensuring that events from the server aren’t dropped while the subscription is being set up, in addition to the Erlang based server-side stack and how it is optimized for often repeated fragment fetching while handling a large volume of subscriptions at scale."""),
      cardUrl = assetUrl("/talks/tasveer-singh.png"),
      speakers = List(speaker.TasveerSingh),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(11, 0),
      endTime = LocalTime.of(11, 30),
      duration = Duration.ofMinutes(30)),
    Talk(
      title = "Building global GraphQL API distribution",
      description = cleanupText(
        """In iflix we are delivering video on demand to emerging markets on our mission to redefine television for 1 billion people. But it has a lot of technical challenges, one of them being to create a fast, reliable and flexible API which will be accessible from Africa over Middle East to South East Asia.
          |
          |Imagine the average user having a low budget phone with slow internet connection wanting to watch his favourite TV shows. Of course the video is static and delivered via CDN but what about API which is crucial for smooth content navigation. And imagine that your closest datacenter reachable from Africa is in Frankfurt. There are tons of network and distribution issues that we needed to solve to bring fast user experience. I will talk about our approach and what we learned on the way to solve this."""),
      cardUrl = assetUrl("/talks/jakub-riedl.png"),
      slidesUrl = Some("https://www.slideshare.net/JakubRiedl/graphql-distribution"),
      speakers = List(speaker.JakubRiedl),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(11, 30),
      endTime = LocalTime.of(12, 0),
      duration = Duration.ofMinutes(30)),
    Lunch(LocalTime.of(12, 0), LocalTime.of(13, 55), Duration.ofHours(2)),
    Talk(
      title = "Sponsor Introduction - GraphQL at commercetools",
      description = "",
      cardUrl = assetUrl("/share-graphql-europe.png"),
      speakers = Nil,
      format = TalkFormat.Special,
      startTime = LocalTime.of(13, 55),
      endTime = LocalTime.of(14, 0),
      duration = Duration.ofMinutes(5)),
    Talk(
      title = "Five Years of Client GraphQL Infrastructure",
      description = cleanupText(
        """When Facebook first started using GraphQL in 2012, “Client GraphQL Infrastructure” meant smashing strings together for the query and a simple JSON parser for the response. Since then, Facebook has developed app-wide SDKs, simplifying how client developers build the entire client based on core principles of GraphQL. From client caches to pagination abstractions, from cross-platform toolchains to generated models, Facebook’s client SDKs have evolved over the last five years to support hundreds of developers and thousands of queries across dozens of apps, and the evolution of these clients has informed the evolution of GraphQL itself.
          |
          |This talk will dive into lessons learned from those developments. What abstractions worked, and which ones are now regrettable? How did the evolution of client abstractions inform the development of GraphQL itself? When beginning to use GraphQL on a new client, what are the best practices to ensure developers move as swiftly as possible?"""),
      cardUrl = assetUrl("/talks/daniel-schafer.png"),
      speakers = List(speaker.DanielSchafer),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(14, 0),
      endTime = LocalTime.of(14, 30),
      duration = Duration.ofMinutes(30)),
    Talk(
      title = "Fighting legacy codebases with GraphQL and Rails",
      description = cleanupText(
        """GraphQL is one of the hottest technologies of the past year or two. Still, very little is talked about GraphQL outside of the realm of front-end applications.
          |
          |We used a different approach at IFTTT and applied GraphQL as an integration layer for different backends and client apps.
          |
          |This talk goes beyond the basic configuration of a GraphQL endpoint with Rails. I’ll cover topics such ActiveRecord Query optimization, performance monitoring, batching and share some of the challenges we ran into while building a GraphQL API that serves over 10 thousand queries per minute."""),
      cardUrl = assetUrl("/talks/netto-farah.png"),
      slidesUrl = Some("https://speakerdeck.com/nettofarah/rescuing-legacy-codebases-with-graphql-1"),
      speakers = List(speaker.NettoFarah),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(14, 30),
      endTime = LocalTime.of(15, 0),
      duration = Duration.ofMinutes(30)),
    Break(LocalTime.of(15, 0), LocalTime.of(15, 20), Duration.ofMinutes(20)),
    Talk(
      title = "Launching GitHub's Public GraphQL API",
      description = cleanupText(
        """It's been a little over a year since GraphQL was first introduced into GitHub's codebase, but a lot has changed since that first commit. Today, all of GitHub's new features use GraphQL internally to access data and a public GraphQL API is available for any user to query across the platform.
          |
          |In this talk, Brooks Swinnerton will discuss why GitHub made the decision to create a public GraphQL API and the things that they've learned along the way with respect to authorization, schema design, and tooling. But the interesting challenges of a public GraphQL API aren't limited to your codebase; Brooks will also discuss some of the ways that GitHub is working to introduce the new world of GraphQL to its users and integrators."""),
      cardUrl = assetUrl("/talks/brooks-swinnerton.png"),
      slidesUrl = Some("https://speakerdeck.com/bswinnerton/launching-githubs-public-graphql-api"),
      speakers = List(speaker.BrooksSwinnerton),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(15, 20),
      endTime = LocalTime.of(15, 50),
      duration = Duration.ofMinutes(30)),
    Talk(
      title = "Schema first development",
      description =
        "GraphQL is not just a great way to query data from your server but also " +
        "an incredibly expressive format to describe the data model of your domain " +
        "and application. In this lightning talk, you will see how you can use " +
        "GraphQL IDL as the foundation of your application and leverage the schema " +
        "definition as a contract between teams.",
      cardUrl = assetUrl("/talks/johannes-schickling.png"),
      speakers = List(speaker.JohannesSchickling),
      format = TalkFormat.Lightning,
      startTime = LocalTime.of(15, 50),
      endTime = LocalTime.of(15, 58),
      duration = Duration.ofMinutes(8)),
    Talk(
      title = "Building a GraphQL client in Javascript",
      description =
        "A really quick overview of what drove me, and a small team at shopify, " +
        "to build our own Relay compliant GraphQL client, and some of the " +
        "features we were able to develop.",
      cardUrl = assetUrl("/talks/mina-smart.png"),
      speakers = List(speaker.MinaSmart),
      format = TalkFormat.Lightning,
      startTime = LocalTime.of(15, 58),
      endTime = LocalTime.of(16, 6),
      duration = Duration.ofMinutes(8)),
    Talk(
      title = "Query Graphs with \"Graph\"QL",
      description =
        "Despite the \"Graph\" in the name, GraphQL is mostly used to " +
        "query relational databases or object models. But it is really well " +
        "suited to querying graph databases too. In this talk, I'll demonstrate " +
        "how I implemented a GraphQL endpoint for the Neo4j graph database and " +
        "how you would use it in your app.",
      cardUrl = assetUrl("/talks/michael-hunger.png"),
      speakers = List(speaker.MichaelHunger),
      format = TalkFormat.Lightning,
      startTime = LocalTime.of(16, 6),
      endTime = LocalTime.of(16, 14),
      duration = Duration.ofMinutes(8)),
    Talk(
      title = "Using GraphQL in a Mesh Network to Enable Real-Time Collaboration",
      description =
        "At Hudl we're helping coaches and athletes around the world perform to " +
        "their best by incorporating live video analysis into their in-game and " +
        "training workflows. We recently started using GraphQL in a P2P network " +
        "to allow for seamless real-time collaboration between individuals on " +
        "the bench and in the locker room. We will talk about automatic discovery, " +
        "capability announcements, and how we leverage GraphQL in order to provide " +
        "value for both internal API consumers and third-parties wanting to " +
        "interface with our systems.",
      cardUrl = assetUrl("/talks/tommy-lillehagen.png"),
      slidesUrl = Some("https://drive.google.com/file/d/0BwHmsxco56hyRkxTQ2pVbEs2ZnM/view"),
      speakers = List(speaker.TommyLillehagen),
      format = TalkFormat.Lightning,
      startTime = LocalTime.of(16, 14),
      endTime = LocalTime.of(16, 22),
      duration = Duration.ofMinutes(8)),
    Talk(
      title = "MockQ buddy! Easy API Driven Design and Accurate Documentation with GraphQL",
      description =
        cleanupText(
          """In this talk I will demonstrate how to rapidly get a GraphQL service running with realistic mock data that consumers can develop against and discuss. In parallel we use the same schema to provide the real data and accurate, up-to-date documentation.
            |
            |GraphQL fills in so many gaps which Rest leaves you to deal with. At Cardano, GraphQL is integral to our technology reboot. I will cover what I would have really liked when I started with GraphQL, for myself and to sell to management.
            |
            |Like with interface driven design for coding, API Driven design allows consumers and producers to agree upon the common API interface then have each team independently start working on either side of this API. Using Node.js, Express, Apollo and GraphQL we'll start with a schema, quickly get a service running with simple mock data then iterate with more accurate and complex mocks. Next, we leave this running for the UI developers to use whilst we start on the real service in parallel. No need for an external service, just start in the code.
            |
            |Once released consumers need accurate API documentation and to try it out. Traditional methods like Wiki’s, annotation or codegen such as Swagger still require the developer to update them and of course we all know how often this happens, not! GraphQL’s schema means your documentation comes from the same code used to create the real service. Plus, you get the nice GraphiQL portal built in to your service to view this documentation and try the service."""),
      shortDescription = Some(cleanupText(
        """In this talk I will demonstrate how to rapidly get a GraphQL service running with realistic mock data that consumers can develop against and discuss. In parallel we use the same schema to provide the real data and accurate, up-to-date documentation.
          |
          |GraphQL fills in so many gaps which Rest leaves you to deal with. At Cardano, GraphQL is integral to our technology reboot. I will cover what I would have really liked when I started with GraphQL, for myself and to sell to management.""")),
      cardUrl = assetUrl("/talks/alison-johnston.png"),
      slidesUrl = Some("https://docs.google.com/presentation/d/1MXV33k7SZhDAyhbaobVXF41hmEoRQ84scJPaSR4qMU4/pub?start=false&loop=false&delayms=3000&slide=id.p"),
      speakers = List(speaker.AlisonJohnston),
      format = TalkFormat.Lightning,
      startTime = LocalTime.of(16, 22),
      endTime = LocalTime.of(16, 30),
      duration = Duration.ofMinutes(8)),
    Break(LocalTime.of(16, 30), LocalTime.of(16, 50), Duration.ofMinutes(20)),
    Talk(
      title = "Panel Discussion",
      description = "Adopting GraphQL in existing products and organizations",
      cardUrl = assetUrl("/share-graphql-europe.png"),
      speakers = List(
        speaker.DanielSchafer,
        speaker.MinaSmart,
        speaker.BjoernRochel,
        speaker.BrooksSwinnerton,
        speaker.ChadFowler),
      format = TalkFormat.PanelDiscussion,
      startTime = LocalTime.of(16, 50),
      endTime = LocalTime.of(17, 50),
      duration = Duration.ofHours(1)),
    Talk(
      title = "Closing Keynote",
      description = "More information coming soon.", // TODO: description
      speakers = List(speaker.LeeByron),
      cardUrl = assetUrl("/talks/lee-byron.png"),
      format = TalkFormat.Standard,
      startTime = LocalTime.of(17, 50),
      endTime = LocalTime.of(18, 20),
      duration = Duration.ofMinutes(30)),
    Talk(
      title = "Closing Remarks",
      description = "Closing Remarks",
      cardUrl = assetUrl("/share-graphql-europe.png"),
      speakers = List(speaker.ChadFowler),
      format = TalkFormat.Special,
      startTime = LocalTime.of(18, 20),
      endTime = LocalTime.of(18, 30),
      duration = Duration.ofMinutes(10)),
    Talk(
      title = "GraphQL Community Party!",
      description = cleanupText(
        """**Fitcher's Vogel**
          |
          |Warschauer Straße 26<br>
          |10243 Berlin"""),
      cardUrl = assetUrl("/share-graphql-europe.png"),
      speakers = Nil,
      format = TalkFormat.Special,
      startTime = LocalTime.of(18, 30),
      endTime = LocalTime.of(22, 0),
      duration = Duration.ofHours(3).plusMinutes(30))
  )

  val team = List(
    TeamMember(
      name = "Oleg Ilyenko",
      photoUrl = Some(assetUrl("/team/oleg.jpg")),
      teamSection = TeamSection.Core,
      description = Some(
        "Oleg is a passionate software engineer and speaker who loves innovative ideas and technology, " +
        "challenging problems and working on things that help other people. " +
        "Oleg is an author of Sangria - a scala GraphQL implementation."),
      twitter = Some("easyangel"),
      github = Some("OlegIlyenko")),
    TeamMember(
      name = "Johannes Schickling",
      photoUrl = Some(assetUrl("/team/johannes.jpg")),
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
      photoUrl = Some(assetUrl("/team/emma.jpg")),
      teamSection = TeamSection.Core,
      description = Some(
        "Emma is co-founder of Honeypot, a developer-focused job platform. She loves bringing " +
        "the community together and previously was part of the organizing team of RustFest."),
      twitter = None,
      github = None),
    TeamMember(
      name = "Artyom Chelbayev",
      photoUrl = Some(assetUrl("/team/artyom.jpg")),
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
      photoUrl = Some(assetUrl("/team/johanna-new.jpg")),
      teamSection = TeamSection.Core,
      description = Some(
        "Johanna is Honeypot’s UI/UX Designer. A Finnish dog-lover with passion for " +
        "fine art, architecture and old black and white photos. She worked as a painter before moving to Berlin."),
      twitter = Some("batjohe"),
      github = Some("Batjohe")),
    TeamMember(
      name = "Dajana Günther",
      photoUrl = Some(assetUrl("/team/dajana.jpg")),
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
      availableUntil = LocalDate.of(2017, Month.MAY, 14),
      availableUntilText = "Available until May 14th",
      url = conf.ticketsUrl,
      soldOut = true,
      available = false),
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
      Direction(DirectionType.TrainStation, "Main Train Station", "From Berlin **Main Train Station** you can catch a train (S5 towards Strausberg, S7 towards Ahrensfelde or S75 towards Wartenberg) to Warschauer Straße. Trains leave every 10 min and cost 2,80€ per person. "),
      Direction(DirectionType.TrainStation, "Ostbahnhof Train Station", "From **Ostbahnhof Train Station** you can catch a train (S3 towards Friedrichshagen, S7 towards Ahrensfelde or S75 towards Wartenberg) to Warschauer Straße. Trains leave every 10 min and cost 2,80€ per person. "),
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
      schedule = schedule,
      team = team,
      tickets = tickets,
      url = "https://graphql-europe.org")
  )

  val conferencesByEdition = conferences.groupBy(_.edition).mapValues(_.head)

  lazy val talks = schedule.collect{case t: Talk ⇒ t}

  def talksBySpeaker(speaker: Speaker) = talks.filter(_.speakers.contains(speaker))
  def talkBySlug(slug: String) = talks.find(_.slug == slug)
  def speakerBySlug(slug: String) = speakers.find(_.slug == slug)
}
