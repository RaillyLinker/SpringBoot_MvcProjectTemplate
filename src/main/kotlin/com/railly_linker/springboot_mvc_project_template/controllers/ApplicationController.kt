package com.railly_linker.springboot_mvc_project_template.controllers

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Hidden
@Tag(name = "root APIs", description = "Root 경로에 대한 API 컨트롤러")
@Controller
class ApplicationController(
    private val serviceMbr: ApplicationService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "서버 Root 경로의 URL 로 포워딩",
        description = "서버 Root 경로의 URL 로 포워딩 합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("", "/")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return serviceMbr.api1(httpServletResponse)
    }
}