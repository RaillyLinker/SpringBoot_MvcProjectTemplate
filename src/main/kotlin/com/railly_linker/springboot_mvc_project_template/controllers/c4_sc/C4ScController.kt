package com.railly_linker.springboot_mvc_project_template.controllers.c4_sc

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Hidden
@Tag(name = "/sc APIs", description = "C4. /sc 경로에 대한 웹페이지 컨트롤러")
@Controller
@RequestMapping("/sc")
class C4ScController(
    private val serviceMbr: C4ScService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1. 홈페이지",
        description = "홈페이지를 반환합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/home-page")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return serviceMbr.api1(httpServletResponse)
    }
}