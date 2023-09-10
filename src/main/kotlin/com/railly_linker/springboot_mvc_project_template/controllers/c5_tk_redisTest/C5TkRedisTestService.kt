package com.railly_linker.springboot_mvc_project_template.controllers.c5_tk_redisTest

import com.railly_linker.springboot_mvc_project_template.annotations.CustomRedisTransactional
import com.railly_linker.springboot_mvc_project_template.configurations.RedisConfig
import com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.redis_keys.Redis1_Test
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class C5TkRedisTestService(
    private val redis1Test: Redis1_Test
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 Unknown 반환))
    // if (activeProfile == "prod80"){ // 배포 서버
    // }
    @Value("\${spring.profiles.active:Unknown}")
    private lateinit var activeProfile: String


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: C5TkRedisTestController.Api1InputVo
    ): C5TkRedisTestController.Api1OutputVo {
        // 입력 테스트
        redis1Test.putValue(
            Redis1_Test.RedisValueVo(
                inputVo.content,
                Redis1_Test.RedisValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.RedisValueVo.InnerVo("testObject1", false),
                    Redis1_Test.RedisValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        // 키 조회 테스트
        val searchOneResult = redis1Test.getValue()!!

        httpServletResponse.setHeader("api-result-code", "ok")
        return C5TkRedisTestController.Api1OutputVo(
            searchOneResult.value.content,
            searchOneResult.expireTimeMs
        )
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse): C5TkRedisTestController.Api2OutputVo? {
        // 전체 조회 테스트
        val result = redis1Test.getValue()

        return if (result == null) {
            httpServletResponse.setHeader("api-result-code", "1")
            null
        } else {
            httpServletResponse.setHeader("api-result-code", "ok")
            C5TkRedisTestController.Api2OutputVo(
                result.value.content,
                result.expireTimeMs
            )
        }
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse) {
        redis1Test.deleteValue()
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    @CustomRedisTransactional(["${RedisConfig.TN_REDIS1}:${Redis1_Test.KEY_NAME}"])
    fun api4(httpServletResponse: HttpServletResponse, inputVo: C5TkRedisTestController.Api4InputVo) {
        // 입력 테스트
        redis1Test.putValue(
            Redis1_Test.RedisValueVo(
                inputVo.content,
                Redis1_Test.RedisValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.RedisValueVo.InnerVo("testObject1", false),
                    Redis1_Test.RedisValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        // 테스트 예외 발생
        throw Exception("test exception")

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse, inputVo: C5TkRedisTestController.Api5InputVo) {
        // 입력 테스트
        redis1Test.putValue(
            Redis1_Test.RedisValueVo(
                inputVo.content,
                Redis1_Test.RedisValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.RedisValueVo.InnerVo("testObject1", false),
                    Redis1_Test.RedisValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        // 테스트 예외 발생
        throw Exception("test exception")

        httpServletResponse.setHeader("api-result-code", "ok")
    }
}