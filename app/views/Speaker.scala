package views

case class Speaker(
  name: String,
  photo: Option[String],
  description: Option[String],
  company: Option[String],
  twitterUrl: Option[String],
  githubUrl: Option[String]
)
