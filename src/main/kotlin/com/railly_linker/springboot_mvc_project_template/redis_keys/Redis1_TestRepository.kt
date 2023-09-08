package com.railly_linker.springboot_mvc_project_template.redis_keys

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.railly_linker.springboot_mvc_project_template.util_objects.RedisUtilObject
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class Redis1_TestRepository(
    @Qualifier("redis1RedisTemplate") private val redisTemplateMbr: RedisTemplate<String, Any>
) {
    // <멤버 변수 공간>
    // 본 클래스명이 통째로 key
    // (ex : com.railly_linker.springboot_mvc_project_template.redis_keys.Redis1_TestRepository)
    val keyName: String = this::class.java.name


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun putValue(
        value: RedisValueVo,
        expireTimeMs: Long
    ) {
        val valueJsonString = Gson().toJson(value)

        RedisUtilObject.putValue(
            redisTemplateMbr,
            keyName,
            valueJsonString,
            expireTimeMs
        )
    }

    fun deleteValue() {
        RedisUtilObject.deleteValue(
            redisTemplateMbr,
            keyName
        )
    }

    fun getValue(): RedisDataVo? {
        val result = RedisUtilObject.getValue(
            redisTemplateMbr,
            keyName
        )

        return if (result == null) {
            null
        } else {
            val valueObject = Gson().fromJson<RedisValueVo>(
                result.valueString, // 해석하려는 json 형식의 String
                object : TypeToken<RedisValueVo>() {}.type // 파싱할 데이터 스키마 객체 타입
            )

            RedisDataVo(
                valueObject,
                result.expireTimeMs
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