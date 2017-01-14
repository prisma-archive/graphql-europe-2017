package repo

import javax.inject.Inject

import com.google.inject.Singleton
import org.apache.commons.mail.{DefaultAuthenticator, Email, HtmlEmail, SimpleEmail}
import play.api.{Configuration, Logger}

import scala.collection.JavaConverters._

@Singleton
class MailClient @Inject()(config: Configuration) {
  val log = Logger(this.getClass)

  def isConfigured = config.getString("mail.userName").getOrElse("").nonEmpty

  def send(
    to: String,
    subject: String,
    text: String,
    html: String
  ): Unit = {
    if (isConfigured) {
      val email = new HtmlEmail()

      email.setHostName(config.getString("mail.smtpHost").get)
      email.setSmtpPort(config.getInt("mail.smtpPort").get)
      email.setAuthenticator(new DefaultAuthenticator(
        config.getString("mail.userName").get,
        config.getString("mail.password").get))
      email.setSSLOnConnect(config.getBoolean("mail.ssl").get)

      email.setFrom(config.getString("mail.from").get)
      email.addTo(to)
      email.setSubject(subject)
      email.setTextMsg(text)
      email.setHtmlMsg(html)

      email.send()
    } else {
      log.error("SMTP is not configured!")
    }
  }

}
