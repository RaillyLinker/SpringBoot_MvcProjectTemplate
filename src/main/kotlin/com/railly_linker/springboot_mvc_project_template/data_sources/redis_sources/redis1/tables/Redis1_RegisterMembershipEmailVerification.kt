package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

// (회원가입 본인 검증 발행 그룹)
// 키 : 이메일
data class Redis1_RegisterMembershipEmailVerification(
    var secret: String // 액세스 토큰 소유 멤버의 고유번호
){
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_RegisterMembershipEmailVerification"
    }
}