package com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories

import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Service1_MemberData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_Service1_MemberDataRepository : JpaRepository<Database1_Service1_MemberData, Long> {
    fun existsByNickNameAndRowActivate(nickName: String, rowActivate: Boolean): Boolean
    fun findByUidAndRowActivate(uid: Long, rowActivate: Boolean): Database1_Service1_MemberData?
    fun findByNickNameAndRowActivate(nickName: String, rowActivate: Boolean): Database1_Service1_MemberData?

}