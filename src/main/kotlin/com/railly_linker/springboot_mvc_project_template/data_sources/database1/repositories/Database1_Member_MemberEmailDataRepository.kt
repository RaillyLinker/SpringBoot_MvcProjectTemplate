package com.railly_linker.springboot_mvc_project_template.data_sources.database1.repositories

import com.railly_linker.springboot_mvc_project_template.data_sources.database1.entities.Database1_Member_MemberEmailData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_Member_MemberEmailDataRepository : JpaRepository<Database1_Member_MemberEmailData, Long> {
    fun findByEmailAddressAndRowActivate(emailAddress: String, rowActivate: Boolean): Database1_Member_MemberEmailData?
    fun existsByEmailAddressAndRowActivate(emailAddress: String, rowActivate: Boolean): Boolean
    fun findAllByMemberUidAndRowActivate(memberUid: Long, rowActivate: Boolean): List<Database1_Member_MemberEmailData>
    fun existsByMemberUidAndRowActivate(memberUid: Long, rowActivate: Boolean): Boolean
}