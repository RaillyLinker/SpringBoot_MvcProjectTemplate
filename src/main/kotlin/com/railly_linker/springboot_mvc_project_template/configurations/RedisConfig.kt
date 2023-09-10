package com.railly_linker.springboot_mvc_project_template.configurations

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

// (Redis 설정)
// !!!redis 리소스를 추가하려면 application.yml 의 datasource-redis 에 접근 url host, port 정보를 입력 후,
// 아래 부분 코드처럼 한 블록을 복사 붙여넣기 한 후 이름만 바꿔주면 됩니다.!!
// 사용시에는, @Qualifier("redis1RedisTemplate") private val redis1RedisTemplate: RedisTemplate<String, Any>
// 이렇게 종속성으로 객체를 받아오고 사용하는데, 사용법은 예시 코드를 참고하세요.
@Configuration
@EnableCaching
class RedisConfig {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        // !!!RedisTemplate Bean 이름 작성 및 적용하기!!
        const val TN_REDIS1 = "redis1RedisTemplate"
    }

    val redisTemplatesMap: Map<String, RedisTemplate<String, Any>> by lazy {
        mapOf(
            TN_REDIS1 to redis1RedisTemplate()
        )
    }

    // ---------------------------------------------------------------------------------------------
    // [redis1]
    @Value("\${datasource-redis.redis1.host}")
    private lateinit var redis1HostMbr: String

    @Value("\${datasource-redis.redis1.port}")
    private var redis1PortMbr: Int? = null

    @Bean(TN_REDIS1 + "_ConnectionFactory")
    fun redis1ConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redis1HostMbr, redis1PortMbr!!)
    }

    @Bean(TN_REDIS1)
    fun redis1RedisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = redis1ConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
        return redisTemplate
    }

    // ---------------------------------------------------------------------------------------------
    // [redis2] (예시)
//    @Value("\${datasource-redis.redis2.host}")
//    private lateinit var redis2HostMbr: String
//
//    @Value("\${datasource-redis.redis2.port}")
//    private var redis2PortMbr: Int? = null
//
//    @Bean(TN_REDIS2+"_ConnectionFactory")
//    fun redis2ConnectionFactory(): RedisConnectionFactory {
//        return LettuceConnectionFactory(redis2HostMbr, redis2PortMbr!!)
//    }
//
//    @Bean(TN_REDIS1)
//    fun redis2RedisTemplate(): RedisTemplate<String, Any> {
//        val redisTemplate = RedisTemplate<String, Any>()
//        redisTemplate.connectionFactory = redis2ConnectionFactory()
//        redisTemplate.keySerializer = StringRedisSerializer()
//        return redisTemplate
//    }

}