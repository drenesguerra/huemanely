package com.huemanely.application.email

import com.huemanely.infra.persistence.exposed.email.EmailRepository
import com.huemanely.infra.persistence.exposed.emailconfig.EmailConfigRepository
import com.huemanely.domain.email.*
import com.huemanely.domain.emailconfig.EmailProvider
import com.huemanely.application.email.EmailSender
import java.util.UUID


class EmailService(
    private val emailRepository: EmailRepository,
    private val emailConfigRepository: EmailConfigRepository
) {

    suspend fun createEmail(request: CreateEmailRequest) {
        val saved = emailRepository.create(request)
        try {
            println("EmailService: Calling sender for email ${saved.uuid}")

            val tenantEmailConfig = emailConfigRepository.findByTenantId(request.tenantId !!)
                ?: throw IllegalStateException("No email config found for tenant ${request.tenantId}")

            val sender = when (tenantEmailConfig.provider) {
                EmailProvider.SMTP -> SmtpEmailSender(tenantEmailConfig)
                EmailProvider.AWS_SES -> SmtpEmailSender(tenantEmailConfig)
                EmailProvider.SENDGRID -> SmtpEmailSender(tenantEmailConfig)
            }
            
            sender.send(saved)

            emailRepository.updateStatus(saved.id, EmailStatus.SENT)
        } catch (e: Exception) {
            println("EmailService: Failed to send email ${saved.uuid}: ${e.message}")
            emailRepository.updateStatus(saved.id, EmailStatus.FAILED, e.message)
        }
    }

    suspend fun getEmailByTenant(tenantId: UUID) : List<EmailResponse> {
        return emailRepository.findByTenant(tenantId).toResponseList()
    }

}