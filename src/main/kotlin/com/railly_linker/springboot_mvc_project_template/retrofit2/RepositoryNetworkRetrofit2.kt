package com.railly_linker.springboot_mvc_project_template.retrofit2

import com.google.gson.GsonBuilder
import com.railly_linker.springboot_mvc_project_template.retrofit2.request_apis.LocalHostRequestApi
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit


// Retrofit2 함수 네트워크 URL 과 API 객체를 이어주고 제공하는 역할
// : 주소 1개당 API 객체 1개를 request_apis 안에 생성하여 아래 (Network Request Api 객체) 공간에 변수를 추가하여 사용
class RepositoryNetworkRetrofit2 private constructor() {
    // <멤버 변수 공간>
    // (Network Request Api 객체들)
    // !!!요청을 보낼 서버 위치 경로별 RequestApi 객체를 생성하기!!

    // 로컬 테스트용
    val localHostRequestApi: LocalHostRequestApi =
        (getRetrofitClient(
            "http://localhost:8080",
            7000L,
            7000L,
            7000L,
            false
        )).create(LocalHostRequestApi::class.java)


    // ---------------------------------------------------------------------------------------------
    // <Static 공간>
    // (싱글톤 설정)
    companion object {
        private val singletonSemaphore = Semaphore(1)
        private var instance: RepositoryNetworkRetrofit2? = null

        fun getInstance(): RepositoryNetworkRetrofit2 {
            singletonSemaphore.acquire()

            if (null == instance) {
                instance = RepositoryNetworkRetrofit2()
            }

            singletonSemaphore.release()

            return instance!!
        }

        // (baseUrl 에 접속하는 레트로핏 객체를 생성 반환하는 함수)
        fun getRetrofitClient(
            baseUrl: String,
            connectTimeOutMilliSecond: Long,
            readTimeOutMilliSecond: Long,
            writeTimeOutMilliSecond: Long,
            retryOnConnectionFailure: Boolean
        ): Retrofit {
            // 클라이언트 설정 객체
            val okHttpClientBuilder = OkHttpClient.Builder()

            okHttpClientBuilder.addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    val originRequest = chain.request()
                    val addedUrl: HttpUrl = originRequest.url.newBuilder()
                        .build()
                    val finalRequest: Request = originRequest.newBuilder()
                        .url(addedUrl).method(originRequest.method, originRequest.body)
                        .build()
                    chain.proceed(finalRequest)
                })

            // 연결 설정
            okHttpClientBuilder.connectTimeout(connectTimeOutMilliSecond, TimeUnit.MILLISECONDS)
            okHttpClientBuilder.readTimeout(readTimeOutMilliSecond, TimeUnit.MILLISECONDS)
            okHttpClientBuilder.writeTimeout(writeTimeOutMilliSecond, TimeUnit.MILLISECONDS)
            okHttpClientBuilder.retryOnConnectionFailure(retryOnConnectionFailure)

            // 로깅 인터셉터 설정
            val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)
            val interceptor = HttpLoggingInterceptor { message -> loggerMbr.debug("Retrofit2 Interceptor : $message") }
            val httpLoggingInterceptor = interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

            // 위 설정에 따른 retrofit 객체 생성 및 반환
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().serializeNulls().create()))
                .client(okHttpClientBuilder.build()).build()
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


}