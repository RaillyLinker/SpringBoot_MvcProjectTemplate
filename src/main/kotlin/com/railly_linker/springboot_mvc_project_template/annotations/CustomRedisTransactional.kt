package com.railly_linker.springboot_mvc_project_template.annotations

// [Redis 트랜젝션 어노테이션]
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomRedisTransactional(
    // RedisTemplate 객체의 Bean 이름과 Key 리스트
    // ex : ["TestBean1:TestKey1", "TestBean1:TestKey2", "TestBean2:TestKey1"]
    val redisTemplateBeanNameAndKeyList: Array<String>
)