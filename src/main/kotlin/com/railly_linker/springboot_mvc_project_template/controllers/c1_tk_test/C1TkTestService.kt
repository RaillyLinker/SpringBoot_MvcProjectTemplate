package com.railly_linker.springboot_mvc_project_template.controllers.c1_tk_test

import com.railly_linker.springboot_mvc_project_template.util_dis.EmailSenderUtilDi
import com.railly_linker.springboot_mvc_project_template.util_objects.NaverSmsUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class C1TkTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // 이메일 발송 유틸
    private val emailSenderUtilDi: EmailSenderUtilDi
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse, inputVo: C1TkTestController.Api1InputVo) {
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
    fun api2(httpServletResponse: HttpServletResponse, inputVo: C1TkTestController.Api2InputVo) {
        emailSenderUtilDi.sendThymeLeafHtmlMail(
            inputVo.senderName,
            inputVo.receiverEmailAddressList.toTypedArray(),
            inputVo.carbonCopyEmailAddressList?.toTypedArray(),
            inputVo.subject,
            "template_c1_n2/html_email_sample",
            hashMapOf(
                Pair("message", inputVo.message)
            ),
            null,
            hashMapOf(
                "image_sample" to ClassPathResource("static/resource_c1_n2/image_sample.jpg")
            ),
            null,
            inputVo.multipartFileList
        )

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse, inputVo: C1TkTestController.Api3InputVo) {
        val phoneNumberSplit = inputVo.phoneNumber.split(")") // ["82", "010-0000-0000"]

        // 국가 코드 (ex : 82)
        val countryCode = phoneNumberSplit[0]

        // 전화번호 (ex : "01000000000")
        val phoneNumber = (phoneNumberSplit[1].replace("-", "")).replace(" ", "")

        // SMS 전송
        NaverSmsUtilObject.sendSms(
            NaverSmsUtilObject.SendSmsInputVo(
                countryCode,
                phoneNumber,
                inputVo.smsMessage
            )
        )
    }
}