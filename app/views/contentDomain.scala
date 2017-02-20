package views

import java.time.LocalDate

import sangria.macros.derive._
import graphql.customScalars._
import sangria.schema.EnumType

case class Speaker(
  name: String,
  photoUrl: Option[String],
  talkTitle: Option[String],
  company: Option[String],
  twitter: Option[String],
  github: Option[String])

object Speaker {
  implicit val graphqlType = deriveObjectType[Unit, Speaker]()
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
  twitterUrl: Option[String])

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
  url: String)

object Conference {
  implicit val graphqlType = deriveObjectType[Unit, Conference]()
}
