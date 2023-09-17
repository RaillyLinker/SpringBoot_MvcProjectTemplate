package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

import com.railly_linker.springboot_mvc_project_template.configurations.RedisConfig

// (이메일 회원가입 본인 검증 발행 그룹)
// 키 : "${memberUid}_${email}"
data class Redis1_AddEmailVerification(
    var secret: String // 이메일로 발송된 비밀키
){
    companion object {
        // !!!Redis Template 이름 설정!!
        private const val TEMPLATE_NAME = RedisConfig.TN_REDIS1

        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_AddEmailVerification"

        // Redis Transaction 이름
        const val TRANSACTION_NAME = "$TEMPLATE_NAME:$TABLE_NAME"
    }
}