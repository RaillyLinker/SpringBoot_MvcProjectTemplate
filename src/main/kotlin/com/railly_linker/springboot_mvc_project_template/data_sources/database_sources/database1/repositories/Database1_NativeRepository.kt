package com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories

import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Template_TestData
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
interface Database1_NativeRepository : JpaRepository<Database1_Template_TestData, Long> {
    // <C6>
    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            test_data.content as content, 
            test_data.random_num as randomNum, 
            ABS(test_data.random_num-:num) as distance 
            from template.test_data 
            where row_activate = b'1' 
            order by 
            distance
            """
    )
    fun selectListForC6N5(
        @Param(value = "num") num: Int
    ): List<SelectListForC6N5OutputVo>

    interface SelectListForC6N5OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var distance: Int
    }

    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.content as content, 
            test_data.random_num as randomNum, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            ABS(TIMESTAMPDIFF(SECOND, test_data.row_create_date, :date)) as timeDiffSec 
            from template.test_data 
            where row_activate = b'1' 
            order by 
            timeDiffSec
            """
    )
    fun selectListForC6N6(
        @Param(value = "date") date: String
    ): List<SelectListForC6N6OutputVo>

    interface SelectListForC6N6OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var timeDiffSec: Long
    }

    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            test_data.content as content, 
            test_data.random_num as randomNum, 
            ABS(test_data.random_num-:num) as distance 
            from 
            template.test_data 
            where row_activate = b'1' 
            order by distance
            """,
        countQuery = """
            select 
            count(*) 
            from test 
            where 
            row_activate = b'1'
            """
    )
    fun selectListForC6N8(
        @Param(value = "num") num: Int,
        pageable: Pageable
    ): Page<SelectListForC6N8OutputVo>

    interface SelectListForC6N8OutputVo {
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
        value ="""
            UPDATE template.test_data 
            SET 
            content = :content 
            WHERE 
            uid = :uid
            """
    )
    fun updateForC6N10(
        @Param(value = "uid") uid: Long,
        @Param(value = "content") content: String
    )

    // like 문을 사용할 때, replace 로 검색어와 탐색 정보의 공백을 없애줌으로써 공백에 유연한 검색이 가능
    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            test_data.content as content, 
            test_data.random_num as randomNum 
            from template.test_data 
            where 
            replace(content, ' ', '') like replace(concat('%',:searchKeyword,'%'), ' ', '') 
            and row_activate = b'1'
            """,
        countQuery = """
            select 
            count(*) 
            from template.test_data 
            where 
            replace(content, ' ', '') like replace(concat('%',:searchKeyword,'%'), ' ', '') 
            and row_activate = b'1'
            """
    )
    fun selectListForC6N11(
        @Param(value = "searchKeyword") searchKeyword: String,
        pageable: Pageable
    ): Page<SelectPageForC6N11OutputVo>

    interface SelectPageForC6N11OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
    }

    @Query(
        nativeQuery = true,
        value = "SELECT " +
                "uid as uid, " +
                "row_create_date as rowCreateDate, " +
                "row_update_date as rowUpdateDate, " +
                "content as content, random_num as randomNum, " +
                "distance as distance " +
                "FROM (" +
                "    SELECT " +
                "    *, " +
                "    ABS(test_data.random_num-:num) as distance," +
                "    @rownum \\:= @rownum + 1 AS row_num " +
                "    FROM " +
                "    template.test_data, " +
                "    (SELECT @rownum \\:= 0) r " +
                "    ORDER BY distance ASC" +
                ") AS ordered_table " +
                "WHERE " +
                "row_activate = b'1' and " +
                "row_num > (" +
                "    select if (" +
                "        :lastItemUid > 0, " +
                "        (SELECT " +
                "            row_num " +
                "            FROM (" +
                "                SELECT " +
                "                *, " +
                "                ABS(test_data.random_num-:num) as distance, " +
                "                @rownum2 \\:= @rownum2 + 1 AS row_num " +
                "                FROM " +
                "                template.test_data, " +
                "                (SELECT @rownum2 \\:= 0) r " +
                "                ORDER BY distance ASC" +
                "            ) AS o2 " +
                "            WHERE " +
                "            uid = :lastItemUid" +
                "        ), " +
                "        0" +
                "    )" +
                ")" +
                "LIMIT :pageElementsCount"
    )
    fun selectListForC6N14(
        @Param(value = "lastItemUid") lastItemUid: Long,
        @Param(value = "pageElementsCount") pageElementsCount: Int,
        @Param(value = "num") num: Int
    ): List<SelectListForC6N14OutputVo>

    interface SelectListForC6N14OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var distance: Int
    }

}