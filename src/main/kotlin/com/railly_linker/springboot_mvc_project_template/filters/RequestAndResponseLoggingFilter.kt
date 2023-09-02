package com.railly_linker.springboot_mvc_project_template.filters

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.railly_linker.springboot_mvc_project_template.util_objects.CustomUtilObject
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.UnsupportedEncodingException
import java.time.LocalDateTime

// (Request / Response 별 로깅 필터)
@Component
class RequestAndResponseLoggingFilter : OncePerRequestFilter() {
    // <멤버 변수 공간>
    private val loggerMbr = LoggerFactory.getLogger(this::class.java)

    // 로깅 body 에 표시할 데이터 타입
    // 여기에 포함된 데이터 타입만 로그에 표시됩니다.
    private val visibleTypeListMbr = listOf(
        MediaType.valueOf("text/*"),
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML,
        MediaType.valueOf("application/*+json"),
        MediaType.valueOf("application/*+xml")
    )

    private val mapperMbr = jacksonObjectMapper()


    // ---------------------------------------------------------------------------------------------
    // <상속 메소드 공간>
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestTime = LocalDateTime.now()

        // (기본 로깅 설정)
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response)
        } else {
            val httpServletRequest = request as? ContentCachingRequestWrapper ?: ContentCachingRequestWrapper(request)
            val httpServletResponse =
                response as? ContentCachingResponseWrapper ?: ContentCachingResponseWrapper(response)

            var isError = false
            try {
                if (httpServletRequest.getHeader("accept") == "text/event-stream") {
                    // httpServletResponse 를 넣어야 response Body 출력이 제대로 되지만 text/event-stream 연결에 에러가 발생함
                    filterChain.doFilter(httpServletRequest, response)
                } else {
                    filterChain.doFilter(httpServletRequest, httpServletResponse)
                }
            } catch (e: Exception) {
                if (!(e is ServletException && e.rootCause is AccessDeniedException)) {
                    isError = true
                }
                throw e
            } finally {
                val clientIp = CustomUtilObject.getClientIp(httpServletRequest)

                val queryString = httpServletRequest.queryString?.let { "?$it" } ?: ""
                val endpoint = "${httpServletRequest.method} ${httpServletRequest.requestURI}$queryString"

                val requestHeaders = httpServletRequest.headerNames.asSequence().associateWith { headerName ->
                    httpServletRequest.getHeader(headerName)
                }

                val requestContentByteArray = httpServletRequest.contentAsByteArray
                val requestBody = if (requestContentByteArray.isNotEmpty()) {
                    getContentByte(requestContentByteArray, httpServletRequest.contentType)
                } else ""

                data class RequestDataVo(
                    val requestEp: String,
                    val clientIp: String?,
                    val header: Map<String, String>,
                    val body: Any?
                )

                data class ResponseDataVo(
                    val responseStatus: String,
                    val header: Map<String, String>,
                    val body: Any?
                )

                val requestJsonString = mapperMbr.writeValueAsString(
                    RequestDataVo(
                        endpoint,
                        clientIp,
                        requestHeaders,
                        if (requestBody.isNotBlank() && httpServletRequest.contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                            try {
                                mapperMbr.readTree(requestBody)
                            } catch (e: Exception) {
                                requestBody
                            }
                        } else {
                            requestBody
                        }
                    )
                )

                val responseStatus = httpServletResponse.status
                val responseStatusPhrase = try {
                    HttpStatus.valueOf(responseStatus).reasonPhrase
                } catch (_: Exception) {
                    ""
                }
                val responseHeaders = httpServletResponse.headerNames.asSequence().associateWith { headerName ->
                    httpServletResponse.getHeader(headerName)
                }

                val responseContentByteArray = httpServletResponse.contentAsByteArray
                val responseBody = if (responseContentByteArray.isNotEmpty()) {
                    getContentByte(httpServletResponse.contentAsByteArray, httpServletResponse.contentType)
                } else ""

                if (isError) {
                    val responseJsonArray = mapperMbr.writeValueAsString(
                        ResponseDataVo(
                            "500 $responseStatusPhrase",
                            responseHeaders,
                            if (responseBody.isNotBlank() && httpServletResponse.contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                                try {
                                    mapperMbr.readTree(responseBody)
                                } catch (e: JacksonException) {
                                    responseBody
                                }
                            } else {
                                responseBody
                            }
                        )
                    )

                    loggerMbr.error("requestTime : $requestTime\n    $requestJsonString\n    $responseJsonArray\n")
                } else {
                    val responseJsonArray = mapperMbr.writeValueAsString(
                        ResponseDataVo(
                            "$responseStatus $responseStatusPhrase",
                            responseHeaders,
                            if (responseBody.isNotBlank() && httpServletResponse.contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                                try {
                                    mapperMbr.readTree(responseBody)
                                } catch (e: JacksonException) {
                                    responseBody
                                }
                            } else {
                                responseBody
                            }
                        )
                    )

                    loggerMbr.info("requestTime : $requestTime\n    $requestJsonString\n    $responseJsonArray\n")
                }

                // response 복사
                if (httpServletRequest.isAsyncStarted) { // DeferredResult 처리
                    httpServletRequest.asyncContext.addListener(object : AsyncListener {
                        override fun onComplete(event: AsyncEvent?) {
                            httpServletResponse.copyBodyToResponse()
                        }

                        override fun onTimeout(event: AsyncEvent?) {
                        }

                        override fun onError(event: AsyncEvent?) {
                        }

                        override fun onStartAsync(event: AsyncEvent?) {
                        }
                    })
                } else {
                    httpServletResponse.copyBodyToResponse()
                }
            }
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    private fun getContentByte(content: ByteArray, contentType: String): String {
        val mediaType = MediaType.valueOf(contentType)
        val visible = visibleTypeListMbr.stream().anyMatch { visibleType -> visibleType.includes(mediaType) }

        return if (visible) {
            var contentStr = ""
            contentStr += try {
                String(content, charset("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                val contentSize = content.size
                "$contentSize bytes content"
            }
            contentStr
        } else {
            val contentSize = content.size
            "$contentSize bytes content"
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
}