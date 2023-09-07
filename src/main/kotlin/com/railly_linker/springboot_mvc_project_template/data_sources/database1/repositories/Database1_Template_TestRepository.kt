package com.railly_linker.springboot_mvc_project_template.data_sources.database1.repositories

import com.railly_linker.springboot_mvc_project_template.data_sources.database1.entities.Database1_Template_Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface Database1_Template_TestRepository : JpaRepository<Database1_Template_Test, Long> {
    fun findAllByRowActivateOrderByRowCreateDate(
        rowActivate: Boolean,
        pageable: Pageable
    ): Page<Database1_Template_Test>

    fun countByRowActivate(rowActivate: Boolean): Long

}