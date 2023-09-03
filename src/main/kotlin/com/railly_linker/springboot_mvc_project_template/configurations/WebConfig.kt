package com.railly_linker.springboot_mvc_project_template.configurations

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

// (CORS 설정)
@Configuration
@EnableWebMvc
class WebConfig(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfileMbr: String
) : WebMvcConfigurer {
    // <멤버 변수 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)
    override fun addCorsMappings(registry: CorsRegistry) {
        // !!!CORS 를 허용할 Origin 리스트 설정하기!!
        val allowedOrigins =
            when (activeProfileMbr) {
                "dev8080" -> {
                    // 개발 프로필
                    listOf(
                        "http://localhost:8081",
                        "http://192.168.0.11:8081"
                    )
                }

                "prod80" -> {
                    // 배포 프로필
                    listOf(
                        "http://localhost:8081",
                        "http://192.168.0.11:8081"
                    )
                }

                else -> {
                    // local 혹은 다른 프로필
                    listOf(
                        "http://localhost:8081",
                        "http://192.168.0.11:8081"
                    )
                }
            }

        registry.addMapping("/**") // 아래 설정을 적용할 요청 경로 (ex : "/somePath/**", "/path")
//            .allowedOriginPatterns("*") // 모든 요청을 허용하려면 allowedOrigins 를 지우고 이것을 사용
            .allowedOrigins(*allowedOrigins.toTypedArray()) // 자원 공유를 허용 할 URL 리스트
            .allowedMethods(
                HttpMethod.POST.name(), HttpMethod.GET.name(),
                HttpMethod.PUT.name(), HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
            ) // 클라이언트에서 발신 가능한 메소드 (ex : "GET", "POST")
            .allowedHeaders("*") // 클라이언트에서 발신 가능한 헤더 (ex : "name", "addr")
    }
}