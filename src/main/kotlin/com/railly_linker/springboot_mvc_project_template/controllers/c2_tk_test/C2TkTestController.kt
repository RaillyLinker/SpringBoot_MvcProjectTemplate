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


    ////
    @Operation(
        summary = "N5. 액셀 파일을 받아서 해석 후 데이터 반환",
        description = "액셀 파일을 받아서 해석 후 데이터 반환\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/read-excel", consumes = ["multipart/form-data"])
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api5InputVo
    ): Api5OutputVo? {
        return service.api5(httpServletResponse, inputVo)
    }

    data class Api5InputVo(
        @Schema(description = "가져오려는 시트 인덱스 (0부터 시작)", required = true, example = "0")
        @JsonProperty("sheetIdx")
        val sheetIdx: Int,
        @Schema(description = "가져올 행 범위 시작 인덱스 (0부터 시작)", required = true, example = "0")
        @JsonProperty("rowRangeStartIdx")
        val rowRangeStartIdx: Int,
        @Schema(description = "가져올 행 범위 끝 인덱스 null 이라면 전부 (0부터 시작)", required = false, example = "10")
        @JsonProperty("rowRangeEndIdx")
        val rowRangeEndIdx: Int?,
        @Schema(description = "가져올 열 범위 인덱스 리스트 null 이라면 전부 (0부터 시작)", required = false, example = "[0, 1, 2]")
        @JsonProperty("columnRangeIdxList")
        val columnRangeIdxList: List<Int>?,
        @Schema(description = "결과 컬럼의 최소 길이 (길이를 넘으면 그대로, 미만이라면 \"\" 로 채움)", required = false, example = "5")
        @JsonProperty("minColumnLength")
        val minColumnLength: Int?,
        @Schema(description = "액셀 파일", required = true)
        @JsonProperty("excelFile")
        val excelFile: MultipartFile
    )

    data class Api5OutputVo(
        @Schema(description = "행 카운트", required = true, example = "1")
        @JsonProperty("rowCount")
        val rowCount: Int,
        @Schema(description = "분석한 객체를 toString 으로 표현한 데이터 String", required = true, example = "[[\"데이터1\", \"데이터2\"]]")
        @JsonProperty("dataString")
        val dataString: String
    )


    ////
    @Operation(
        summary = "N6. 액셀 파일 쓰기",
        description = "받은 데이터를 기반으로 액셀 파일을 만들어 temps 폴더에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/write-excel")
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api6InputVo
    ) {
        service.api6(httpServletResponse, inputVo)
    }

    data class Api6InputVo(
        @Schema(description = "입력할 시트 데이터 리스트 (= 액셀 데이터)", required = true)
        @JsonProperty("inputExcelSheetDataList")
        val inputExcelSheetDataList: List<SheetData>
    ) {
        @Schema(description = "입력할 시트 데이터 : [시트번호][행번호][컬럼번호] == 셀값")
        data class SheetData(
            @Schema(description = "시트 이름", required = true, example = "test")
            @JsonProperty("sheetName")
            val sheetName: String,
            @Schema(
                description = "시트 데이터 [행번호][컬럼번호] == 셀값",
                required = true,
                example = "[[\"test1\", \"test2\"], [\"test3\"]]"
            )
            @JsonProperty("sheetData")
            val sheetData: List<List<String>>
        )
    }
}