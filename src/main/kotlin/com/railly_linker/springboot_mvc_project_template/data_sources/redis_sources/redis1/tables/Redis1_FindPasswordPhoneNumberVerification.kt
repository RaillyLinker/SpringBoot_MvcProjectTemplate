package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

import com.railly_linker.springboot_mvc_project_template.configurations.RedisConfig

// (전화 문자로 비밀번호 찾기 본인 검증 발행 그룹)
// 키 : 전화번호
data class Redis1_FindPasswordPhoneNumberVerification(
    var secret: String // 전화 문자로 발송된 비밀키
){
    companion object {
        // !!!Redis Template 이름 설정!!
        private const val TEMPLATE_NAME = RedisConfig.TN_REDIS1

        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_FindPasswordPhoneNumberVerification"

        // Redis Transaction 이름
        const val TRANSACTION_NAME = "$TEMPLATE_NAME:$TABLE_NAME"
    }
}