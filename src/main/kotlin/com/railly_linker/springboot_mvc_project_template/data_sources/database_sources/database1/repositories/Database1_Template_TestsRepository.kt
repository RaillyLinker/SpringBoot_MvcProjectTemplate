package com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories

import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Template_TestData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database1_Template_TestsRepository : JpaRepository<Database1_Template_TestData, Long> {
    fun findAllByRowActivateOrderByRowCreateDate(
        rowActivate: Boolean,
        pageable: Pageable
    ): Page<Database1_Template_TestData>

    fun countByRowActivate(rowActivate: Boolean): Long

}