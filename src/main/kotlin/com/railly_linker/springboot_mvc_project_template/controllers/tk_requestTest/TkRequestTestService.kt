package com.railly_linker.springboot_mvc_project_template.controllers.tk_requestTest

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.servlet.ModelAndView

@Service
class TkRequestTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfileMbr: String
) {
    // <멤버 변수 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "redirect:/tk/request-test"
        return mv
    }

    fun api2(): String? {
        return activeProfileMbr
    }
}