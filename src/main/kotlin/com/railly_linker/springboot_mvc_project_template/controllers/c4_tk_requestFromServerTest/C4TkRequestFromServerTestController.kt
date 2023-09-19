package com.railly_linker.springboot_mvc_project_template.controllers.c4_tk_requestFromServerTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "/tk/request-from-server-test APIs", description = "C4. 서버에서 요청을 보내는 테스트 API 컨트롤러")
@RestController
@RequestMapping("/tk/request-from-server-test")
class C4TkRequestFromServerTestController(
    private val service: C4TkRequestFromServerTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : /tk/request-test 요청 테스트 API",
        description = "/tk/request-test 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api1(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N2 : /tk/request-test/redirect-to-blank 요청 테스트 API",
        description = "/tk/request-test/redirect-to-blank 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/redirect-to-blank")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api2(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N3 : /tk/request-test/forward-to-blank 요청 테스트 API",
        description = "/tk/request-test/forward-to-blank 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/forward-to-blank")
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api3(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N4 : /tk/request-test/get-request 요청 테스트 API",
        description = "/tk/request-test/get-request 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/get-request")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api4OutputVo? {
        return service.api4(httpServletResponse)
    }

    data class Api4OutputVo(
        @Schema(description = "입력한 String Query 파라미터", required = true, example = "testString")
        @JsonProperty("queryParamString")
        val queryParamString: String,
        @Schema(description = "입력한 String Nullable Query 파라미터", required = false, example = "testString")
        @JsonProperty("queryParamStringNullable")
        val queryParamStringNullable: String?,
        @Schema(description = "입력한 Int Query 파라미터", required = true, example = "1")
        @JsonProperty("queryParamInt")
        val queryParamInt: Int,
        @Schema(description = "입력한 Int Nullable Query 파라미터", required = false, example = "1")
        @JsonProperty("queryParamIntNullable")
        val queryParamIntNullable: Int?,
        @Schema(description = "입력한 Double Query 파라미터", required = true, example = "1.1")
        @JsonProperty("queryParamDouble")
        val queryParamDouble: Double,
        @Schema(description = "입력한 Double Nullable Query 파라미터", required = false, example = "1.1")
        @JsonProperty("queryParamDoubleNullable")
        val queryParamDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Query 파라미터", required = true, example = "true")
        @JsonProperty("queryParamBoolean")
        val queryParamBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Query 파라미터", required = false, example = "true")
        @JsonProperty("queryParamBooleanNullable")
        val queryParamBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Query 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("queryParamStringList")
        val queryParamStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Query 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("queryParamStringListNullable")
        val queryParamStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N5 : /tk/request-test/get-request/{pathParamInt} 요청 테스트 API",
        description = "/tk/request-test/get-request/{pathParamInt} 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/get-request-path-param")
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api5OutputVo? {
        return service.api5(httpServletResponse)
    }

    data class Api5OutputVo(
        @Schema(description = "입력한 Int Path 파라미터", required = true, example = "1")
        @JsonProperty("pathParamInt")
        val pathParamInt: Int
    )

    ////
    @Operation(
        summary = "N6 : /tk/request-test/post-request-application-json 요청 테스트 API",
        description = "/tk/request-test/post-request-application-json 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/post-request-application-json")
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api6OutputVo? {
        return service.api6(httpServletResponse)
    }

    data class Api6OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestBodyString")
        val requestBodyString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestBodyStringNullable")
        val requestBodyStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestBodyInt")
        val requestBodyInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestBodyIntNullable")
        val requestBodyIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestBodyDouble")
        val requestBodyDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestBodyDoubleNullable")
        val requestBodyDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestBodyBoolean")
        val requestBodyBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestBodyBooleanNullable")
        val requestBodyBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestBodyStringList")
        val requestBodyStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestBodyStringListNullable")
        val requestBodyStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N7 : /tk/request-test/post-request-x-www-form-urlencoded 요청 테스트 API",
        description = "/tk/request-test/post-request-x-www-form-urlencoded 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/post-request-x-www-form-urlencoded")
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api7OutputVo? {
        return service.api7(httpServletResponse)
    }

    data class Api7OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N8 : /tk/request-test/post-request-multipart-form-data 요청 테스트 API",
        description = "/tk/request-test/post-request-multipart-form-data 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/post-request-multipart-form-data")
    fun api8(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api8OutputVo? {
        return service.api8(httpServletResponse)
    }

    data class Api8OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N9 : /tk/request-test/post-request-multipart-form-data2 요청 테스트 API",
        description = "/tk/request-test/post-request-multipart-form-data2 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/post-request-multipart-form-data2")
    fun api9(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api9OutputVo? {
        return service.api9(httpServletResponse)
    }

    data class Api9OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N10 : /tk/request-test/post-request-multipart-form-data-json 요청 테스트 API",
        description = "/tk/request-test/post-request-multipart-form-data-json 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/post-request-multipart-form-data-json")
    fun api10(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api10OutputVo? {
        return service.api10(httpServletResponse)
    }

    data class Api10OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N11 : /tk/request-test/generate-error 요청 테스트 API",
        description = "/tk/request-test/generate-error 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : api-result-code 가 반환되지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/generate-error")
    fun api11(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api11(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N12 : /tk/request-test/api-result-code-test 요청 테스트 API",
        description = "/tk/request-test/api-result-code-test 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : errorType 을 A 로 보냈습니다.\n\n" +
                "2 : errorType 을 B 로 보냈습니다.\n\n" +
                "3 : errorType 을 C 로 보냈습니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/api-result-code-test")
    fun api12(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api12(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N13 : /tk/request-test/generate-time-out-error 요청 테스트 API",
        description = "/tk/request-test/generate-time-out-error 로 요청을 보냅니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 타임아웃이 발생함",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/tk/request-test/generate-time-out-error")
    fun api13(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "delayTimeSec", description = "지연 시간(초)", example = "1")
        @RequestParam("delayTimeSec")
        delayTimeSec: Int
    ) {
        service.api13(httpServletResponse, delayTimeSec)
    }
}