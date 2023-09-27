package com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories

import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Member_JoinTheMembershipWithPhoneNumberVerificationData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// (JPA 레포지토리)
// : 함수 작성 명명법에 따라 데이터베이스 SQL 동작을 자동지원
@Repository
interface Database1_Member_JoinTheMembershipWithPhoneNumberVerificationDataRepository :
    JpaRepository<Database1_Member_JoinTheMembershipWithPhoneNumberVerificationData, Long>