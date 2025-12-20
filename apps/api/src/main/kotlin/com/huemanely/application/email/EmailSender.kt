package com.huemanely.application.email

import com.huemanely.domain.email.Email

interface EmailSender {
    fun send(email: Email)
}