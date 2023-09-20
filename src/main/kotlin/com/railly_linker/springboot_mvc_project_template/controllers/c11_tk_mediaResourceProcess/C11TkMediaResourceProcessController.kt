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


    ////
    @Operation(
        summary = "N2 : 정적 이미지 파일(지원 타입은 description 에 후술)을 업로드 하여 파일 포멧 변경 후 다운",
        description = "multipart File 로 받은 이미지 파일을 업로드 하여 포멧 변경 후 다운\n\n" +
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
    @PostMapping("/change-format-image", consumes = ["multipart/form-data"])
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api2InputVo
    ): ResponseEntity<Resource>? {
        return service.api2(inputVo, httpServletResponse)
    }

    data class Api2InputVo(
        @Schema(description = "업로드 이미지 파일", required = true)
        @JsonProperty("multipartImageFile")
        val multipartImageFile: MultipartFile,
        @Schema(description = "변경하려는 이미지 파일 포멧", required = true, example = "JPG")
        @JsonProperty("imageFileFormat")
        val imageFileFormat: FileType
    ) {
        enum class FileType {
            JPG, PNG, BMP, GIF
        }
    }


    ////
    @Operation(
        summary = "N3 : 정적 이미지 파일(지원 타입은 description 에 후술)을 업로드 하여 파일 포멧, 사이즈 변경 후 다운",
        description = "multipart File 로 받은 이미지 파일을 업로드 하여 포멧, 사이즈 변경 후 다운\n\n" +
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
    @PostMapping("/change-format-and-resize-image", consumes = ["multipart/form-data"])
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api3InputVo
    ): ResponseEntity<Resource>? {
        return service.api3(inputVo, httpServletResponse)
    }

    data class Api3InputVo(
        @Schema(description = "업로드 이미지 파일", required = true)
        @JsonProperty("multipartImageFile")
        val multipartImageFile: MultipartFile,
        @Schema(description = "이미지 리사이징 너비", required = true, example = "300")
        @JsonProperty("resizingWidth")
        val resizingWidth: Int,
        @Schema(description = "이미지 리사이징 높이", required = true, example = "400")
        @JsonProperty("resizingHeight")
        val resizingHeight: Int,
        @Schema(description = "변경하려는 이미지 파일 포멧", required = true, example = "JPG")
        @JsonProperty("imageFileFormat")
        val imageFileFormat: FileType
    ) {
        enum class FileType {
            JPG, PNG, BMP, GIF
        }
    }


    ////
    // todo : 기존 방식으로 변경 후 테스트
    @Operation(
        summary = "N4 : 서버에 저장된 움직이는 Gif 이미지 파일에서 프레임을 PNG 이미지 파일로 분리한 후 files/temps 폴더 안에 저장",
        description = "서버에 저장된 움직이는 Gif 이미지 파일에서 프레임을 PNG 이미지 파일로 분리한 후 files/temps 폴더 안에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/split-animated-gif")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api4(httpServletResponse)
    }


    ////
    // todo : 기존 방식으로 변경 후 테스트
    @Operation(
        summary = "N5 : 서버에 저장된 움직이는 PNG 이미지 프레임들을 움직이는 Gif 파일로 병합 후 files/temps 폴더 안에 저장",
        description = "서버에 저장된 움직이는 PNG 이미지 프레임들을 움직이는 Gif 파일로 병합 후 files/temps 폴더 안에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/merge-images-to-animated-gif")
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api5(httpServletResponse)
    }


    // todo resize animated gif

    // todo avif
}