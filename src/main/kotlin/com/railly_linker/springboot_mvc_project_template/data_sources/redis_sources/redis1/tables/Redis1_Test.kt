package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

data class Redis1_Test(
    var content: String,
    var innerVo: InnerVo,
    var innerVoList: List<InnerVo>
) {
    companion object {
        // !!!Redis Table 클래스명을 TABLE_NAME 으로 설정하기!!
        const val TABLE_NAME = "Redis1_Test"
    }

    data class InnerVo(
        var testString: String,
        var testBoolean: Boolean
    )
}