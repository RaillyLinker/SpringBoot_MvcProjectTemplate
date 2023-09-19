package com.railly_linker.springboot_mvc_project_template.controllers.c10_tk_advancedRequestTest

import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.web.context.request.async.DeferredResult
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class C10TkAdvancedRequestTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // (스레드 풀)
    private val executorService: ExecutorService = Executors.newCachedThreadPool()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): Resource? {
        httpServletResponse.setHeader("api-result-code", "0")
        return ByteArrayResource(
            byteArrayOf(
                'a'.code.toByte(),
                'b'.code.toByte(),
                'c'.code.toByte(),
                'd'.code.toByte(),
                'e'.code.toByte(),
                'f'.code.toByte()
            )
        )
    }


    ////
    fun api2(
        videoHeight: C10TkAdvancedRequestTestController.Api1VideoHeight,
        httpServletResponse: HttpServletResponse
    ): Resource? {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val serverFileAbsolutePathString = "$projectRootAbsolutePathString/src/main/resources/static/resource_c10_n2"

        // 멤버십 등의 정보로 해상도 제한을 걸 수도 있음
        val serverFileNameString =
            when (videoHeight) {
                C10TkAdvancedRequestTestController.Api1VideoHeight.H240 -> {
                    "test_240p.mp4"
                }

                C10TkAdvancedRequestTestController.Api1VideoHeight.H360 -> {
                    "test_360p.mp4"
                }

                C10TkAdvancedRequestTestController.Api1VideoHeight.H480 -> {
                    "test_480p.mp4"
                }

                C10TkAdvancedRequestTestController.Api1VideoHeight.H720 -> {
                    "test_720p.mp4"
                }
            }

        // 반환값에 전해줄 FIS
        val fileInputStream = FileInputStream("$serverFileAbsolutePathString/$serverFileNameString")

        httpServletResponse.setHeader("api-result-code", "0")
        return ByteArrayResource(FileCopyUtils.copyToByteArray(fileInputStream))
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse): Resource? {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val serverFileAbsolutePathString = "$projectRootAbsolutePathString/src/main/resources/static/resource_c10_n3"
        val serverFileNameString = "test.mp3"

        // 반환값에 전해줄 FIS
        val fileInputStream = FileInputStream("$serverFileAbsolutePathString/$serverFileNameString")

        httpServletResponse.setHeader("api-result-code", "0")
        return ByteArrayResource(FileCopyUtils.copyToByteArray(fileInputStream))
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse): DeferredResult<C10TkAdvancedRequestTestController.Api4OutputVo>? {
        // 연결 타임아웃 밀리초
        val deferredResultTimeoutMs = 1000L * 60
        val deferredResult = DeferredResult<C10TkAdvancedRequestTestController.Api4OutputVo>(deferredResultTimeoutMs)

        // 비동기 처리
        executorService.execute {
            // 지연시간 대기
            val delayMs = 5000L
            Thread.sleep(delayMs)

            // 결과 반환
            deferredResult.setResult(C10TkAdvancedRequestTestController.Api4OutputVo("${delayMs / 1000} 초 경과 후 반환했습니다."))
        }

        // 결과 대기 객체를 먼저 반환
        return deferredResult
    }


    ////
}