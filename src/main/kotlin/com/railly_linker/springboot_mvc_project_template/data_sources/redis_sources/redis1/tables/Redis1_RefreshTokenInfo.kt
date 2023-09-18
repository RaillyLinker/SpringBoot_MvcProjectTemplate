package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

import com.railly_linker.springboot_mvc_project_template.configurations.RedisConfig

// (발행된 리플레시 토큰 그룹)
// 키 : "{tokenType} {accessToken}"
data class Redis1_RefreshTokenInfo(
    var refreshToken: String, // "{tokenType} {refreshToken}" (ex : "Bearer abcd1234")
) {
    companion object {
        // !!!Redis Template 이름 설정!!
        private const val TEMPLATE_NAME = RedisConfig.TN_REDIS1

        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_RefreshTokenInfo"

        // Redis Transaction 이름
        const val TRANSACTION_NAME = "$TEMPLATE_NAME:$TABLE_NAME"
    }
}