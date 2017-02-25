package views

import java.time.LocalDate

import sangria.macros.derive._
import graphql.customScalars._
import sangria.schema._

case class Speaker(
  name: String,
  photoUrl: Option[String],
  talkTitle: Option[String],
  company: Option[String],
  twitter: Option[String],
  github: Option[String],
  stub: Boolean = false)

object Speaker {
  implicit val graphqlType = deriveObjectType[Unit, Speaker](ExcludeFields("stub"))
}

object SponsorType extends Enumeration {
  val Organiser, Platinum, Gold, Silver, Bronze = Value

  implicit val graphqlType: EnumType[SponsorType.Value] = deriveEnumType[SponsorType.Value]()
}

case class Sponsor(
  name: String,
  url: String,
  logoUrl: String,
  description: Option[String],
  twitter: Option[String],
  github: Option[String])

object Sponsor {
  implicit val graphqlType = deriveObjectType[Unit, Sponsor]()
}

case class Venue(name: String, address: String, url: String)

object Venue {
  implicit val graphqlType = deriveObjectType[Unit, Venue]()
}

object Edition extends Enumeration {
  val Berlin2017 = Value

  implicit val graphqlType: EnumType[Edition.Value] = deriveEnumType[Edition.Value]()
}

case class Conference(
  name: String,
  venue: Option[Venue],
  dateStart: Option[LocalDate],
  dateEnd: Option[LocalDate],
  speakers: List[Speaker],
  team: List[TeamMember],
  url: String)

object Conference {
  private val SectionArg = Argument("section", OptionInputType(TeamSection.graphqlType))

  implicit val graphqlType = deriveObjectType[Unit, Conference](
    ReplaceField("speakers", Field("speakers", ListType(Speaker.graphqlType),
      resolve = _.value.speakers.filterNot(_.stub))),
    ReplaceField("team", Field("team", ListType(TeamMember.graphqlType),
      arguments = SectionArg :: Nil,
      resolve = c ⇒ c.withArgs(SectionArg)(_.fold(c.value.team)(s ⇒ c.value.team.filter(_.teamSection == s)))))
  )
}

object TeamSection extends Enumeration {
  val Core, SpecialThanks = Value

  implicit val graphqlType: EnumType[TeamSection.Value] = deriveEnumType[TeamSection.Value]()
}

case class TeamMember(
  name: String,
  photoUrl: Option[String],
  teamSection: TeamSection.Value,
  description: Option[String],
  twitter: Option[String],
  github: Option[String])

object TeamMember {
  implicit val graphqlType = deriveObjectType[Unit, TeamMember]()
}
