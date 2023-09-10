package com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables

data class Redis1_Test(
    var content: String,
    var innerVo: InnerVo,
    var innerVoList: List<InnerVo>
) {
    data class InnerVo(
        var testString: String,
        var testBoolean: Boolean
    )
}