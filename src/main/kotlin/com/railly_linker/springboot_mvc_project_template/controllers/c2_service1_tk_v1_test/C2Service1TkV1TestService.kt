package com.railly_linker.springboot_mvc_project_template.controllers.c2_service1_tk_v1_test

import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.request_apis.FcmGoogleapisComRequestApi
import com.railly_linker.springboot_mvc_project_template.custom_dis.EmailSenderUtilDi
import com.railly_linker.springboot_mvc_project_template.custom_objects.*
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.HashMap

@Service
class C2Service1TkV1TestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // 이메일 발송 유틸
    private val emailSenderUtilDi: EmailSenderUtilDi
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse, inputVo: C2Service1TkV1TestController.Api1InputVo) {
        emailSenderUtilDi.sendMessageMail(
            inputVo.senderName,
            inputVo.receiverEmailAddressList.toTypedArray(),
            inputVo.carbonCopyEmailAddressList?.toTypedArray(),
            inputVo.subject,
            inputVo.message,
            null,
            inputVo.multipartFileList
        )

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse, inputVo: C2Service1TkV1TestController.Api2InputVo) {
        // CID 는 첨부파일을 보내는 것과 동일한 의미입니다.
        // 고로 전송시 서버 성능에 악영향을 끼칠 가능성이 크고, CID 처리도 번거로우므로, CDN 을 사용하고, CID 는 되도록 사용하지 마세요.
        emailSenderUtilDi.sendThymeLeafHtmlMail(
            inputVo.senderName,
            inputVo.receiverEmailAddressList.toTypedArray(),
            inputVo.carbonCopyEmailAddressList?.toTypedArray(),
            inputVo.subject,
            "template_c2_n2/html_email_sample",
            hashMapOf(
                Pair("message", inputVo.message)
            ),
            null,
            hashMapOf(
                "html_email_sample_css" to ClassPathResource("static/resource_c2_n2/html_email_sample.css"),
                "image_sample" to ClassPathResource("static/resource_c2_n2/image_sample.jpg")
            ),
            null,
            inputVo.multipartFileList
        )

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse, inputVo: C2Service1TkV1TestController.Api3InputVo) {
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

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api4(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1TestController.Api4InputVo
    ) {
        FcmPushUtilObject.sendMessageTo(
            inputVo.senderServerKey,
            FcmPushUtilObject.SendMessageToInputVo(
                inputVo.receiverDeviceTokenList,
                "high",
                true,
                FcmGoogleapisComRequestApi.PostFcmSendInputVo.Notification(
                    inputVo.notificationTitle, inputVo.notificationContent
                ),
                hashMapOf()
            )
        )

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        inputVo: C2Service1TkV1TestController.Api5InputVo
    ): C2Service1TkV1TestController.Api5OutputVo? {
        val excelData = ExcelFileUtilObject.readExcel(
            inputVo.excelFile.inputStream,
            inputVo.sheetIdx,
            inputVo.rowRangeStartIdx,
            inputVo.rowRangeEndIdx,
            inputVo.columnRangeIdxList,
            inputVo.minColumnLength
        )

        httpServletResponse.setHeader("api-result-code", "0")
        return C2Service1TkV1TestController.Api5OutputVo(
            excelData?.size ?: 0,
            excelData.toString()
        )
    }


    ////
    fun api6(httpServletResponse: HttpServletResponse, inputVo: C2Service1TkV1TestController.Api6InputVo) {
        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 확장자 포함 파일명 생성
        val saveFileName = "temp_${timeString}.xlsx"

        // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
        val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()
        val file = fileTargetPath.toFile()

        val inputExcelSheetDataMap: HashMap<String, List<List<String>>> = hashMapOf()
        for (data in inputVo.inputExcelSheetDataList) {
            inputExcelSheetDataMap[data.sheetName] = data.sheetData
        }

        ExcelFileUtilObject.writeExcel(file.outputStream(), inputExcelSheetDataMap)

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api7(
        httpServletResponse: HttpServletResponse
    ) {
        // thymeLeaf 엔진으로 파싱한 HTML String 가져오기
        // 여기서 가져온 HTML 내에 기입된 static resources 의 경로는 절대경로가 아님
        val htmlString = ThymeleafParserUtilObject.parseHtmlFileToHtmlString(
            "template_c2_n7/html_to_pdf_sample", // thymeLeaf Html 이름 (ModelAndView 의 사용과 동일)
            // thymeLeaf 에 전해줄 데이터 Map
            mapOf(
                "title" to "PDF 변환 테스트"
            )
        )

        // htmlString 을 PDF 로 변환하여 저장
        // XHTML 1.0(strict), CSS 2.1 (@page 의 size 는 가능)
        PdfGenerator.createPdfFileFromHtmlString(
            "./files/temp",
            "temp(${
                LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                )
            }).pdf",
            htmlString,
            arrayListOf(
                "/static/resource_global/fonts/for_itext/NanumGothic.ttf",
                "/static/resource_global/fonts/for_itext/NanumMyeongjo.ttf"
            )
        )

        httpServletResponse.setHeader("api-result-code", "0")
    }
}