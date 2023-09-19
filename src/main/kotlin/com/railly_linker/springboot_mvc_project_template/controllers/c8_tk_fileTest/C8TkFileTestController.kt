package com.railly_linker.springboot_mvc_project_template.controllers.c8_tk_fileTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@Tag(name = "/tk/file-test APIs", description = "C8. 파일을 다루는 테스트 API 컨트롤러")
@RestController
@RequestMapping("/tk/file-test")
class C8TkFileTestController(
    private val service: C8TkFileTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : files/temp 폴더로 파일 업로드",
        description = "multipart File 을 하나 업로드하여 서버의 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/upload-to-temp", consumes = ["multipart/form-data"])
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @ModelAttribute
        inputVo: Api1InputVo
    ) : Api1OutputVo? {
        return service.api1(httpServletResponse, inputVo)
    }

    data class Api1InputVo(
        @Schema(description = "업로드 파일", required = true)
        @JsonProperty("multipartFile")
        val multipartFile: MultipartFile
    )

    data class Api1OutputVo(
        @Schema(description = "파일 다운로드 경로", required = true)
        @JsonProperty("fileDownloadFullUrl")
        val fileDownloadFullUrl: String
    )


    ////
    @Operation(
        summary = "N2. files/temp 폴더에서 파일 다운받기",
        description = "업로드 API 를 사용하여 templs 로 업로드한 파일을 다운로드\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 파일이 존재하지 않습니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/download-from-temp/{fileName}")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "fileName", description = "files/temp 폴더 안의 파일명", example = "test.txt")
        @PathVariable("fileName")
        fileName: String
    ): ResponseEntity<Resource>? {
        return service.api2(httpServletResponse, fileName)
    }


    ////
    @Operation(
        summary = "N3. 파일 zip 압축 테스트",
        description = "파일들을 zip 타입으로 압축하여 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/zip-files")
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api3(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N4. zip 압축 파일 해제 테스트",
        description = "zip 압축 파일을 해제하여 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/unzip-file")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ) {
        service.api4(httpServletResponse)
    }
}