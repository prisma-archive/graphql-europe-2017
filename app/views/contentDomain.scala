package views

import java.time._

import sangria.macros.derive._
import graphql.customScalars._
import repo.ContentRepo
import sangria.schema._

trait WithSlug {
  def slug: String
}

case class Speaker(
  name: String,
  photoUrl: Option[String],
  company: Option[String],
  twitter: Option[String],
  github: Option[String],
  description: Option[String],
  stub: Boolean = false
) extends WithSlug {
  lazy val slug = name.replaceAll("\\s+", "-").toLowerCase
}

object Speaker {
  private val TalkFormatArg = Argument("format", OptionInputType(TalkFormat.graphqlType))

  implicit lazy val graphqlType = deriveObjectType[ContentRepo, Speaker](
    ExcludeFields("stub"),
    AddFields(
      Field("url", StringType, resolve = c ⇒ c.ctx.url(s"/talks/${c.value.slug}")),
      Field("talks", ListType(Talk.graphqlType),
      arguments = TalkFormatArg :: Nil,
      resolve = c ⇒ {
        val talks = c.ctx.talksBySpeaker(c.value)

        c.withArgs(TalkFormatArg)(_.fold(talks)(f ⇒ talks.filter(_.format == f)))
      })))
}

object TalkFormat extends Enumeration {
  val Special, PanelDiscussion, Standard, Lightning = Value

  implicit val graphqlType: EnumType[TalkFormat.Value] = deriveEnumType[TalkFormat.Value]()
}

object ScheduleEntryType extends Enumeration {
  val Registration, Talk, Break, Lunch = Value

  implicit val graphqlType: EnumType[ScheduleEntryType.Value] = deriveEnumType[ScheduleEntryType.Value]()
}

abstract class ScheduleEntry(
  val entryType: ScheduleEntryType.Value
) {
  def startTime: LocalTime
  def endTime: LocalTime
  def duration: Duration
}

object ScheduleEntry {
  implicit val graphqlType = InterfaceType("ScheduleEntry", fields[ContentRepo, ScheduleEntry](
    Field("startTime", TimeType, resolve = _.value.startTime),
    Field("endTime", TimeType, resolve = _.value.endTime),
    Field("duration", DurationType, resolve = _.value.duration),
    Field("entryType", ScheduleEntryType.graphqlType, resolve = _.value.entryType)))
}

case class Break(startTime: LocalTime, endTime: LocalTime, duration: Duration) extends ScheduleEntry(ScheduleEntryType.Break)

object Break {
  implicit val graphqlType = deriveObjectType[ContentRepo, Break](Interfaces(ScheduleEntry.graphqlType))
}

case class Registration(startTime: LocalTime, endTime: LocalTime, duration: Duration) extends ScheduleEntry(ScheduleEntryType.Registration)

object Registration {
  implicit val graphqlType = deriveObjectType[ContentRepo, Registration](Interfaces(ScheduleEntry.graphqlType))
}

case class Lunch(startTime: LocalTime, endTime: LocalTime, duration: Duration) extends ScheduleEntry(ScheduleEntryType.Lunch)

object Lunch {
  implicit val graphqlType = deriveObjectType[ContentRepo, Lunch](Interfaces(ScheduleEntry.graphqlType))
}

case class Talk(
  title: String,
  description: String,
  format: TalkFormat.Value,
  startTime: LocalTime,
  endTime: LocalTime,
  duration: Duration,
  speakers: List[Speaker]
) extends ScheduleEntry(ScheduleEntryType.Talk) with WithSlug {
  lazy val slug = title.replaceAll("\\s+", "-").replaceAll("[^a-zA-Z0-9\\-]", "").toLowerCase
}

object Talk {
  implicit lazy val graphqlType: ObjectType[ContentRepo, Talk] =
    deriveObjectType[ContentRepo, Talk](
      Interfaces(ScheduleEntry.graphqlType),
      AddFields(Field("url", StringType, resolve = c ⇒ c.ctx.url(s"/talks/${c.value.slug}"))))
}

object SponsorType extends Enumeration {
  val Organiser, Partner, Opportunity, Platinum, Gold, Silver, Bronze = Value

  implicit val graphqlType: EnumType[SponsorType.Value] = deriveEnumType[SponsorType.Value]()
}

case class Sponsor(
  name: String,
  sponsorType: SponsorType.Value,
  url: String,
  logoUrl: String,
  description: Option[String],
  twitter: Option[String],
  github: Option[String])

