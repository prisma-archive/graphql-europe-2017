package graphql

import java.time.{Duration, LocalDate, LocalTime}

import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation
import sangria.ast

import scala.util.{Failure, Success, Try}

object customScalars {
  private case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")
  private case object TimeCoercionViolation extends ValueCoercionViolation("Time value expected")

  private def parseLocalDate(s: String) = Try(LocalDate.parse(s)) match {
    case Success(date) ⇒ Right(date)
    case Failure(_) ⇒ Left(DateCoercionViolation)
  }

  private def parseLocalTime(s: String) = Try(LocalTime.parse(s)) match {
    case Success(time) ⇒ Right(time)
    case Failure(_) ⇒ Left(TimeCoercionViolation)
  }

  private def parseDuration(s: String) = Try(Duration.parse(s)) match {
    case Success(time) ⇒ Right(time)
    case Failure(_) ⇒ Left(TimeCoercionViolation)
  }

  private def formatDuration(d: Duration) = {
    val seconds = d.getSeconds
    val absSeconds = Math.abs(seconds)

    "%02d:%02d".format(absSeconds / 3600, (absSeconds % 3600) / 60)
  }

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

  implicit val TimeType = ScalarType[LocalTime]("Time",
    coerceOutput = (t, _) ⇒ t.toString,
    coerceUserInput = {
      case s: String ⇒ parseLocalTime(s)
      case _ ⇒ Left(TimeCoercionViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _) ⇒ parseLocalTime(s)
      case _ ⇒ Left(TimeCoercionViolation)
    })

  implicit val DurationType = ScalarType[Duration]("Duration",
    coerceOutput = (t, _) ⇒ formatDuration(t),
    coerceUserInput = {
      case s: String ⇒ parseDuration(s)
      case _ ⇒ Left(TimeCoercionViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _) ⇒ parseDuration(s)
      case _ ⇒ Left(TimeCoercionViolation)
    })
}
