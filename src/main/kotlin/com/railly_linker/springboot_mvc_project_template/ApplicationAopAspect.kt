package com.railly_linker.springboot_mvc_project_template

import com.railly_linker.springboot_mvc_project_template.annotations.CustomRedisTransactional
import com.railly_linker.springboot_mvc_project_template.annotations.CustomTransactional
import com.railly_linker.springboot_mvc_project_template.configurations.RedisConfig
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.ApplicationContext
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.util.concurrent.TimeUnit

// [AOP]
@Component
@Aspect
class ApplicationAopAspect(
    private val applicationContext: ApplicationContext,
    private val redisConfig: RedisConfig
) {
    companion object {
        // !!!DB 트랜젝션용 어노테이션인 CustomTransactional 파일의 프로젝트 경로 설정하기!!
        const val TRANSACTION_ANNOTATION_PATH =
            "@annotation(${ApplicationConstants.PACKAGE_NAME}.annotations.CustomTransactional)"

        // !!!Redis 트랜젝션용 어노테이션인 CustomRedisTransactional 파일의 프로젝트 경로 설정하기!!
        const val REDIS_TRANSACTION_ANNOTATION_PATH =
            "@annotation(${ApplicationConstants.PACKAGE_NAME}.annotations.CustomRedisTransactional)"
    }


    // ---------------------------------------------------------------------------------------------
    // <AOP 작성 공간>
    // (@CustomTransactional 를 입력한 함수 실행 전후에 JPA 트랜젝션 적용)
    @Around(TRANSACTION_ANNOTATION_PATH)
    fun aroundTransactionAnnotationFunction(joinPoint: ProceedingJoinPoint): Any? {
        val proceed: Any?

        // transactionManager and transactionStatus 리스트
        val transactionManagerAndTransactionStatusList =
            ArrayList<Pair<PlatformTransactionManager, TransactionStatus>>()

        try {
            // annotation 에 설정된 transaction 순차 실행 및 저장
            for (transactionManagerBeanName in ((joinPoint.signature as MethodSignature).method).getAnnotation(
                CustomTransactional::class.java
            ).transactionManagerBeanNameList) {
                // annotation 에 저장된 transactionManager Bean 이름으로 Bean 객체 가져오기
                val platformTransactionManager =
                    applicationContext.getBean(transactionManagerBeanName) as PlatformTransactionManager

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


    // todo
    ////
    // (@CustomRedisTransactional 를 입력한 함수 실행 전후에 JPA 트랜젝션 적용)
    @Around(REDIS_TRANSACTION_ANNOTATION_PATH)
    fun aroundRedisTransactionAnnotationFunction(joinPoint: ProceedingJoinPoint): Any? {
        val proceed: Any?

        // 백업 데이터
        val backUpDataList: ArrayList<RedisData> = arrayListOf()

        try {
            val redisTemplateBeanNameAndKeyList =
                ((joinPoint.signature as MethodSignature).method).getAnnotation(
                    CustomRedisTransactional::class.java
                ).redisTemplateBeanNameAndKeyList

            // annotation 에 설정된 transaction 순차 실행 및 저장
            for (redisTemplateBeanNameAndKey in redisTemplateBeanNameAndKeyList) {
                // annotation 에 저장된 transactionManager Bean 이름으로 Bean 객체 가져오기
                val redisTemplateBeanNameAndKeySplit = redisTemplateBeanNameAndKey.split(":")
                val redisTemplate = redisConfig.redisTemplatesMap[redisTemplateBeanNameAndKeySplit[0].trim()]!!
                val redisKey = redisTemplateBeanNameAndKeySplit[1].trim()

                // 각 키로 저장된 데이터 가져와 저장하기
                val redisValueVo: RedisData.RedisValueVo?
                val value = redisTemplate.opsForValue()[redisKey]
                redisValueVo = if (value == null) {
                    null
                } else {
                    RedisData.RedisValueVo(
                        value as String,
                        redisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS)
                    )
                }

                backUpDataList.add(
                    RedisData(
                        redisTemplate,
                        redisKey,
                        redisValueVo
                    )
                )
            }

            //// 함수 실행 전
            proceed = joinPoint.proceed() // 함수 실행
            //// 함수 실행 후

        } catch (e: Exception) {
            // 각 키로 저장되어있던 데이터 복원하기
            // annotation 에 설정된 transaction rollback 역순 실행 및 저장

            for (backUpData in backUpDataList) {
                // 현재까지 저장된 정보 제거
                backUpData.redisTemplate.delete(backUpData.key)

                if (backUpData.redisValueVo != null) {
                    // Redis Value 저장
                    backUpData.redisTemplate.opsForValue()[backUpData.key] = backUpData.redisValueVo.value

                    // 이번에 넣은 Redis Key 에 대한 만료시간 설정
                    backUpData.redisTemplate.expire(
                        backUpData.key,
                        backUpData.redisValueVo.expireTimeMs,
                        TimeUnit.MILLISECONDS
                    )
                }
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
    data class RedisData(
        val redisTemplate: RedisTemplate<String, Any>,
        val key: String,
        val redisValueVo: RedisValueVo?
    ) {
        data class RedisValueVo(
            val value: String,
            val expireTimeMs: Long
        )
    }
}