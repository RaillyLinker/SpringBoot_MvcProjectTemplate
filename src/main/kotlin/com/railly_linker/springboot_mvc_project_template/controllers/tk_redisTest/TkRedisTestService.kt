package com.railly_linker.springboot_mvc_project_template.controllers.tk_redisTest

import com.railly_linker.springboot_mvc_project_template.annotations.CustomRedisTransactional
import com.railly_linker.springboot_mvc_project_template.redis_repositories.Redis1_TestRepository
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TkRedisTestService(
    private val testRedis1_TestRepository: Redis1_TestRepository
) {
    // <멤버 변수 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)

    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 Unknown 반환))
    // if (activeProfile == "prod80"){ // 배포 서버
    // }
    @Value("\${spring.profiles.active:Unknown}")
    private lateinit var activeProfileMbr: String


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: TkRedisTestController.Api1InputVo
    ): TkRedisTestController.Api1OutputVo {
        // 입력 테스트
        testRedis1_TestRepository.putValue(
            Redis1_TestRepository.RedisValueVo(
                inputVo.content,
                Redis1_TestRepository.RedisValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_TestRepository.RedisValueVo.InnerVo("testObject1", false),
                    Redis1_TestRepository.RedisValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        // 키 조회 테스트
        val searchOneResult = testRedis1_TestRepository.getValue()!!

        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRedisTestController.Api1OutputVo(
            searchOneResult.value.content,
            searchOneResult.expireTimeMs
        )
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse): TkRedisTestController.Api2OutputVo? {
        // 전체 조회 테스트
        val result = testRedis1_TestRepository.getValue()

        return if (result == null) {
            httpServletResponse.setHeader("api-result-code", "1")
            null
        } else {
            httpServletResponse.setHeader("api-result-code", "ok")
            TkRedisTestController.Api2OutputVo(
                result.value.content,
                result.expireTimeMs
            )
        }
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse) {
        testRedis1_TestRepository.deleteValue()
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    @CustomRedisTransactional(["redis1RedisTemplate:Redis1_TestRepository"])
    fun api4(httpServletResponse: HttpServletResponse, inputVo: TkRedisTestController.Api4InputVo) {
        // 입력 테스트
        testRedis1_TestRepository.putValue(
            Redis1_TestRepository.RedisValueVo(
                inputVo.content,
                Redis1_TestRepository.RedisValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_TestRepository.RedisValueVo.InnerVo("testObject1", false),
                    Redis1_TestRepository.RedisValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        // 테스트 예외 발생
        throw Exception("test exception")

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse, inputVo: TkRedisTestController.Api5InputVo) {
        // 입력 테스트
        testRedis1_TestRepository.putValue(
            Redis1_TestRepository.RedisValueVo(
                inputVo.content,
                Redis1_TestRepository.RedisValueVo.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_TestRepository.RedisValueVo.InnerVo("testObject1", false),
                    Redis1_TestRepository.RedisValueVo.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )

        // 테스트 예외 발생
        throw Exception("test exception")

        httpServletResponse.setHeader("api-result-code", "ok")
    }
}