package graphql

import java.time.LocalDate

import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.ISODateTimeFormat
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation
import sangria.ast

import scala.util.{Failure, Success, Try}

object customScalars {
  private case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")

  private def parseDate(s: String) = Try(new DateTime(s, DateTimeZone.UTC)) match {
    case Success(date) ⇒ Right(date)
    case Failure(_) ⇒ Left(DateCoercionViolation)
  }

  private def parseLocalDate(s: String) = Try(LocalDate.parse(s)) match {
    case Success(date) ⇒ Right(date)
    case Failure(_) ⇒ Left(DateCoercionViolation)
  }

  implicit val DateTimeType = ScalarType[DateTime]("DateTime",
    coerceOutput = (d, _) ⇒ ISODateTimeFormat.dateTime().print(d),
    coerceUserInput = {
      case s: String ⇒ parseDate(s)
      case _ ⇒ Left(DateCoercionViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _) ⇒ parseDate(s)
      case _ ⇒ Left(DateCoercionViolation)
    })

  implicit val DateType = ScalarType[LocalDate]("Date",
    coerceOutput = (d, _) ⇒ d.toString,
    coerceUserInput = {
      case s: String ⇒ parseLocalDate(s)
      case _ ⇒ Left(DateCoercionViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _) ⇒ parseLocalDate(s)
      case _ ⇒ Left(DateCoercionViolation)
    })
}
