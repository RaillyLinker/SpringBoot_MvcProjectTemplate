package com.railly_linker.springboot_mvc_project_template.configurations.database_configs

import com.railly_linker.springboot_mvc_project_template.ApplicationConstants
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

// [DB 설정]
// 트랜젝션 처리를 할 때는
// @ProwdTransactional([Database1Config.TRANSACTION_NAME])
// fun api1(...
// 와 같이 DB 를 사용하는 함수 위에 어노테이션을 붙여주세요.
@Configuration
@EnableJpaRepositories(
    // database repository path
    basePackages = [Database1Config.REPOSITORY_PATH],
    entityManagerFactoryRef = Database1Config.LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN_NAME, // 아래 bean 이름과 동일
    transactionManagerRef = Database1Config.TRANSACTION_NAME // 아래 bean 이름과 동일
)
class Database1Config(
    private val env: Environment
) {
    companion object {
        // !!!application.yml 에 정의된 datasource 내의 DB명 작성!!
        private const val DATASOURCE_NAME: String = "database1"

        // !!!Database Repository 객체가 저장된 위치 작성!!
        const val REPOSITORY_PATH: String =
            "${ApplicationConstants.PACKAGE_NAME}.data_sources.database1.repositories"

        // !!!Database Entity 객체가 저장된 위치 작성!!
        private const val ENTITY_PATH: String =
            "${ApplicationConstants.PACKAGE_NAME}.data_sources.database1.entities"

        // 위 설정을 조합한 변수
        const val LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN_NAME: String =
            "${DATASOURCE_NAME}_LocalContainerEntityManagerFactoryBean"
        const val TRANSACTION_NAME: String = "${DATASOURCE_NAME}_PlatformTransactionManager"
        private const val DATASOURCE_BEAN_NAME: String = "${DATASOURCE_NAME}_DataSource"
    }

    @Bean(LOCAL_CONTAINER_ENTITY_MANAGER_FACTORY_BEAN_NAME)
    fun customEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = customDataSource()
        em.setPackagesToScan(ENTITY_PATH)
        val vendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties = HashMap<String, Any?>()
        properties["hibernate.hbm2ddl.auto"] = env.getProperty("spring.jpa.hibernate.ddl-auto")
        properties["hibernate.dialect"] = env.getProperty("spring.jpa.database-platform")
        em.setJpaPropertyMap(properties)
        return em
    }

    @Bean(TRANSACTION_NAME)
    fun customTransactionManager(): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = customEntityManagerFactory().`object`
        return transactionManager
    }

    @Bean(DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = "datasource.${DATASOURCE_NAME}")
    fun customDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}