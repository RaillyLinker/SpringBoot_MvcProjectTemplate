package com.railly_linker.springboot_mvc_project_template.data_sources.database1.repositories

import com.railly_linker.springboot_mvc_project_template.data_sources.database1.entities.Database1_Template_Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// 주의 : NativeRepository 의 반환값으로 기본 Entity 객체는 매핑되지 않으므로 OutputVo Interface 를 작성하여 사용할것.
// sql 문은 한줄로 표기 할 것을 권장. (간편하게 복사해서 사용하며 디버그하기 위하여)
@Repository
interface Database1_NativeRepository : JpaRepository<Database1_Template_Test, Long> {
    // <TkDatabaseTest>
    @Query(
        nativeQuery = true,
        value = "select " +
                "test.uid as uid, " +
                "test.row_create_date as rowCreateDate, " +
                "test.row_update_date as rowUpdateDate, " +
                "test.content as content, " +
                "test.random_num as randomNum, " +
                "ABS(test.random_num-:num) as distance " +
                "from template.test " +
                "order by " +
                "distance"
    )
    fun selectListForTkDatabaseTestApi5(
        @Param(value = "num") num: Int
    ): List<SelectListForTkDatabaseTestApi5OutputVo>

    interface SelectListForTkDatabaseTestApi5OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var distance: Int
    }

    @Query(
        nativeQuery = true,
        value = "select " +
                "test.uid as uid, " +
                "test.content as content, " +
                "test.random_num as randomNum, " +
                "test.row_create_date as rowCreateDate, " +
                "test.row_update_date as rowUpdateDate, " +
                "ABS(TIMESTAMPDIFF(SECOND, test.row_create_date, :date)) as timeDiffSec " +
                "from template.test " +
                "order by " +
                "timeDiffSec"
    )
    fun selectListForTkDatabaseTestApi6(
        @Param(value = "date") date: String
    ): List<SelectListForTkDatabaseTestApi6OutputVo>

    interface SelectListForTkDatabaseTestApi6OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var timeDiffSec: Long
    }

    @Query(
        nativeQuery = true,
        value = "select " +
                "test.uid as uid, " +
                "test.row_create_date as rowCreateDate, " +
                "test.row_update_date as rowUpdateDate, " +
                "test.content as content, " +
                "test.random_num as randomNum, " +
                "ABS(test.random_num-:num) as distance " +
                "from " +
                "template.test " +
                "order by distance",
        countQuery = "select " +
                "count(*) " +
                "from test"
    )
    fun selectListForTkDatabaseTestApi8(
        @Param(value = "num") num: Int,
        pageable: Pageable
    ): Page<SelectListForTkDatabaseTestApi8OutputVo>

    interface SelectListForTkDatabaseTestApi8OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var distance: Int
    }

    @Modifying // Native Query 에서 Delete, Update 문은 이것을 붙여야함
    @Query(
        nativeQuery = true,
        value = "UPDATE template.test " +
                "SET " +
                "content = :content " +
                "WHERE " +
                "uid = :uid"
    )
    fun updateForTkDatabaseTestApi10(
        @Param(value = "uid") uid: Long,
        @Param(value = "content") content: String
    )

    // like 문을 사용할 때, replace 로 검색어와 탐색 정보의 공백을 없애줌으로써 공백에 유연한 검색이 가능
    @Query(
        nativeQuery = true,
        value = "select " +
                "test.uid as uid, " +
                "test.row_create_date as rowCreateDate, " +
                "test.row_update_date as rowUpdateDate, " +
                "test.content as content, " +
                "test.random_num as randomNum " +
                "from template.test " +
                "where " +
                "replace(content, ' ', '') like replace(concat('%',:searchKeyword,'%'), ' ', '')",
        countQuery = "select " +
                "count(*) " +
                "from template.test " +
                "where " +
                "replace(content, ' ', '') like replace(concat('%',:searchKeyword,'%'), ' ', '')"
    )
    fun selectListForTkDatabaseTestApi13(
        @Param(value = "searchKeyword") searchKeyword: String,
        pageable: Pageable
    ): Page<SelectPageForApiC4N13OutputVo>

    interface SelectPageForApiC4N13OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
    }


    // ---------------------------------------------------------------------------------------------
    // <C10>
    @Query(
        nativeQuery = true,
        value = "SELECT " +
                "*, " +
                "(6371*acos(cos(radians(latitude))*cos(radians(:latitude))*cos(radians(:longitude)-radians(longitude))+sin(radians(latitude))*sin(radians(:latitude)))) as distanceKiloMeter " +
                "FROM template.test_map " +
                "HAVING " +
                "distanceKiloMeter <= :radiusKiloMeter " +
                "order by " +
                "distanceKiloMeter"
    )
    fun selectListForApiC7N5(
        @Param(value = "latitude") latitude: Double,
        @Param(value = "longitude") longitude: Double,
        @Param(value = "radiusKiloMeter") radiusKiloMeter: Double
    ): List<SelectListForApiC7N5OutputVo>

    interface SelectListForApiC7N5OutputVo {
        var uid: Long
        var latitude: Double
        var longitude: Double
        var distanceKiloMeter: Double
    }

    @Query(
        nativeQuery = true,
        value = "SELECT " +
                "* " +
                "FROM template.test_map " +
                "where " +
                "(CASE WHEN :southLatitude < :northLatitude THEN latitude BETWEEN :southLatitude AND :northLatitude ELSE latitude BETWEEN :northLatitude AND :southLatitude END) AND " +
                "(CASE WHEN :westLongitude < :eastLongitude THEN longitude BETWEEN :westLongitude AND :eastLongitude ELSE longitude BETWEEN :eastLongitude AND :westLongitude END)"
    )
    fun selectListForApiC7N6(
        @Param(value = "northLatitude") northLatitude: Double,
        @Param(value = "eastLongitude") eastLongitude: Double,
        @Param(value = "southLatitude") southLatitude: Double,
        @Param(value = "westLongitude") westLongitude: Double
    ): List<SelectListForApiC7N6OutputVo>

    interface SelectListForApiC7N6OutputVo {
        var uid: Long
        var latitude: Double
        var longitude: Double
    }

}