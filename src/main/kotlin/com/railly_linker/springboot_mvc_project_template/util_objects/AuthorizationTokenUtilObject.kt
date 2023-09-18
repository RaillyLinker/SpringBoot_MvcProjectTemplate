package com.railly_linker.springboot_mvc_project_template.util_objects

// [Authorization 유틸]
// Request Header 의 Authorization 키로 전달되는 토큰의 정보를 가져오는 유틸
// JWT 등 종류에 상관 없이 공통적인 정보를 반환해주는 wrapper 역할
object AuthorizationTokenUtilObject {
    // 토큰 만료 시간 반환
    fun getTokenRemainSeconds(
        authorization: String // ex : "Bearer abcd1234"
    ): Long {
        // authorization 에서 액세스 토큰 분리
        val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
        val tokenType = authorizationSplit[0].trim().lowercase() // (ex : "bearer")
        val accessToken = authorizationSplit[1].trim() // (ex : "abcd1234")

        return when (tokenType) { // 토큰 타입별 정보 가져오기
            "bearer" -> {
                JwtTokenUtilObject.getRemainSeconds(accessToken)
            }

            else -> {
                throw RuntimeException("지원하지 않는 토큰 타입입니다.")
            }
        }
    }

    fun getTokenMemberUid(
        authorization: String // ex : "Bearer abcd1234"
    ): String {
        // authorization 에서 액세스 토큰 분리
        val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
        val tokenType = authorizationSplit[0].trim().lowercase() // (ex : "bearer")
        val token = authorizationSplit[1].trim() // (ex : "abcd1234")

        return when (tokenType) { // 토큰 타입별 정보 가져오기
            "bearer" -> {
                JwtTokenUtilObject.getMemberUid(token)
            }

            else -> {
                throw RuntimeException("지원하지 않는 토큰 타입입니다.")
            }
        }
    }

    fun getTokenType(
        authorization: String // ex : "Bearer abcd1234"
    ): String {
        // authorization 에서 액세스 토큰 분리
        val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
        val tokenType = authorizationSplit[0].trim().lowercase() // (ex : "bearer")
        val token = authorizationSplit[1].trim() // (ex : "abcd1234")

        return when (tokenType) { // 토큰 타입별 정보 가져오기
            "bearer" -> {
                JwtTokenUtilObject.getTokenType(token)
            }

            else -> {
                throw RuntimeException("지원하지 않는 토큰 타입입니다.")
            }
        }
    }
}