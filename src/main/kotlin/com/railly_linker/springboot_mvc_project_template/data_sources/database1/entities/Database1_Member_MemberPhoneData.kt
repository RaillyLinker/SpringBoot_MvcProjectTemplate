package com.railly_linker.springboot_mvc_project_template.data_sources.database1.entities

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "member_phone_data", catalog = "member")
@Comment("회원 전화 정보 테이블")
class Database1_Member_MemberPhoneData(
    @Column(name = "member_uid", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    @Comment("멤버 고유값 (member.members.uid)")
    var memberUid: Long,

    @Column(name = "phone_number", nullable = false, columnDefinition = "VARCHAR(45)")
    @Comment("전화번호(국가번호 + 전화번호, 중복 비허용)")
    var phoneNumber: String,

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