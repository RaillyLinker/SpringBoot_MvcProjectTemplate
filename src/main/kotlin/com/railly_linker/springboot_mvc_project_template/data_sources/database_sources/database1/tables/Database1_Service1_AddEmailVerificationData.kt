package com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "add_email_verification_data", catalog = "service1")
@Comment("이메일 추가하기 검증 테이블")
class Database1_Service1_AddEmailVerificationData(
    @Column(name = "member_uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("멤버 고유값 (member.members.uid)")
    var memberUid: Long,

    @Column(name = "email_address", nullable = false, columnDefinition = "VARCHAR(100)")
    @Comment("이메일 주소")
    var emailAddress: String,

    @Column(name = "verification_secret", nullable = false, columnDefinition = "VARCHAR(20)")
    @Comment("검증 비문")
    var verificationSecret: String,

    @Column(name = "verification_expire_when", nullable = false, columnDefinition = "DATETIME")
    @Comment("검증 만료 일시")
    var verificationExpireWhen: LocalDateTime,

    @Column(name = "row_activate", nullable = false, columnDefinition = "BIT(1)")
    @Comment("행 활성 여부")
    var rowActivate: Boolean
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", columnDefinition = "BIGINT UNSIGNED")
    @Comment("행 고유값")
    var uid: Long? = null

    @Column(name = "row_create_date", nullable = false, columnDefinition = "DATETIME")
    @CreationTimestamp
    @Comment("행 생성일")
    var rowCreateDate: LocalDateTime? = null

    @Column(name = "row_update_date", nullable = false, columnDefinition = "DATETIME")
    @UpdateTimestamp
    @Comment("행 수정일")
    var rowUpdateDate: LocalDateTime? = null


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
}