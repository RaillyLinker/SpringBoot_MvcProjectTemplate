package com.railly_linker.springboot_mvc_project_template.util_objects

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import java.util.concurrent.TimeUnit

object RedisUtilObject {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (Redis Data 저장)
    fun putValue(
        redisTemplate: RedisTemplate<String, Any>,
        key: String,
        value: String,
        expireTimeMs: Long
    ) {
        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(String::class.java)

        // Redis Value 저장
        redisTemplate.opsForValue()[key] = value

        // 이번에 넣은 Redis Key 에 대한 만료시간 설정
        redisTemplate.expire(key, expireTimeMs, TimeUnit.MILLISECONDS)
    }

    // (Redis Group 의 key 에 해당하는 데이터 삭제)
    fun deleteValue(
        redisTemplate: RedisTemplate<String, Any>,
        key: String
    ) {
        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(String::class.java)

        val cursor = redisTemplate.connectionFactory!!.connection.keyCommands().scan(
            ScanOptions.scanOptions().match(key).count(1).build()
        ) // $groupName: 로 시작되는 모든 키를 찾기
        while (cursor.hasNext()) { // 키 리스트 순회
            // 실제 저장된 키 = 그룹명:키
            val redisKey = String(cursor.next())

            redisTemplate.delete(redisKey)
        }
    }

    // (Redis Group 의 key 에 해당하는 데이터 반환)
    fun getValue(
        redisTemplate: RedisTemplate<String, Any>,
        key: String
    ): RedisData? {
        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(String::class.java)

        var result: RedisData? = null

        val cursor = redisTemplate.connectionFactory!!.connection.keyCommands().scan(
            ScanOptions.scanOptions().match(key).count(1).build()
        ) // $groupName: 로 시작되는 모든 키를 찾기
        while (cursor.hasNext()) { // 키 리스트 순회
            // 실제 저장된 키 = 그룹명:키
            val redisKey = String(cursor.next())

            val value = redisTemplate.opsForValue()[redisKey] ?: continue // 값

            result = RedisData(
                value as String,
                redisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS) // 남은 만료시간
            )
        }

        return result
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class RedisData(
        val valueString: String,
        val expireTimeMs: Long // 남은 만료 시간 밀리초
    )

}