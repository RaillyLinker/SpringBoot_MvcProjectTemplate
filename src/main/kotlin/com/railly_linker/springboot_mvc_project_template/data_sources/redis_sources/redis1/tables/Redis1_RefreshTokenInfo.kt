package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

// (발행된 리플레시 토큰 그룹)
// 키 : "{tokenType} {accessToken}"
data class Redis1_RefreshTokenInfo(
    var refreshToken: String, // "{tokenType} {refreshToken}" (ex : "Bearer abcd1234")
){
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_RefreshTokenInfo"
    }
}