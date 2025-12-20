package com.huemanely.application.email

import javax.mail.*
import javax.mail.internet.*
import com.huemanely.domain.email.Email
import com.huemanely.domain.emailconfig.EmailConfig
import com.huemanely.application.email.EmailSender
import org.slf4j.LoggerFactory
import java.util.Properties

class SmtpEmailSender(
    private val emailConfig: EmailConfig
) : EmailSender {

    private val logger = LoggerFactory.getLogger(SmtpEmailSender::class.java)

    override fun send(email: Email) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", emailConfig.host)
            put("mail.smtp.port", emailConfig.port.toString())
            put("mail.smtp.ssl.trust", emailConfig.host)
            // allow following in some environments:
            put("mail.smtp.connectiontimeout", "10000")
            put("mail.smtp.timeout", "10000")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication =
                PasswordAuthentication(emailConfig.username, emailConfig.password)
        })

        try {
            val msg = MimeMessage(session).apply {
                setFrom(InternetAddress(emailConfig.username))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.to))
                subject = email.subject
                setText(email.body)
            }

            Transport.send(msg)
            logger.info("Email queued/sent to {} (subject={})", email.to, email.subject)
        } catch (e: AuthenticationFailedException) {
            logger.error("SMTP authentication failed for {}: {}", emailConfig.username, e.message)
            throw e
        } catch (e: MessagingException) {
            logger.error("SMTP messaging error: {}", e.message, e)
            throw e
        } catch (e: Exception) {
            logger.error("Unexpected error when sending email: {}", e.message, e)
            throw e
        }
    }
}