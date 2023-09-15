package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

// (이메일 회원가입 본인 검증 발행 그룹)
// 키 : "${memberUid}_${email}"
data class Redis1_AddEmailVerification(
    var secret: String // 이메일로 발송된 비밀키
){
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_AddEmailVerification"
    }
}