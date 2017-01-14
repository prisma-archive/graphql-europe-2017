package repo

import javax.inject.Inject

import com.google.inject.Singleton
import org.apache.commons.mail.{DefaultAuthenticator, Email, HtmlEmail, SimpleEmail}
import play.api.Configuration

import scala.collection.JavaConverters._

@Singleton
class MailClient @Inject()(config: Configuration) {

  def send(
    to: String,
    subject: String,
    text: String,
    html: String
  ): Unit = {
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
  }

}
