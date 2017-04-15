package views

case class Config(
  gaCode: Option[String],
  smoochApiKey: Option[String],
  graphCoolProjectKey: String,
  canonicalUrl: String,
  name: String,
  location: String,
  shortDescription: String,
  description: String,
  sponsorEmail: String,
  supportEmail: String,
  cdn: Boolean,
  preview: Boolean,
  ticketsUrl: String,
  googleMapsApiKey: String,
  path: String = ""
)
