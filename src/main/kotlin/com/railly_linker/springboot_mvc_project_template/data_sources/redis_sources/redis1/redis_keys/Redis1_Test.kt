package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.redis_keys

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.railly_linker.springboot_mvc_project_template.configurations.RedisConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Redis1_Test(
    @Qualifier(RedisConfig.TN_REDIS1) private val redisTemplate: RedisTemplate<String, Any>
) {
    // <멤버 변수 공간>
    companion object {
        const val KEY_NAME: String = "Redis1_Test"
    }


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun putValue(
        value: RedisValueVo,
        expireTimeMs: Long
    ) {
        val valueJsonString = Gson().toJson(value)

        // Redis Value 저장
        redisTemplate.opsForValue()[KEY_NAME] = valueJsonString

        // 이번에 넣은 Redis Key 에 대한 만료시간 설정
        redisTemplate.expire(KEY_NAME, expireTimeMs, TimeUnit.MILLISECONDS)
    }

    fun deleteValue() {
        redisTemplate.delete(KEY_NAME)
    }

    fun getValue(): RedisDataVo? {
        val value = redisTemplate.opsForValue()[KEY_NAME]
        val expireTimeMs = redisTemplate.getExpire(KEY_NAME, TimeUnit.MILLISECONDS)

        return if (value == null) {
            null
        } else {
            val valueObject = Gson().fromJson<RedisValueVo>(
                value as String, // 해석하려는 json 형식의 String
                object : TypeToken<RedisValueVo>() {}.type // 파싱할 데이터 스키마 객체 타입
            )

            RedisDataVo(
                valueObject,
                expireTimeMs
            )
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    class RedisDataVo(
        val value: RedisValueVo,
        val expireTimeMs: Long // 남은 만료 시간 밀리초
    )

    // !!!원하는 Value 저장 타입을 설정!!
    data class RedisValueVo(
        var content: String,
        var innerVo: InnerVo,
        var innerVoList: List<InnerVo>
    ) {
        data class InnerVo(
            var testString: String,
            var testBoolean: Boolean
        )
    }
}