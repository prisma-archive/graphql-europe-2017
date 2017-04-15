package views

import java.time.{Duration, LocalTime}
import java.time.format.DateTimeFormatter

object FormatUtil {
  val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

  def format(time: LocalTime) =
    time.format(timeFormat)

  def format(f: TalkFormat.Value) = f match {
    case TalkFormat.PanelDiscussion ⇒ "Panel Discussion"
    case f ⇒ f.toString
  }

  def format(d: Duration) = {
    val seconds = d.getSeconds
    val absSeconds = Math.abs(seconds)

    "%02d:%02d".format(absSeconds / 3600, (absSeconds % 3600) / 60)
  }
}
