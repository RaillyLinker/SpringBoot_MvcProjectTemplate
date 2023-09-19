package com.railly_linker.springboot_mvc_project_template.controllers.c11_tk_mediaResourceProcess

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "/tk/media-resource-process APIs", description = "C11. 미디어 리소스(이미지, 비디오, 오디오 등...) 처리 API 컨트롤러")
@RestController
@RequestMapping("/tk/media-resource-process")
class C11TkMediaResourceProcessController(
    private val service: C11TkMediaResourceProcessService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    // todo ProcessBuilder 샘플 추가
    // todo gif, png, jpg 기본 처리
    // todo libwebp 와 ProcessBuilder 로 image to webp 로 변경
    // todo webp 를 중심으로 리사이징, 프레임 분리 등을 수행
}