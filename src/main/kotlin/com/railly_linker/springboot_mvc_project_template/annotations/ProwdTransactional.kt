package com.railly_linker.springboot_mvc_project_template.annotations

// [JPA 의 Transactional 을 여러 TransactionManager 으로 사용 가능하도록 개조한 annotation]
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProwdTransactional(
    val transactionManagerBeanNameList: Array<String>
)