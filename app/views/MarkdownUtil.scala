package views

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.profiles.pegdown.{Extensions, PegdownOptionsAdapter}
import play.twirl.api.Html

object MarkdownUtil {
  def render(text: String) = {
    val options = PegdownOptionsAdapter.flexmarkOptions(Extensions.ALL)
    val parser = Parser.builder(options).build()
    val renderer = HtmlRenderer.builder(options).softBreak("\n").build()

    Html(renderer.render(parser.parse(text)))
  }
}
