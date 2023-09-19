package com.railly_linker.springboot_mvc_project_template.controllers.c10_tk_advancedRequestTest

import com.railly_linker.springboot_mvc_project_template.util_classes.SseEmitterWrapper
import com.railly_linker.springboot_mvc_project_template.util_objects.AuthorizationTokenUtilObject
import com.railly_linker.springboot_mvc_project_template.util_objects.SseEmitterUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*
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
        httpServletResponse.setHeader("api-result-code", "0")
        return deferredResult
    }


    ////
    // api5 에서 발급한 Emitter 객체
    private val api5SseEmitterWrapperMbr = SseEmitterWrapper(1000L * 10L)
    fun api5(httpServletResponse: HttpServletResponse, authorization: String?, lastSseEventId: String?): SseEmitter? {
        api5SseEmitterWrapperMbr.emitterMapSemaphore.acquire()
        api5SseEmitterWrapperMbr.emitterEventMapSemaphore.acquire()

        // 수신 멤버 고유번호(비회원은 -1)
        val memberUid = if (authorization == null) {
            "-1"
        } else {
            AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        }

        val emitterPublishCount = api5SseEmitterWrapperMbr.emitterPublishSequence++

        // 수신 객체 아이디 (발행총개수_발행일_멤버고유번호)
        val sseEmitterId =
            "${emitterPublishCount}_${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Date())}_${memberUid}"

        // 수신 객체
        val sseEmitter = api5SseEmitterWrapperMbr.getSseEmitter(sseEmitterId, lastSseEventId)

        api5SseEmitterWrapperMbr.emitterEventMapSemaphore.release()
        api5SseEmitterWrapperMbr.emitterMapSemaphore.release()

        return sseEmitter
    }


    ////
    private var triggerTestCountMbr = 0
    fun api6(httpServletResponse: HttpServletResponse) {
        // emitter 이벤트 전송
        api5SseEmitterWrapperMbr.emitterMapSemaphore.acquire()
        api5SseEmitterWrapperMbr.emitterEventMapSemaphore.acquire()
        val nowTriggerTestCount = ++triggerTestCountMbr

        for (emitter in api5SseEmitterWrapperMbr.emitterMap) { // 저장된 모든 emitter 에 발송 (필터링 하려면 emitter.key 에 저장된 정보로 필터링 가능)
            // 발송 시간
            val dateString = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Date())

            // 이벤트 고유값 생성 (이미터고유값/발송시간)
            val eventId = "${emitter.key}/${dateString}"

            // 이벤트 빌더 생성
            val sseEventBuilder = SseEmitterUtilObject.makeSseEventBuilder(
                "triggerTest", // 이벤트 그룹명
                eventId, // 이벤트 고유값
                "trigger $nowTriggerTestCount" // 전달 데이터
            )

            // 이벤트 누락 방지 처리를 위하여 이벤트 빌더 기록
            if (api5SseEmitterWrapperMbr.emitterEventMap.containsKey(emitter.key)) {
                api5SseEmitterWrapperMbr.emitterEventMap[emitter.key]!!.add(Pair(dateString, sseEventBuilder))
            } else {
                api5SseEmitterWrapperMbr.emitterEventMap[emitter.key] = arrayListOf(Pair(dateString, sseEventBuilder))
            }

            // 이벤트 발송
            SseEmitterUtilObject.sendSseEvent(
                emitter.value,
                sseEventBuilder
            )
        }

        api5SseEmitterWrapperMbr.emitterEventMapSemaphore.release()
        api5SseEmitterWrapperMbr.emitterMapSemaphore.release()
    }
}