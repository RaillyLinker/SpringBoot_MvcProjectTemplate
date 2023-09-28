package com.railly_linker.springboot_mvc_project_template.controllers.c10_service1_tk_v1_mediaResourceProcess

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "/service1/tk/media-resource-process/v1 APIs", description = "C10 : 미디어 리소스(이미지, 비디오, 오디오 등...) 처리 API 컨트롤러")
@RestController
@RequestMapping("/service1/tk/media-resource-process/v1")
class C10Service1TkV1MediaResourceProcessController(
    private val service: C10Service1TkV1MediaResourceProcessService
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
                "1 : 지원하는 파일이 아닙니다."
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
                "1 : 지원하는 파일이 아닙니다."
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
                "1 : 지원하는 파일이 아닙니다."
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
    @Operation(
        summary = "N4 : 서버에 저장된 움직이는 Gif 이미지 파일에서 프레임을 PNG 이미지 파일로 분리한 후 files/temps 폴더 안에 저장",
        description = "서버에 저장된 움직이는 Gif 이미지 파일에서 프레임을 PNG 이미지 파일로 분리한 후 files/temps 폴더 안에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping("/split-animated-gif")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api4(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N5 : 서버에 저장된 움직이는 PNG 이미지 프레임들을 움직이는 Gif 파일로 병합 후 files/temps 폴더 안에 저장",
        description = "서버에 저장된 움직이는 PNG 이미지 프레임들을 움직이는 Gif 파일로 병합 후 files/temps 폴더 안에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping("/merge-images-to-animated-gif")
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api5(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N6 : 동적 GIF 이미지 파일을 업로드 하여 리사이징 후 다운",
        description = "multipart File 로 받은 움직이는 GIF 이미지 파일을 업로드 하여 리사이징 후 다운\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 지원하는 파일이 아닙니다."
    )
    @PostMapping("/resize-gif-image", consumes = ["multipart/form-data"])
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api6InputVo
    ): ResponseEntity<Resource>? {
        return service.api6(inputVo, httpServletResponse)
    }

    data class Api6InputVo(
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
        summary = "N7 : FFMPEG 을 이용하여 비디오 파일에서 GIF 추출 후 반환",
        description = "FFMPEG 을 이용하여 비디오 파일에서 GIF 를 추출하여 반환합니다.\n\n" +
                "ProcessBuilder 를 사용하므로 OS 에 FFMPEG 이 설치되어 있어야합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 지원하는 파일이 아닙니다."
    )
    @PostMapping("/ffmpeg-video-to-gif", consumes = ["multipart/form-data"])
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api7InputVo
    ): ResponseEntity<Resource>? {
        return service.api7(inputVo, httpServletResponse)
    }

    data class Api7InputVo(
        @Schema(description = "업로드 비디오 파일", required = true)
        @JsonProperty("multipartVideoFile")
        val multipartVideoFile: MultipartFile,
        @Schema(description = "잘라내기 시작 시간 (HH:mm:ss)", required = true, example = "00:00:00")
        @JsonProperty("startTime")
        val startTime: String,
        @Schema(description = "잘라내기 지속 시간 (초)", required = true, example = "3")
        @JsonProperty("duration")
        val duration: String
    )


    ////
    @Operation(
        summary = "N8 : FFMPEG 을 이용하여 GIF 파일을 비디오 파일로 변환 후 반환",
        description = "FFMPEG 을 이용하여 GIF 파일을 비디오 파일로 변환하여 반환합니다.\n\n" +
                "ProcessBuilder 를 사용하므로 OS 에 FFMPEG 이 설치되어 있어야합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 지원하는 파일이 아닙니다."
    )
    @PostMapping("/ffmpeg-gif-to-video", consumes = ["multipart/form-data"])
    fun api8(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api8InputVo
    ): ResponseEntity<Resource>? {
        return service.api8(inputVo, httpServletResponse)
    }

    data class Api8InputVo(
        @Schema(description = "업로드 GIF 파일", required = true)
        @JsonProperty("multipartGifFile")
        val multipartGifFile: MultipartFile
    )


    ////
    @Operation(
        summary = "N9 : FFMPEG 을 이용하여 비디오 리사이징 후 반환",
        description = "FFMPEG 을 이용하여 비디오 파일을 리사이징하여 반환합니다.\n\n" +
                "ProcessBuilder 를 사용하므로 OS 에 FFMPEG 이 설치되어 있어야합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 지원하는 파일이 아닙니다."
    )
    @PostMapping("/ffmpeg-video-resizing", consumes = ["multipart/form-data"])
    fun api9(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api9InputVo
    ): ResponseEntity<Resource>? {
        return service.api9(inputVo, httpServletResponse)
    }

    data class Api9InputVo(
        @Schema(description = "업로드 비디오 파일", required = true)
        @JsonProperty("multipartVideoFile")
        val multipartVideoFile: MultipartFile,
        @Schema(description = "리사이징 width", required = true, example = "300")
        @JsonProperty("width")
        val width: Int,
        @Schema(description = "리사이징 height", required = true, example = "200")
        @JsonProperty("height")
        val height: Int
    )

    // todo jpg to avif
    //     png to avif
    //     bmp to avif
    //     gif to avif
    //     avif to jpg
    //     avif to png
    //     avif to bmp
    //     avif to gif
    //     avif resize
    //     animated avif resize
}