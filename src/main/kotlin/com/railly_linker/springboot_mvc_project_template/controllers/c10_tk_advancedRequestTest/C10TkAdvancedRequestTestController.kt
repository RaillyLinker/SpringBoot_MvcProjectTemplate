package com.railly_linker.springboot_mvc_project_template.controllers.c10_tk_advancedRequestTest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@Tag(name = "/tk/advanced-request-test APIs", description = "C10. 고급 요청 / 응답에 대한 테스트 API 컨트롤러")
@RestController
@RequestMapping("/tk/advanced-request-test")
class C10TkAdvancedRequestTestController(
    private val service: C10TkAdvancedRequestTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    ////
    @Operation(
        summary = "N1 : byte 반환 샘플",
        description = " byte array('a', .. , 'f') 에서 아래와 같은 요청으로 원하는 바이트를 요청 가능\n\n" +
                "    >> curl http://localhost:8080/tk/advanced-request-test/byte -i -H \"Range: bytes=2-4\"\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping(path = ["/byte"])
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(
            name = "Range",
            description = "byte array('a', 'b', 'c', 'd', 'e', 'f') 중 가져올 범위(0 부터 시작되는 인덱스)",
            example = "Bytes=2-4"
        )
        @RequestHeader("Range")
        range: String
    ): Resource? {
        return service.api1(httpServletResponse)
    }


    ////
    /**
     * 스트리밍을 하려면,
     * <video src="http://127.0.0.1:8080/tk/advanced-request-test/video-streaming?videoHeight=H720" width="1280px" height="720px" controls></video>
     * 위와 같이 웹 페이지에서 접근이 가능
     * **/
    @Operation(
        summary = "N2 : 비디오 스트리밍 샘플",
        description = "비디오 스트리밍 샘플\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping(path = ["/video-streaming"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestParam(value = "videoHeight")
        videoHeight: Api1VideoHeight // 비디오 해상도 높이
    ): Resource? {
        return service.api2(videoHeight, httpServletResponse)
    }

    enum class Api1VideoHeight {
        H240,
        H360,
        H480,
        H720
    }


    ////
    /**
     * 스트리밍을 하려면,
     * <audio src="http://127.0.0.1:8080/tk/advanced-request-test/audio-streaming" controls></audio>
     * 위와 같이 웹 페이지에서 접근이 가능
     * **/
    @Operation(
        summary = "N3 : 오디오 스트리밍 샘플",
        description = "오디오 스트리밍 샘플\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping(path = ["/audio-streaming"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Resource? {
        return service.api3(httpServletResponse)
    }


    ////
}