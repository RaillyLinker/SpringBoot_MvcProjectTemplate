package com.railly_linker.springboot_mvc_project_template.controllers.tk_redisTest

import com.railly_linker.springboot_mvc_project_template.redis_groups.Redis1_Test
import com.railly_linker.springboot_mvc_project_template.util_objects.RedisUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class TkRedisTestService(
    @Qualifier("redis1RedisTemplate") private val redis1RedisTemplateMbr: RedisTemplate<String, Any>
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
        RedisUtilObject.putValue<Redis1_Test>(
            redis1RedisTemplateMbr,
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

        // 키 조회 테스트
        val searchOneResult = RedisUtilObject.getValue<Redis1_Test>(redis1RedisTemplateMbr, inputVo.key)!!

        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRedisTestController.Api1OutputVo(
            searchOneResult.groupName,
            searchOneResult.key,
            (searchOneResult.value as Redis1_Test).content,
            searchOneResult.expireTimeMs
        )
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse): TkRedisTestController.Api2OutputVo? {
        // 전체 조회 테스트
        val resultEntityList = RedisUtilObject.getAllValues<Redis1_Test>(
            redis1RedisTemplateMbr
        )

        val testEntityListVoList = ArrayList<TkRedisTestController.Api2OutputVo.TestEntityVo>()
        for (resultEntity in resultEntityList) {
            testEntityListVoList.add(
                TkRedisTestController.Api2OutputVo.TestEntityVo(
                    resultEntity.key,
                    (resultEntity.value as Redis1_Test).content,
                    resultEntity.expireTimeMs
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRedisTestController.Api2OutputVo(
            testEntityListVoList
        )
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse, key: String) {
        RedisUtilObject.deleteValue<Redis1_Test>(redis1RedisTemplateMbr, key)
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse) {
        RedisUtilObject.deleteAllValues<Redis1_Test>(redis1RedisTemplateMbr)
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse, inputVo: TkRedisTestController.Api5InputVo) {
        // 현재로썬 테스트 수행시 수동으로 롤백 기능을 구현합니다.

        // 작업 전 Redis 데이터 백업
        val beforeValue = RedisUtilObject.getValue<Redis1_Test>(redis1RedisTemplateMbr, inputVo.key)

        try {
            // 입력 테스트
            RedisUtilObject.putValue<Redis1_Test>(
                redis1RedisTemplateMbr,
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

            // 테스트 예외 발생
            throw Exception("test exception")

            httpServletResponse.setHeader("api-result-code", "ok")
        } catch (e: Exception) {
            loggerMbr.error(e.toString())

            // 예외가 발생하면 Redis 를 기존 정보로 변경
            if (beforeValue == null) {
                RedisUtilObject.deleteValue<Redis1_Test>(redis1RedisTemplateMbr, inputVo.key)
            } else {
                RedisUtilObject.putValue<Redis1_Test>(
                    redis1RedisTemplateMbr,
                    inputVo.key,
                    beforeValue.value,
                    beforeValue.expireTimeMs
                )
            }
        }
    }
}