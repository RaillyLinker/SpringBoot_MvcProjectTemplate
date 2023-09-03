package com.railly_linker.springboot_mvc_project_template.controllers.tk_test

import com.railly_linker.springboot_mvc_project_template.util_dis.EmailSenderUtilDi
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class TkTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfileMbr: String,

    // 이메일 발송 유틸
    private val emailSenderUtilDi: EmailSenderUtilDi
) {
    // <멤버 변수 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse, inputVo: TkTestController.Api1InputVo) {
        emailSenderUtilDi.sendMessageMail(
            inputVo.senderName,
            inputVo.receiverEmailAddressList.toTypedArray(),
            inputVo.carbonCopyEmailAddressList?.toTypedArray(),
            inputVo.subject,
            inputVo.message,
            null,
            inputVo.multipartFileList
        )

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse, inputVo: TkTestController.Api2InputVo) {
        emailSenderUtilDi.sendThymeLeafHtmlMail(
            inputVo.senderName,
            inputVo.receiverEmailAddressList.toTypedArray(),
            inputVo.carbonCopyEmailAddressList?.toTypedArray(),
            inputVo.subject,
            "tk_test_api2/html_email_sample",
            hashMapOf(
                Pair("message", inputVo.message)
            ),
            null,
            hashMapOf(
                "html_to_pdf_sample_image" to ClassPathResource("static/tk_test_api2/html_to_pdf_sample.jpg"),
                "html_email_sample_css" to  ClassPathResource("static/tk_test_api2/html_email_sample.css")
            ),
            null,
            inputVo.multipartFileList
        )

        httpServletResponse.setHeader("api-result-code", "ok")
    }
}