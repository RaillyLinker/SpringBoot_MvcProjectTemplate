package com.railly_linker.springboot_mvc_project_template.util_objects

import jakarta.servlet.http.HttpServletRequest
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.util.regex.Pattern

// [커스텀 유틸 함수 모음]
object CustomUtilObject {
    // (요청자의 IPv6 반환)
    fun getClientIp(request: HttpServletRequest): String? {
        var ip: String?
        ip = request.getHeader("X-Forwarded-For")
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("X-Real-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("X-RealIP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("REMOTE_ADDR")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        return ip
    }

    // (랜덤 영문 대소문자 + 숫자 문자열 생성)
    fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    // (이메일 적합성 검증)
    fun isValidEmail(email: String): Boolean {
        var err = false
        if (Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$").matcher(email).matches()) {
            err = true
        }
        return err
    }

    // (ThymeLeaf 엔진으로 랜더링 한 HTML String 을 반환)
    fun parseHtmlFileToHtmlString(justHtmlFileNameWithOutSuffix: String, variableDataMap: Map<String, Any?>): String {
        // 타임리프 resolver 설정
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/" // static/templates 경로 아래에 있는 파일을 읽는다
        templateResolver.suffix = ".html" // .html로 끝나는 파일을 읽는다
        templateResolver.templateMode = TemplateMode.HTML // 템플릿은 html 형식

        // 스프링 template 엔진을 thymeleafResolver 를 사용하도록 설정
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)

        // 템플릿 엔진에서 사용될 변수 입력
        val context = Context()
        context.setVariables(variableDataMap)

        // 지정한 html 파일과 context 를 읽어 String 으로 반환
        return templateEngine.process(justHtmlFileNameWithOutSuffix, context)
    }

    // (byteArray 를 Hex String 으로 반환)
    fun bytesToHex(bytes: ByteArray): String {
        val builder = StringBuilder()
        for (b in bytes) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }

    // degree 를 radian 으로
    fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    // radian 을 degree 로
    fun rad2deg(rad: Double): Double {
        return rad * 180 / Math.PI
    }


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
}