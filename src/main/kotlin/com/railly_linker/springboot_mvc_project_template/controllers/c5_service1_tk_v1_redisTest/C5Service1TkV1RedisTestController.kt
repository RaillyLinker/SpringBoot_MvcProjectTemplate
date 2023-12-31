package com.railly_linker.springboot_mvc_project_template.controllers.c5_service1_tk_v1_redisTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*

@Tag(name = "/service1/tk/v1/redis-test APIs", description = "C5 : Redis 에 대한 테스트 API 컨트롤러")
@RestController
@RequestMapping("/service1/tk/v1/redis-test")
class C5Service1TkV1RedisTestController(
    private val service: C5Service1TkV1RedisTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : Redis Key-Value 입력 테스트",
        description = "Redis 테이블에 Key-Value 를 입력합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping("/test")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody inputVo: Api1InputVo
    ) {
        service.api1(httpServletResponse, inputVo)
    }

    data class Api1InputVo(
        @Schema(description = "redis 키", required = true, example = "test_key")
        @JsonProperty("key")
        val key: String,
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )


    ////
    @Operation(
        summary = "N2 : Redis Key-Value 조회 테스트",
        description = "Redis Table 에 저장된 Key-Value 를 조회합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : key 에 저장된 데이터가 없음"
    )
    @GetMapping("/test")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "key", description = "redis 키", example = "test_key")
        @RequestParam("key")
        key: String
    ): Api2OutputVo? {
        return service.api2(httpServletResponse, key)
    }

    data class Api2OutputVo(
        @Schema(description = "Table 이름", required = true, example = "Redis1_Test")
        @JsonProperty("tableName")
        val tableName: String,
        @Schema(description = "Key", required = true, example = "testing")
        @JsonProperty("key")
        val key: String,
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )


    ////
    @Operation(
        summary = "N3 : Redis Key-Value 모두 조회 테스트",
        description = "Redis Table 에 저장된 모든 Key-Value 를 조회합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @GetMapping("/tests")
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Api3OutputVo? {
        return service.api3(
            httpServletResponse
        )
    }

    data class Api3OutputVo(
        @Schema(description = "Table 이름", required = true, example = "Redis1_Test")
        @JsonProperty("tableName")
        val tableName: String,

        @Schema(description = "Key-Value 리스트", required = true)
        @JsonProperty("keyValueList")
        val keyValueList: List<KeyValueVo>,
    ) {
        @Schema(description = "Key-Value 객체")
        data class KeyValueVo(
            @Schema(description = "Key", required = true, example = "testing")
            @JsonProperty("key")
            val key: String,
            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            val content: String,
            @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
            @JsonProperty("expirationMs")
            val expirationMs: Long
        )
    }


    ////
    @Operation(
        summary = "N4 : Redis Key-Value 삭제 테스트",
        description = "Redis Table 에 저장된 Key 를 삭제합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @DeleteMapping("/test")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "key", description = "redis 키", example = "test_key")
        @RequestParam("key")
        key: String
    ) {
        return service.api4(httpServletResponse, key)
    }


    ////
    @Operation(
        summary = "N5 : Redis Key-Value 모두 삭제 테스트",
        description = "Redis Table 에 저장된 모든 Key 를 삭제합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @DeleteMapping("/test-all")
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        return service.api5(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N6 : Redis 트랜젝션 적용 테스트",
        description = "Redis 트랜젝션 테스트용 API\n\n" +
                "Redis 에 데이터 저장 직후 바로 Exception 을 발생시킵니다.\n\n" +
                "이 API 를 사용하고 바로 데이터 조회를 했을 때, 데이터가 없다고 나오면 Rollback 이 동작한 것입니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping("/test-transaction")
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody inputVo: Api6InputVo
    ) {
        return service.api6(httpServletResponse, inputVo)
    }

    data class Api6InputVo(
        @Schema(description = "redis 키", required = true, example = "test_key")
        @JsonProperty("key")
        val key: String,
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )


    ////
    @Operation(
        summary = "N7 : Redis 트랜젝션 미적용 테스트",
        description = "Redis 트랜젝션 미적용 테스트용 API\n\n" +
                "Redis 에 데이터 저장 직후 바로 Exception 을 발생시킵니다.\n\n" +
                "트랜젝션을 적용하지 않았으니 데이터는 저장될 것입니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping("/test-no-transaction")
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody inputVo: Api7InputVo
    ) {
        return service.api7(httpServletResponse, inputVo)
    }

    data class Api7InputVo(
        @Schema(description = "redis 키", required = true, example = "test_key")
        @JsonProperty("key")
        val key: String,
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "데이터 만료시간(밀리 초)", required = true, example = "12000")
        @JsonProperty("expirationMs")
        val expirationMs: Long
    )
}