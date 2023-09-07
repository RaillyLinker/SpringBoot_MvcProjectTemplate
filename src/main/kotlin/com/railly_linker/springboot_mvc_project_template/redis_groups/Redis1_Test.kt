package com.railly_linker.springboot_mvc_project_template.redis_groups

// [Redis Group]
// 본 프로젝트 내에선 같은 클래스명을 하나의 그룹으로 취급
// 기본 생성자가 필요하므로 data class 의 파라미터를 기본값으로 채우기
// (Redis 테스트 그룹)
data class Redis1_Test(
    var content: String = "",
    var innerVo: InnerVo = InnerVo(),
    var innerVoList: List<InnerVo> = ArrayList()
) {
    data class InnerVo(
        var testString: String = "",
        var testBoolean: Boolean = false
    )
}