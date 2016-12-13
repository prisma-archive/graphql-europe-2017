package views

case class Speaker(
  name: String,
  photo: Option[String],
  talkTitle: Option[String],
  company: Option[String],
  twitterUrl: Option[String],
  githubUrl: Option[String]
)
