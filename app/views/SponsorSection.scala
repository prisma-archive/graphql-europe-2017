package views

case class SponsorSection(name: String, sponsorType: SponsorType.Value, color: String, borderColor: String, justLogos: Boolean = false)

object SponsorSection {
  val sections = List(
    SponsorSection("Platinum", SponsorType.Platinum, "#AAACAA", "rgba(170, 172, 170, 0.25)"),
    SponsorSection("Gold", SponsorType.Gold, "#FA8D22", "rgba(250, 141, 34, 0.25)"),
    SponsorSection("Silver", SponsorType.Silver, "#331A99", "rgba(51, 26, 153, 0.25)"),
    SponsorSection("Bronze", SponsorType.Bronze, "#F56199", "rgba(245, 97, 153, 0.25)"),
    SponsorSection("Opportunity", SponsorType.Opportunity, "#1f2228", "#c9ced6", justLogos = true),
    SponsorSection("Partners", SponsorType.Partner, "#1f2228", "#c9ced6", justLogos = true),
    SponsorSection("Co-Organisers", SponsorType.Organiser, "#1f2228", "#c9ced6"))
}