object Sponsor {
  implicit val graphqlType = deriveObjectType[ContentRepo, Sponsor]()
}

object Edition extends Enumeration {
  val Berlin2017 = Value

  implicit val graphqlType: EnumType[Edition.Value] = deriveEnumType[Edition.Value]()
}

case class Ticket(
  name: String,
  price: String,
  availableUntil: LocalDate,
  availableUntilText: String,
  url: String,
  soldOut: Boolean,
  available: Boolean)

object Ticket {
  implicit val graphqlType = deriveObjectType[ContentRepo, Ticket](
    ExcludeFields("availableUntilText"))
}

case class Address(
  country: String,
  city: String,
  zipCode: String,
  streetName: String,
  houseNumber: String,
  latitude: Double,
  longitude: Double)

object Address {
  implicit val graphqlType = deriveObjectType[ContentRepo, Address]()
}

object DirectionType extends Enumeration {
  val Airport, TrainStation, Car = Value

  implicit val graphqlType: EnumType[DirectionType.Value] = deriveEnumType[DirectionType.Value]()
}

case class Direction(`type`: DirectionType.Value, from: String, description: String)

object Direction {
  implicit val graphqlType = deriveObjectType[ContentRepo, Direction]()
}

case class Venue(
  name: String,
  url: String,
  phone: String,
  directions: List[Direction],
  address: Address)

object Venue {
  val DirectionTypeArg = Argument("type", OptionInputType(DirectionType.graphqlType))

  implicit val graphqlType = deriveObjectType[ContentRepo, Venue](
    ReplaceField("directions", Field("directions", ListType(Direction.graphqlType),
      arguments = DirectionTypeArg :: Nil,
      resolve = c ⇒ c.arg(DirectionTypeArg).fold(c.value.directions)(d ⇒ c.value.directions.filter(_.`type` == d))))
  )
}

case class Conference(
  name: String,
  edition: Edition.Value,
  tagLine: String,
  year: Int,
  venue: Option[Venue],
  dateStart: Option[LocalDate],
  dateEnd: Option[LocalDate],
  speakers: List[Speaker],
  sponsors: List[Sponsor],
  schedule: List[ScheduleEntry],
  team: List[TeamMember],
  tickets: List[Ticket],
  url: String)

object Conference {
  private val SectionArg = Argument("section", OptionInputType(TeamSection.graphqlType))
  private val EntryTypeArg = Argument("type", OptionInputType(ScheduleEntryType.graphqlType))
  private val TalkFormatArg = Argument("format", OptionInputType(TalkFormat.graphqlType))
  private val SponsorTypeArg = Argument("type", OptionInputType(SponsorType.graphqlType))

  implicit val graphqlType = deriveObjectType[ContentRepo, Conference](
    ReplaceField("speakers", Field("speakers", ListType(Speaker.graphqlType),
      resolve = _.value.speakers.filterNot(_.stub))),
    ReplaceField("team", Field("team", ListType(TeamMember.graphqlType),
      arguments = SectionArg :: Nil,
      resolve = c ⇒ c.withArgs(SectionArg)(_.fold(c.value.team)(s ⇒ c.value.team.filter(_.teamSection == s))))),
    ReplaceField("schedule", Field("schedule", ListType(ScheduleEntry.graphqlType),
      arguments = EntryTypeArg :: Nil,
      resolve = c ⇒ c.withArgs(EntryTypeArg)(_.fold(c.value.schedule)(et ⇒ c.value.schedule.filter(_.entryType == et))))),
    ReplaceField("sponsors", Field("sponsors", ListType(Sponsor.graphqlType),
      arguments = SponsorTypeArg :: Nil,
      resolve = c ⇒ {
        val sponsors = c.value.sponsors.sortBy(_.name)

        c.withArgs(SponsorTypeArg)(_.fold(sponsors)(s ⇒ sponsors.filter(_.sponsorType == s)))
      })),
    AddFields(Field("talks", ListType(Talk.graphqlType),
      arguments = TalkFormatArg :: Nil,
      resolve = c ⇒ {
        val talks = c.value.schedule.collect{case t: Talk ⇒ t}

        c.withArgs(TalkFormatArg)(_.fold(talks)(f ⇒ talks.filter(_.format == f)))
      }))
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
  implicit val graphqlType = deriveObjectType[ContentRepo, TeamMember]()
}