package com.huemanely.application.email

import com.huemanely.domain.email.Email
import com.huemanely.application.email.EmailSender

import org.slf4j.LoggerFactory

class MockEmailSender : EmailSender {
    private val logger = LoggerFactory.getLogger(MockEmailSender::class.java)

    override fun send(email: Email) {
        // non-blocking log; can be extended to write to a file/queue for inspections
        logger.info("[MOCK EMAIL] to={} subject={} bodyPreview={}", email.to, email.subject, email.body?.take(120) ?: "")
    }
}