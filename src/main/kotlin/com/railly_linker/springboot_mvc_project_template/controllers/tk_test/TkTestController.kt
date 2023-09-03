package com.railly_linker.springboot_mvc_project_template.controllers.tk_test

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "/tk/test APIs", description = "테스트 API 컨트롤러")
@RestController
@RequestMapping("/tk/test")
class TkTestController(
    private val serviceMbr: TkTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "이메일 발송 테스트",
        description = "이메일 발송 테스트\n\n" +
                "(api-result-code)\n\n" +
                "ok : 정상 동작",
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
        serviceMbr.api1(httpServletResponse, inputVo)
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
        summary = "HTML 이메일 발송 테스트",
        description = "HTML 로 이루어진 이메일 발송 테스트\n\n" +
                "(api-result-code)\n\n" +
                "ok : 정상 동작",
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
        serviceMbr.api2(httpServletResponse, inputVo)
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
}