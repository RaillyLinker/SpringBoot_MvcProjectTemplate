package com.railly_linker.springboot_mvc_project_template.controllers.c5_tk_redisTest

import com.railly_linker.springboot_mvc_project_template.annotations.CustomRedisTransactional
import com.railly_linker.springboot_mvc_project_template.configurations.RedisConfig
import com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.repositories.Redis1_TestRepository
import com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables.Redis1_Test
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class C5TkRedisTestService(
    private val redis1TestRepository: Redis1_TestRepository
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
    ) {
        redis1TestRepository.saveKeyValue(
            inputVo.key,
            Redis1_Test(
                inputVo.content,
                Redis1_Test.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.InnerVo("testObject1", false),
                    Redis1_Test.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse, key: String): C5TkRedisTestController.Api2OutputVo? {
        // 전체 조회 테스트
        val keyValue = redis1TestRepository.findKeyValue(key)

        if (keyValue == null) {
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return C5TkRedisTestController.Api2OutputVo(
            Redis1_Test.TABLE_NAME,
            keyValue.key,
            keyValue.value.content,
            keyValue.expireTimeMs
        )
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse): C5TkRedisTestController.Api3OutputVo? {
        // 전체 조회 테스트
        val keyValueList = redis1TestRepository.findAllKeyValues()

        val testEntityListVoList = ArrayList<C5TkRedisTestController.Api3OutputVo.KeyValueVo>()
        for (keyValue in keyValueList) {
            testEntityListVoList.add(
                C5TkRedisTestController.Api3OutputVo.KeyValueVo(
                    keyValue.key,
                    keyValue.value.content,
                    keyValue.expireTimeMs
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return C5TkRedisTestController.Api3OutputVo(
            Redis1_Test.TABLE_NAME,
            testEntityListVoList
        )
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse, key: String) {
        redis1TestRepository.deleteKeyValue(key)
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse) {
        redis1TestRepository.deleteAllKeyValues()
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    @CustomRedisTransactional(["${RedisConfig.TN_REDIS1}:${Redis1_Test.TABLE_NAME}"])
    fun api6(httpServletResponse: HttpServletResponse, inputVo: C5TkRedisTestController.Api6InputVo) {
        redis1TestRepository.saveKeyValue(
            inputVo.key,
            Redis1_Test(
                inputVo.content,
                Redis1_Test.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.InnerVo("testObject1", false),
                    Redis1_Test.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )
        throw RuntimeException("Test Exception for Redis Transaction Test")
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api7(httpServletResponse: HttpServletResponse, inputVo: C5TkRedisTestController.Api7InputVo) {
        redis1TestRepository.saveKeyValue(
            inputVo.key,
            Redis1_Test(
                inputVo.content,
                Redis1_Test.InnerVo("testObject", true),
                arrayListOf(
                    Redis1_Test.InnerVo("testObject1", false),
                    Redis1_Test.InnerVo("testObject2", true)
                )
            ),
            inputVo.expirationMs
        )
        throw RuntimeException("Test Exception for Redis Non Transaction Test")
        httpServletResponse.setHeader("api-result-code", "ok")
    }
}