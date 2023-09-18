package com.railly_linker.springboot_mvc_project_template.controllers.c3_tk_requestTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView


@Tag(name = "/tk/request-test APIs", description = "C3. 요청 / 응답에 대한 테스트 API 컨트롤러")
@RestController
@RequestMapping("/tk/request-test")
class C3TkRequestTestController(
    private val service: C3TkRequestTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1. 기본 요청 테스트 API",
        description = "이 API 를 요청하면 현재 실행중인 프로필 이름을 반환합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("", "/")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): String? {
        return service.api1(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N2. 요청 Redirect 테스트 API",
        description = "이 API 를 요청하면 /tk/request-test 로 Redirect 됩니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/redirect-to-blank")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return service.api2(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N3. 요청 Forward 테스트 API",
        description = "이 API 를 요청하면 /tk/request-test 로 Forward 됩니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/forward-to-blank")
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return service.api3(httpServletResponse)
    }

    ////
    @Operation(
        summary = "N4. Get 요청 테스트 (Query Parameter)",
        description = "Query Parameter 를 받는 Get 메소드 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/get-request")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "queryParamString", description = "String Query 파라미터", example = "testString")
        @RequestParam("queryParamString")
        queryParamString: String,
        @Parameter(
            name = "queryParamStringNullable",
            description = "String Query 파라미터 Nullable",
            example = "testString"
        )
        @RequestParam("queryParamStringNullable")
        queryParamStringNullable: String?,
        @Parameter(name = "queryParamInt", description = "Int Query 파라미터", example = "1")
        @RequestParam("queryParamInt")
        queryParamInt: Int,
        @Parameter(name = "queryParamIntNullable", description = "Int Query 파라미터 Nullable", example = "1")
        @RequestParam("queryParamIntNullable")
        queryParamIntNullable: Int?,
        @Parameter(name = "queryParamDouble", description = "Double Query 파라미터", example = "1.1")
        @RequestParam("queryParamDouble")
        queryParamDouble: Double,
        @Parameter(name = "queryParamDoubleNullable", description = "Double Query 파라미터 Nullable", example = "1.1")
        @RequestParam("queryParamDoubleNullable")
        queryParamDoubleNullable: Double?,
        @Parameter(name = "queryParamBoolean", description = "Boolean Query 파라미터", example = "true")
        @RequestParam("queryParamBoolean")
        queryParamBoolean: Boolean,
        @Parameter(name = "queryParamBooleanNullable", description = "Boolean Query 파라미터 Nullable", example = "true")
        @RequestParam("queryParamBooleanNullable")
        queryParamBooleanNullable: Boolean?,
        @Parameter(
            name = "queryParamStringList",
            description = "StringList Query 파라미터",
            example = "[\"testString1\", \"testString2\"]"
        )
        @RequestParam("queryParamStringList")
        queryParamStringList: List<String>,
        @Parameter(
            name = "queryParamStringListNullable", description = "StringList Query 파라미터 Nullable",
            example = "[\"testString1\", \"testString2\"]"
        )
        @RequestParam("queryParamStringListNullable")
        queryParamStringListNullable: List<String>?
    ): Api4OutputVo? {
        return service.api4(
            httpServletResponse,
            queryParamString,
            queryParamStringNullable,
            queryParamInt,
            queryParamIntNullable,
            queryParamDouble,
            queryParamDoubleNullable,
            queryParamBoolean,
            queryParamBooleanNullable,
            queryParamStringList,
            queryParamStringListNullable
        )
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
        summary = "N5. Get 요청 테스트 (Path Parameter)",
        description = "Path Parameter 를 받는 Get 메소드 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/get-request/{pathParamInt}")
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "pathParamInt", description = "Int Path 파라미터", example = "1")
        @PathVariable("pathParamInt")
        pathParamInt: Int
    ): Api5OutputVo? {
        return service.api5(
            httpServletResponse,
            pathParamInt
        )
    }

    data class Api5OutputVo(
        @Schema(description = "입력한 Int Path 파라미터", required = true, example = "1")
        @JsonProperty("pathParamInt")
        val pathParamInt: Int
    )


    ////
    @Operation(
        summary = "N6. Post 요청 테스트 (application-json)",
        description = "application-json 형태의 Request Body 를 받는 Post 메소드 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/post-request-application-json")
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api6InputVo
    ): Api6OutputVo? {
        return service.api6(
            httpServletResponse,
            inputVo
        )
    }

    data class Api6InputVo(
        @Schema(description = "String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestBodyString")
        val requestBodyString: String,
        @Schema(description = "String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestBodyStringNullable")
        val requestBodyStringNullable: String?,
        @Schema(description = "Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestBodyInt")
        val requestBodyInt: Int,
        @Schema(description = "Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestBodyIntNullable")
        val requestBodyIntNullable: Int?,
        @Schema(description = "Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestBodyDouble")
        val requestBodyDouble: Double,
        @Schema(description = "Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestBodyDoubleNullable")
        val requestBodyDoubleNullable: Double?,
        @Schema(description = "Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestBodyBoolean")
        val requestBodyBoolean: Boolean,
        @Schema(description = "Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestBodyBooleanNullable")
        val requestBodyBooleanNullable: Boolean?,
        @Schema(description = "StringList Body 파라미터", required = true, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestBodyStringList")
        val requestBodyStringList: List<String>,
        @Schema(
            description = "StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestBodyStringListNullable")
        val requestBodyStringListNullable: List<String>?
    )

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
        summary = "N7. Post 요청 테스트 (x-www-form-urlencoded)",
        description = "x-www-form-urlencoded 형태의 Request Body 를 받는 Post 메소드 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/post-request-x-www-form-urlencoded", consumes = ["application/x-www-form-urlencoded"])
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api7InputVo
    ): Api7OutputVo? {
        return service.api7(httpServletResponse, inputVo)
    }

    data class Api7InputVo(
        @Schema(description = "String Form 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "String Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "Int Form 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "Int Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "Double Form 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "Double Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "Boolean Form 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "Boolean Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(description = "StringList Form 파라미터", required = true, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(description = "StringList Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    data class Api7OutputVo(
        @Schema(description = "입력한 String Form 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Form 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Form 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Form 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Form 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Form 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Form 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Form 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Form 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )


    ////
    @Operation(
        summary = "N8. Post 요청 테스트 (multipart/form-data)",
        description = "multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트\n\n" +
                "MultipartFile 파라미터가 null 이 아니라면 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/post-request-multipart-form-data", consumes = ["multipart/form-data"])
    fun api8(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api8InputVo
    ): Api8OutputVo? {
        return service.api8(httpServletResponse, inputVo)
    }

    data class Api8InputVo(
        @Schema(description = "String Form 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "String Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "Int Form 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "Int Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "Double Form 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "Double Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "Boolean Form 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "Boolean Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(description = "StringList Form 파라미터", required = true, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(description = "StringList Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?,
        @Schema(description = "멀티 파트 파일", required = true)
        @JsonProperty("multipartFile")
        val multipartFile: MultipartFile,
        @Schema(description = "멀티 파트 파일 Nullable")
        @JsonProperty("multipartFileNullable")
        val multipartFileNullable: MultipartFile?
    )

    data class Api8OutputVo(
        @Schema(description = "입력한 String Form 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Form 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Form 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Form 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Form 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Form 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Form 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Form 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Form 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )


    ////
    @Operation(
        summary = "N9. Post 요청 테스트2 (multipart/form-data)",
        description = "multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)\n\n" +
                "파일 리스트가 null 이 아니라면 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/post-request-multipart-form-data2", consumes = ["multipart/form-data"])
    fun api9(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api9InputVo
    ): Api9OutputVo? {
        return service.api9(httpServletResponse, inputVo)
    }

    data class Api9InputVo(
        @Schema(description = "String Form 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "String Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "Int Form 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "Int Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "Double Form 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "Double Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "Boolean Form 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "Boolean Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(description = "StringList Form 파라미터", required = true, example = "[\"testString1\", \"testString2\"]")
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(description = "StringList Nullable Form 파라미터", required = false)
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?,
        @Schema(description = "멀티 파트 파일", required = true)
        @JsonProperty("multipartFileList")
        val multipartFileList: List<MultipartFile>,
        @Schema(description = "멀티 파트 파일 Nullable", required = false)
        @JsonProperty("multipartFileNullableList")
        val multipartFileNullableList: List<MultipartFile>?
    )

    data class Api9OutputVo(
        @Schema(description = "입력한 String Form 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Form 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Form 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Form 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Form 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Form 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Form 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Form 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Form 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )


    ////
    @Operation(
        summary = "N10. Post 요청 테스트 (multipart/form-data - JsonString)",
        description = "multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트\n\n" +
                "Form Data 의 Input Body 에는 Object 리스트 타입은 사용 불가능입니다.\n\n" +
                "Object 리스트 타입을 사용한다면, Json String 타입으로 객체를 받아서 파싱하여 사용하는 방식을 사용합니다.\n\n" +
                "아래 예시에서는 모두 JsonString 형식으로 만들었지만, ObjectList 타입만 이런식으로 처리하세요.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/post-request-multipart-form-data-json", consumes = ["multipart/form-data"])
    fun api10(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api10InputVo
    ): Api10OutputVo? {
        return service.api10(httpServletResponse, inputVo)
    }

    data class Api10InputVo(
        @Schema(
            description = "json 형식의 문자열\n\n" +
                    "        data class InputJsonObject(\n" +
                    "            @Schema(description = \"String Form 파라미터\", required = true, example = \"testString\")\n" +
                    "            @JsonProperty(\"requestFormString\")\n" +
                    "            val requestFormString: String,\n" +
                    "            @Schema(description = \"String Nullable Form 파라미터\", required = false, example = \"testString\")\n" +
                    "            @JsonProperty(\"requestFormStringNullable\")\n" +
                    "            val requestFormStringNullable: String?,\n" +
                    "            @Schema(description = \"Int Form 파라미터\", required = true, example = \"1\")\n" +
                    "            @JsonProperty(\"requestFormInt\")\n" +
                    "            val requestFormInt: Int,\n" +
                    "            @Schema(description = \"Int Nullable Form 파라미터\", required = false, example = \"1\")\n" +
                    "            @JsonProperty(\"requestFormIntNullable\")\n" +
                    "            val requestFormIntNullable: Int?,\n" +
                    "            @Schema(description = \"Double Form 파라미터\", required = true, example = \"1.1\")\n" +
                    "            @JsonProperty(\"requestFormDouble\")\n" +
                    "            val requestFormDouble: Double,\n" +
                    "            @Schema(description = \"Double Nullable Form 파라미터\", required = false, example = \"1.1\")\n" +
                    "            @JsonProperty(\"requestFormDoubleNullable\")\n" +
                    "            val requestFormDoubleNullable: Double?,\n" +
                    "            @Schema(description = \"Boolean Form 파라미터\", required = true, example = \"true\")\n" +
                    "            @JsonProperty(\"requestFormBoolean\")\n" +
                    "            val requestFormBoolean: Boolean,\n" +
                    "            @Schema(description = \"Boolean Nullable Form 파라미터\", required = false, example = \"true\")\n" +
                    "            @JsonProperty(\"requestFormBooleanNullable\")\n" +
                    "            val requestFormBooleanNullable: Boolean?,\n" +
                    "            @Schema(description = \"StringList Form 파라미터\", required = true, example = \"[\\\"testString1\\\", \\\"testString2\\\"]\")\n" +
                    "            @JsonProperty(\"requestFormStringList\")\n" +
                    "            val requestFormStringList: List<String>,\n" +
                    "            @Schema(\n" +
                    "                description = \"StringList Nullable Form 파라미터\",\n" +
                    "                required = false,\n" +
                    "                example = \"[\\\"testString1\\\", \\\"testString2\\\"]\"\n" +
                    "            )\n" +
                    "            @JsonProperty(\"requestFormStringListNullable\")\n" +
                    "            val requestFormStringListNullable: List<String>?\n" +
                    "        )",
            required = true,
            example = "{\n" +
                    "  \"requestFormString\": \"testString\",\n" +
                    "  \"requestFormStringNullable\": null,\n" +
                    "  \"requestFormInt\": 1,\n" +
                    "  \"requestFormIntNullable\": null,\n" +
                    "  \"requestFormDouble\": 1.1,\n" +
                    "  \"requestFormDoubleNullable\": null,\n" +
                    "  \"requestFormBoolean\": true,\n" +
                    "  \"requestFormBooleanNullable\": null,\n" +
                    "  \"requestFormStringList\": [\n" +
                    "    \"testString1\",\n" +
                    "    \"testString2\"\n" +
                    "  ],\n" +
                    "  \"requestFormStringListNullable\": null\n" +
                    "}"
        )
        @JsonProperty("jsonString")
        val jsonString: String,
        @Schema(description = "멀티 파트 파일", required = true)
        @JsonProperty("multipartFile")
        val multipartFile: MultipartFile,
        @Schema(description = "멀티 파트 파일 Nullable", required = false)
        @JsonProperty("multipartFileNullable")
        val multipartFileNullable: MultipartFile?
    ) {
        @Schema(description = "Json String 스키마")
        data class InputJsonObject(
            @Schema(description = "String Form 파라미터", required = true, example = "testString")
            @JsonProperty("requestFormString")
            val requestFormString: String,
            @Schema(description = "String Nullable Form 파라미터", required = false, example = "testString")
            @JsonProperty("requestFormStringNullable")
            val requestFormStringNullable: String?,
            @Schema(description = "Int Form 파라미터", required = true, example = "1")
            @JsonProperty("requestFormInt")
            val requestFormInt: Int,
            @Schema(description = "Int Nullable Form 파라미터", required = false, example = "1")
            @JsonProperty("requestFormIntNullable")
            val requestFormIntNullable: Int?,
            @Schema(description = "Double Form 파라미터", required = true, example = "1.1")
            @JsonProperty("requestFormDouble")
            val requestFormDouble: Double,
            @Schema(description = "Double Nullable Form 파라미터", required = false, example = "1.1")
            @JsonProperty("requestFormDoubleNullable")
            val requestFormDoubleNullable: Double?,
            @Schema(description = "Boolean Form 파라미터", required = true, example = "true")
            @JsonProperty("requestFormBoolean")
            val requestFormBoolean: Boolean,
            @Schema(description = "Boolean Nullable Form 파라미터", required = false, example = "true")
            @JsonProperty("requestFormBooleanNullable")
            val requestFormBooleanNullable: Boolean?,
            @Schema(
                description = "StringList Form 파라미터",
                required = true,
                example = "[\"testString1\", \"testString2\"]"
            )
            @JsonProperty("requestFormStringList")
            val requestFormStringList: List<String>,
            @Schema(
                description = "StringList Nullable Form 파라미터",
                required = false,
                example = "[\"testString1\", \"testString2\"]"
            )
            @JsonProperty("requestFormStringListNullable")
            val requestFormStringListNullable: List<String>?
        )
    }

    data class Api10OutputVo(
        @Schema(description = "입력한 String Form 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Form 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Form 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Form 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Form 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Form 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Form 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Form 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Form 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Form 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )


    ////
    @Operation(
        summary = "N11. 인위적 에러 발생 테스트",
        description = "요청 받으면 인위적인 서버 에러를 발생시킵니다.(Http Response Status 500)\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/generate-error")
    fun api11(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api11(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N12. 결과 코드 발생 테스트",
        description = "Response Header 에 api-result-code 를 반환하는 테스트 API\n\n" +
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
    @PostMapping("/api-result-code-test")
    fun api12(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "errorType", description = "정상적이지 않은 상황을 만들도록 가정된 변수입니다.", example = "A")
        @RequestParam("errorType")
        errorType: Api12ErrorTypeEnum?
    ) {
        service.api12(httpServletResponse, errorType)
    }

    enum class Api12ErrorTypeEnum {
        A,
        B,
        C
    }


    ////
    @Operation(
        summary = "N13. 인위적 타임아웃 에러 발생 테스트",
        description = "타임아웃 에러를 발생시키기 위해 임의로 응답 시간을 지연시킵니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/generate-time-out-error")
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