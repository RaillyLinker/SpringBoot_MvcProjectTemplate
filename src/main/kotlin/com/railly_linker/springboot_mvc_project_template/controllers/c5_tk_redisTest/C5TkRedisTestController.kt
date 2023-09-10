package com.railly_linker.springboot_mvc_project_template.controllers.c5_tk_redisTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*

@Tag(name = "/tk/redis-test APIs", description = "C5. Redis 에 대한 테스트 API 컨트롤러")
@RestController
@RequestMapping("/tk/redis-test")
class C5TkRedisTestController(
    private val service: C5TkRedisTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1. Redis 입력 테스트",
        description = "Redis 정보 입력 테스트용 API\n\n" +
                "(api-result-code)\n\n" +
                "ok : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/redis1-test")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody inputVo: Api1InputVo
    ): Api1OutputVo {
        return service.api1(httpServletResponse, inputVo)
    }

    data class Api1InputVo(
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )

    data class Api1OutputVo(
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )


    ////
    @Operation(
        summary = "N2. Redis 정보 조회 테스트",
        description = "Redis 정보 조회 테스트용 API\n\n" +
                "(api-result-code)\n\n" +
                "ok : 정상 동작\n\n" +
                "1 : 정보가 없습니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/redis1-tests")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api2OutputVo? {
        return service.api2(
            httpServletResponse
        )
    }

    data class Api2OutputVo(
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )


    ////
    @Operation(
        summary = "N3. Redis 삭제 테스트",
        description = "Redis 정보 삭제 테스트용 API\n\n" +
                "(api-result-code)\n\n" +
                "ok : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @DeleteMapping("/redis1-test")
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        return service.api3(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N4. Redis 트랜젝션 테스트",
        description = "Redis 트랜젝션 테스트용 API\n\n" +
                "Redis 에 데이터를 저장한 직후 바로 Exception 을 발생시킵니다.\n\n" +
                "이 API 를 사용하고 바로 데이터 조회를 했을 때, 데이터가 없다고 나오면 Rollback 이 동작한 것입니다.\n\n" +
                "(api-result-code)\n\n" +
                "ok : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/redis1-test-transaction")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody inputVo: Api4InputVo
    ) {
        return service.api4(httpServletResponse, inputVo)
    }

    data class Api4InputVo(
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )


    ////
    @Operation(
        summary = "N5. Redis 트랜젝션 미적용 테스트",
        description = "Redis 트랜젝션 미적용 테스트용 API\n\n" +
                "Redis 에 데이터를 저장한 직후 바로 Exception 을 발생시킵니다.\n\n" +
                "트랜젝션을 설정하지 않았으니, 에러가 나도 데이터가 저장되어 있습니다.\n\n" +
                "(api-result-code)\n\n" +
                "ok : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/redis1-test-no-transaction")
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody inputVo: Api5InputVo
    ) {
        return service.api5(httpServletResponse, inputVo)
    }

    data class Api5InputVo(
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )
}