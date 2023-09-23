package com.railly_linker.springboot_mvc_project_template.filters

import jakarta.annotation.PostConstruct
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

// 민감한 정보를 지닌 actuator 접근 제한 필터
@Component
class ActuatorEndpointFilter : Filter {
    @Value("\${spring.boot.admin.client.url}")
    private lateinit var springAdminServerFullUrlString: String // SpringAdmin(Server) 주소 (ex : "http://127.0.0.1:8082")

    // actuator 접근가능 IP 리스트
    lateinit var actuatorAccessClientAddressIpStringListMbr: Array<String>

    @PostConstruct // 생성자 실행 및 의존성 주입이 끝난 후 실행되는 함수
    fun initMemberValue() {
        // 기본적으로 local IP, SpringAdminServer Ip 를 허용. 이외엔 아래 array 에 추가할것.
        // !!!actuator 접근 IP 설정!!
        actuatorAccessClientAddressIpStringListMbr =
            arrayOf(
                "127.0.0.1", // Local IP
                springAdminServerFullUrlString.substring(
                    7 until springAdminServerFullUrlString.indexOfLast { it == ':' } // "http://" 의 다음 인덱스부터 ":8080" 앞까지 자르기
                ) // Spring Admin Server IP
            )
    }

    override fun doFilter(
        request: ServletRequest, response: ServletResponse, chain: FilterChain
    ) {
        val httpServletRequest = (request as HttpServletRequest)
        val httpServletResponse = (response as HttpServletResponse)

        // 리퀘스트 URI (ex : /sample/test) 가 /actuator 로 시작되는지를 확인 후 블록
        if (httpServletRequest.requestURI.startsWith("/actuator")) {
            // 요청자 Ip (ex : 127.0.0.1)
            val clientAddressIp = httpServletRequest.remoteAddr

            // actuator 접근 허용 IP 리스트에 포함된 요청자인지 확인
            if (!actuatorAccessClientAddressIpStringListMbr.contains(clientAddressIp)) { // 화이트 리스트 포함 안됨
                // status 404 반환 및 무동작
                httpServletResponse.status = 404
                return
            }
        }

        chain.doFilter(request, response)
    }
}