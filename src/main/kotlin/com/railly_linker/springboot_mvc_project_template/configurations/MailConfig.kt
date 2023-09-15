package com.railly_linker.springboot_mvc_project_template.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailConfig {
    companion object {
        // !!!SMTP 설정!!
        const val SMTP_HOST = "smtp.gmail.com"
        const val SMTP_PORT = 587
        const val MAIL_SENDER_NAME = "test@gmail.com"
        const val MAIL_SENDER_PASSWORD = "test"
        const val TIME_OUT_MILLIS = 1000 * 10
    }

    @Bean
    fun javaMailSender(): JavaMailSenderImpl {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = SMTP_HOST
        mailSender.port = SMTP_PORT
        mailSender.username = MAIL_SENDER_NAME
        mailSender.password = MAIL_SENDER_PASSWORD

        val props: Properties = mailSender.javaMailProperties
        props["mail.smtp.connectiontimeout"] = TIME_OUT_MILLIS.toString()
        props["mail.smtp.timeout"] = TIME_OUT_MILLIS.toString()
        props["mail.smtp.writetimeout"] = TIME_OUT_MILLIS.toString()

        // !!!SMTP 종류별 설정을 바꿔주기!!
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"

        return mailSender
    }
}

