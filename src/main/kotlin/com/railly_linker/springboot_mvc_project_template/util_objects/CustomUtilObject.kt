package com.railly_linker.springboot_mvc_project_template.util_objects

import jakarta.servlet.http.HttpServletRequest
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


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
}