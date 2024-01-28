package org.team.b6.catchtable.global.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class UtilService(
    private val javaMailSender: JavaMailSender
) {
    fun sendMail(email: String, subject: String, text: String) =
        javaMailSender.createMimeMessage().let {
            MimeMessageHelper(it, false).let { helper ->
                helper.setTo(email)
                helper.setSubject(subject)
                helper.setText(text)
            }
            javaMailSender.send(it)
        }
}