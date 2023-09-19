package com.railly_linker.springboot_mvc_project_template.controllers.c2_tk_test

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "/tk/test APIs", description = "C2. 테스트 API 컨트롤러")
@RestController
@RequestMapping("/tk/test")
class C2TkTestController(
    private val service: C2TkTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1. 이메일 발송 테스트",
        description = "이메일 발송 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/send-email", consumes = ["multipart/form-data"])
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api1InputVo
    ) {
        service.api1(httpServletResponse, inputVo)
    }

    data class Api1InputVo(
        @Schema(description = "수신자 이메일 배열", required = true, example = "[\"test1@gmail.com\"]")
        @JsonProperty("receiverEmailAddressList")
        val receiverEmailAddressList: List<String>,
        @Schema(description = "참조자 이메일 배열", required = false, example = "[\"test2@gmail.com\"]")
        @JsonProperty("carbonCopyEmailAddressList")
        val carbonCopyEmailAddressList: List<String>?,
        @Schema(description = "발신자명", required = true, example = "Railly Linker")
        @JsonProperty("senderName")
        val senderName: String,
        @Schema(description = "제목", required = true, example = "테스트 이메일")
        @JsonProperty("subject")
        val subject: String,
        @Schema(description = "메세지", required = true, example = "테스트 이메일을 송신했습니다.")
        @JsonProperty("message")
        val message: String,
        @Schema(description = "첨부 파일 리스트", required = false)
        @JsonProperty("multipartFileList")
        val multipartFileList: List<MultipartFile>?
    )


    ////
    @Operation(
        summary = "N2. HTML 이메일 발송 테스트",
        description = "HTML 로 이루어진 이메일 발송 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/send-html-email", consumes = ["multipart/form-data"])
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api2InputVo
    ) {
        service.api2(httpServletResponse, inputVo)
    }

    data class Api2InputVo(
        @Schema(description = "수신자 이메일 배열", required = true, example = "[\"test1@gmail.com\"]")
        @JsonProperty("receiverEmailAddressList")
        val receiverEmailAddressList: List<String>,
        @Schema(description = "참조자 이메일 배열", required = false, example = "[\"test2@gmail.com\"]")
        @JsonProperty("carbonCopyEmailAddressList")
        val carbonCopyEmailAddressList: List<String>?,
        @Schema(description = "발신자명", required = true, example = "Railly Linker")
        @JsonProperty("senderName")
        val senderName: String,
        @Schema(description = "제목", required = true, example = "테스트 이메일")
        @JsonProperty("subject")
        val subject: String,
        @Schema(description = "메세지", required = true, example = "테스트 이메일을 송신했습니다.")
        @JsonProperty("message")
        val message: String,
        @Schema(description = "첨부 파일 리스트", required = false)
        @JsonProperty("multipartFileList")
        val multipartFileList: List<MultipartFile>?
    )


    ////
    @Operation(
        summary = "N3. Naver API SMS 발송 샘플",
        description = "Naver API 를 사용한 SMS 발송 샘플\n\n" +
                "Service 에서 사용하는 Naver SMS 발송 유틸 내의 개인정보를 변경해야 사용 가능\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/naver-sms-sample")
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api3InputVo
    ) {
        return service.api3(httpServletResponse, inputVo)
    }

    data class Api3InputVo(
        @Schema(description = "SMS 수신측 휴대전화 번호", required = true, example = "82)010-1111-1111")
        @JsonProperty("phoneNumber")
        val phoneNumber: String,
        @Schema(description = "SMS 메세지", required = true, example = "테스트 메세지 발송입니다.")
        @JsonProperty("smsMessage")
        val smsMessage: String
    )


    ////
    @Operation(
        summary = "N4. FCM Push 테스트",
        description = "FCM Push 메세지 발신\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/fcm-push/push-message")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api4InputVo
    ) {
        return service.api4(httpServletResponse, inputVo)
    }

    data class Api4InputVo(
        @Schema(
            description = "푸시 메세지 발신 서버 키 : FCM 프로젝트 생성 후 가져오기",
            required = true,
            example = "AAAAx-a8CLQ:APA91bEgU_DIwgvpiK4jiTIMt1ah16UCSdVrVOIVEZ2407i6hasZq3B0EuppcuqEL-pE5PsBl19hxOUt0miOYcW241-WzdsU1lZpXNdOzoNKOex2GlUQcZiioJDk3C2hlZcgq4Yf6EP4"
        )
        @JsonProperty("senderServerKey")
        val senderServerKey: String,
        @Schema(
            description = "푸시 메세지 수신 디바이스 토큰 리스트 : FCM 프로젝트에 소속된 FCM 설정을 마친 디바이스에서 가져오기",
            required = true,
            example = "[\"aaaaaaa\",\"bbbbbbb\"]"
        )
        @JsonProperty("receiverDeviceTokenList")
        val receiverDeviceTokenList: List<String>,
        @Schema(
            description = "노티피케이션 타이틀",
            required = true,
            example = "타이틀"
        )
        @JsonProperty("notificationTitle")
        val notificationTitle: String,
        @Schema(
            description = "노티피케이션 본문",
            required = true,
            example = "본문"
        )
        @JsonProperty("notificationContent")
        val notificationContent: String
    )
}