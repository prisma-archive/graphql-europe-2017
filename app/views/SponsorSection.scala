package views

case class SponsorSection(name: String, sponsorType: SponsorType.Value, color: String)

object SponsorSection {
  val sections = List(
    SponsorSection("Platinum", SponsorType.Platinum, "#AAACAA"),
    SponsorSection("Gold", SponsorType.Gold, "#FA8D22"),
    SponsorSection("Silver", SponsorType.Silver, "#331A99"),
    SponsorSection("Bronze", SponsorType.Bronze, "#F56199"),
    SponsorSection("Co-Organisers", SponsorType.Organiser, "#1f2228")
  )
}