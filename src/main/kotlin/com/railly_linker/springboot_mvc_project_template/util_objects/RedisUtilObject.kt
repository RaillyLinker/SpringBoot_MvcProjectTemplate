package com.railly_linker.springboot_mvc_project_template.util_objects

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import java.util.concurrent.TimeUnit

object RedisUtilObject {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (Redis Group 의 모든 데이터 리스트 반환)
    inline fun <reified RedisGroupClass : Any> getAllValues(
        redisTemplate: RedisTemplate<String, Any>
    ): List<RedisData> {
        val groupName = RedisGroupClass::class.java.name

        if (groupName.trim() == "") {
            throw RuntimeException("groupName 은 비어있을 수 없습니다.")
        }

        if (groupName.contains(":")) {
            throw RuntimeException("groupName 은 : 를 포함 할 수 없습니다.")
        }

        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(RedisGroupClass::class.java)

        val resultList = ArrayList<RedisData>()

        val cursor = redisTemplate.connectionFactory!!.connection.keyCommands().scan(
            ScanOptions.scanOptions().match("$groupName:*").count(10).build()
        ) // $groupName: 로 시작되는 모든 키를 찾기
        while (cursor.hasNext()) { // 키 리스트 순회
            // 실제 저장된 키 = 그룹명:키
            val redisKey = String(cursor.next())

            val key = redisKey.substring("$groupName:".length) // 키
            val value = redisTemplate.opsForValue()[redisKey] ?: continue // 값

            resultList.add(
                RedisData(
                    groupName,
                    key,
                    value,
                    redisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS) // 남은 만료시간
                )
            )
        }

        return resultList
    }

    // (Redis Group 의 key 에 해당하는 데이터 반환)
    inline fun <reified RedisGroupClass : Any> getValue(
        redisTemplate: RedisTemplate<String, Any>,
        searchKey: String
    ): RedisData? {
        val groupName = RedisGroupClass::class.java.name

        if (groupName.trim() == "") {
            throw RuntimeException("groupName 은 비어있을 수 없습니다.")
        }
        if (groupName.contains(":")) {
            throw RuntimeException("groupName 은 : 를 포함 할 수 없습니다.")
        }
        if (searchKey.trim() == "") {
            throw RuntimeException("key 는 비어있을 수 없습니다.")
        }
        if (searchKey.contains(":")) {
            throw RuntimeException("key 는 : 를 포함 할 수 없습니다.")
        }

        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(RedisGroupClass::class.java)

        var result: RedisData? = null

        val cursor = redisTemplate.connectionFactory!!.connection.keyCommands().scan(
            ScanOptions.scanOptions().match("$groupName:$searchKey").count(1).build()
        ) // $groupName: 로 시작되는 모든 키를 찾기
        while (cursor.hasNext()) { // 키 리스트 순회
            // 실제 저장된 키 = 그룹명:키
            val redisKey = String(cursor.next())

            val value = redisTemplate.opsForValue()[redisKey] ?: continue // 값

            result = RedisData(
                groupName,
                searchKey,
                value,
                redisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS) // 남은 만료시간
            )
        }

        return result
    }

    // (Redis Data 저장)
    inline fun <reified RedisGroupClass : Any> putValue(
        redisTemplate: RedisTemplate<String, Any>,
        key: String,
        value: Any,
        expireTimeMs: Long
    ) {
        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(RedisGroupClass::class.java)

        val redisData = RedisData(
            RedisGroupClass::class.java.name,
            key,
            value,
            expireTimeMs
        )

        // 그룹명과 키를 합쳐서 Redis 고유키 생성
        val redisKey = "${redisData.groupName}:${redisData.key}" // 실제 저장되는 키 = 그룹명:키

        // Redis Value 저장
        redisTemplate.opsForValue()[redisKey] = redisData.value as RedisGroupClass

        // 이번에 넣은 Redis Key 에 대한 만료시간 설정
        redisTemplate.expire(redisKey, redisData.expireTimeMs, TimeUnit.MILLISECONDS)
    }

    // (Redis Group 의 모든 데이터 리스트 삭제)
    inline fun <reified RedisGroupClass : Any> deleteAllValues(
        redisTemplate: RedisTemplate<String, Any>
    ) {
        val groupName = RedisGroupClass::class.java.name

        if (groupName.trim() == "") {
            throw RuntimeException("groupName 은 비어있을 수 없습니다.")
        }

        if (groupName.contains(":")) {
            throw RuntimeException("groupName 은 : 를 포함 할 수 없습니다.")
        }

        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(RedisGroupClass::class.java)

        val cursor = redisTemplate.connectionFactory!!.connection.keyCommands().scan(
            ScanOptions.scanOptions().match("$groupName:*").count(10).build()
        ) // $groupName: 로 시작되는 모든 키를 찾기
        while (cursor.hasNext()) { // 키 리스트 순회
            // 실제 저장된 키 = 그룹명:키
            val redisKey = String(cursor.next())

            redisTemplate.delete(redisKey)
        }
    }

    // (Redis Group 의 key 에 해당하는 데이터 삭제)
    inline fun <reified RedisGroupClass : Any> deleteValue(
        redisTemplate: RedisTemplate<String, Any>,
        searchKey: String
    ) {
        val groupName = RedisGroupClass::class.java.name

        if (groupName.trim() == "") {
            throw RuntimeException("groupName 은 비어있을 수 없습니다.")
        }
        if (groupName.contains(":")) {
            throw RuntimeException("groupName 은 : 를 포함 할 수 없습니다.")
        }
        if (searchKey.trim() == "") {
            throw RuntimeException("key 는 비어있을 수 없습니다.")
        }
        if (searchKey.contains(":")) {
            throw RuntimeException("key 는 : 를 포함 할 수 없습니다.")
        }

        // Redis Value Object Type 설정
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(RedisGroupClass::class.java)

        val cursor = redisTemplate.connectionFactory!!.connection.keyCommands().scan(
            ScanOptions.scanOptions().match("$groupName:$searchKey").count(1).build()
        ) // $groupName: 로 시작되는 모든 키를 찾기
        while (cursor.hasNext()) { // 키 리스트 순회
            // 실제 저장된 키 = 그룹명:키
            val redisKey = String(cursor.next())

            redisTemplate.delete(redisKey)
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class RedisData(
        val groupName: String, // 그룹명 : 리플렉션 클래스 name 을 사용
        val key: String, // 멤버가 입력한 키 : 실제 키는 ${groupName:key}
        val value: Any,
        val expireTimeMs: Long // 남은 만료 시간 밀리초
    ) {
        init {
            if (groupName.trim() == "") {
                throw RuntimeException("groupName 은 비어있을 수 없습니다.")
            }
            if (groupName.contains(":")) {
                throw RuntimeException("groupName 은 : 를 포함 할 수 없습니다.")
            }
            if (key.trim() == "") {
                throw RuntimeException("key 는 비어있을 수 없습니다.")
            }
            if (key.contains(":")) {
                throw RuntimeException("key 는 : 를 포함 할 수 없습니다.")
            }
        }
    }

}