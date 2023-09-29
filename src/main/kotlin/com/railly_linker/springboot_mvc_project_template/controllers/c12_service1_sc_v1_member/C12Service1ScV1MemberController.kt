package com.railly_linker.springboot_mvc_project_template.controllers.c12_service1_sc_v1_member

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Tag(name = "/service1/sc/v1/member APIs", description = "C12 : 회원 관련 화면에 대한 API 컨트롤러")
@Controller
@RequestMapping("/service1/sc/v1/member")
class C12Service1ScV1MemberController(
    private val service: C12Service1ScV1MemberService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Hidden
    @Operation(
        summary = "N1 : 로그인 페이지",
        description = "로그인 페이지를 반환합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @GetMapping("/login-page")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): ModelAndView? {
        return service.api1(httpServletResponse)
    }
}