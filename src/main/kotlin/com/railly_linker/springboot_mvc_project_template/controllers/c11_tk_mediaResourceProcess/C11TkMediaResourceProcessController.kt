package com.railly_linker.springboot_mvc_project_template.controllers.c11_tk_mediaResourceProcess

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "/tk/media-resource-process APIs", description = "C11. 미디어 리소스(이미지, 비디오, 오디오 등...) 처리 API 컨트롤러")
@RestController
@RequestMapping("/tk/media-resource-process")
class C11TkMediaResourceProcessController(
    private val service: C11TkMediaResourceProcessService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 정적 이미지 파일(지원 타입은 description 에 후술)을 업로드 하여 리사이징 후 다운",
        description = "multipart File 로 받은 이미지 파일을 업로드 하여 리사이징 후 다운\n\n" +
                "지원 타입 : jpg, jpeg, bmp, png, gif(움직이지 않는 타입)\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 지원하는 파일이 아닙니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/resize-image", consumes = ["multipart/form-data"])
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api1InputVo
    ): ResponseEntity<Resource>? {
        return service.api1(inputVo, httpServletResponse)
    }

    data class Api1InputVo(
        @Schema(description = "업로드 이미지 파일", required = true)
        @JsonProperty("multipartImageFile")
        val multipartImageFile: MultipartFile,
        @Schema(description = "이미지 리사이징 너비", required = true, example = "300")
        @JsonProperty("resizingWidth")
        val resizingWidth: Int,
        @Schema(description = "이미지 리사이징 높이", required = true, example = "400")
        @JsonProperty("resizingHeight")
        val resizingHeight: Int
    )


    // todo split gif frame
    // todo merge png and jpeg to gif
    // todo resize gif
    // todo png to jpeg
    // todo jpeg to png

    // todo avif
}