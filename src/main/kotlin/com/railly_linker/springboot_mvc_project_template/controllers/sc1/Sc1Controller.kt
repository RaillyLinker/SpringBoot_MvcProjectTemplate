package com.railly_linker.springboot_mvc_project_template.controllers.sc1

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

// 주소의 sc 는, tk 의 반대대는 개념으로, 해당 주소 체계가 Session / Cookie 인증 / 인가 방식을 따른다는 뜻입니다.
// 컨트롤러의 SC 는, Sc 주소에 대한 컨트롤러를 뜻합니다.
// SC 컨트롤러 이름과 sc 주소는 tk 주소 API 들과 성격이 다른 API 들을 분리하여 관리하기 위해 이름을 붙인 것으로,
// SC 컨트롤러는 기본 C 컨트롤러와는 달리 Swagger 문서에 표시되지 않으며, Session / Cookie 인증 / 인가 방식을 따르며,
// 대부분 Server Side Rendering Web Page 를 반환하는 기능을 담당합니다.
@Hidden
@Tag(name = "/sc APIs", description = "SC1 : /sc 경로에 대한 API 컨트롤러")
@Controller
@RequestMapping("/sc")
class Sc1Controller(
    private val service: Sc1Service
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 홈페이지",
        description = "홈페이지를 반환합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @GetMapping("/home-page")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return service.api1(httpServletResponse)
    }
}