package com.railly_linker.springboot_mvc_project_template.controllers.c11_service1_sc_v1

import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.servlet.ModelAndView

@Service
class C11Service1ScV1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "template_c11_n1/home_page"

        mv.addObject(
            "viewModel",
            Api1ViewModel(
                activeProfile
            )
        )

        httpServletResponse.setHeader("api-result-code", "0")
        return mv
    }

    data class Api1ViewModel(
        val env: String
    )
}