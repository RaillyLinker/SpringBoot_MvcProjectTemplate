package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

// (발행된 액세스 토큰 그룹)
// 키 : "{tokenType} {accessToken}"
data class Redis1_SignInAccessTokenInfo(
    var memberUid: String, // 액세스 토큰 소유 멤버의 고유번호
    var signInDateString: String // 로그인한 일시(yyyy-MM-dd HH:mm:ss.SSS)
){
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_SignInAccessTokenInfo"
    }
}