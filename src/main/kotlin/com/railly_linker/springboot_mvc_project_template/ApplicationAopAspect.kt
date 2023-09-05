package com.railly_linker.springboot_mvc_project_template

import com.railly_linker.springboot_mvc_project_template.annotations.ProwdTransactional
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

// [AOP]
@Component
@Aspect
class ApplicationAopAspect(
    private val applicationContextMbr: ApplicationContext
) {
    companion object {
        // !!!DB 트랜젝션용 어노테이션인 ProwdTransactional 파일의 프로젝트 경로 설정하기!!
        const val TRANSACTION_ANNOTATION_PATH =
            "@annotation(${ApplicationConstants.PACKAGE_NAME}.annotations.ProwdTransactional)"

    }


    // ---------------------------------------------------------------------------------------------
    // <AOP 작성 공간>
    // (@ProwdTransactional 를 입력한 함수 실행 전후에 JPA 트랜젝션 적용)
    // !!!Annotation 경로 확인!!
    @Around(TRANSACTION_ANNOTATION_PATH)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val proceed: Any?

        // transactionManager and transactionStatus 리스트
        val transactionManagerAndTransactionStatusList =
            ArrayList<Pair<PlatformTransactionManager, TransactionStatus>>()

        try {
            // annotation 에 설정된 transaction 순차 실행 및 저장
            for (transactionManagerBeanName in ((joinPoint.signature as MethodSignature).method).getAnnotation(
                ProwdTransactional::class.java
            ).transactionManagerBeanNameList) {
                // annotation 에 저장된 transactionManager Bean 이름으로 Bean 객체 가져오기
                val platformTransactionManager =
                    applicationContextMbr.getBean(transactionManagerBeanName) as PlatformTransactionManager

                // transaction 시작 및 정보 저장
                transactionManagerAndTransactionStatusList.add(
                    Pair(
                        platformTransactionManager,
                        platformTransactionManager.getTransaction(DefaultTransactionDefinition())
                    )
                )
            }

            //// 함수 실행 전
            proceed = joinPoint.proceed() // 함수 실행
            //// 함수 실행 후

            // annotation 에 설정된 transaction commit 역순 실행 및 저장
            for (transactionManagerIdx in transactionManagerAndTransactionStatusList.size - 1 downTo 0) {
                val transactionManager = transactionManagerAndTransactionStatusList[transactionManagerIdx]
                transactionManager.first.commit(transactionManager.second)
            }
        } catch (e: Exception) {
            // annotation 에 설정된 transaction rollback 역순 실행 및 저장
            for (transactionManagerIdx in transactionManagerAndTransactionStatusList.size - 1 downTo 0) {
                val transactionManager = transactionManagerAndTransactionStatusList[transactionManagerIdx]
                transactionManager.first.rollback(transactionManager.second)
            }
            throw e
        }

        return proceed // 결과 리턴
    }


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
}