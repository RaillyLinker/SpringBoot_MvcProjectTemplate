package com.railly_linker.springboot_mvc_project_template.controllers.tk_requestTest

import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/tk/request-test")
class TkRequestTestController(
    private val serviceMbr: TkRequestTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @GetMapping("/")
    fun api1(): ModelAndView? {
        return serviceMbr.api1()
    }

    @GetMapping("")
    fun api2(): String? {
        return serviceMbr.api2()
    }
}