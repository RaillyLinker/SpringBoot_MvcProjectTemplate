package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

// (이메일로 비밀번호 찾기 본인 검증 발행 그룹)
// 키 : 이메일
data class Redis1_FindPasswordEmailVerification(
    var secret: String // 이메일로 발송된 비밀키
){
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_FindPasswordEmailVerification"
    }
}