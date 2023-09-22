package com.railly_linker.springboot_mvc_project_template.controllers.c4_tk_requestFromServerTest

import com.google.gson.Gson
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.request_apis.LocalHostRequestApi
import com.railly_linker.springboot_mvc_project_template.util_classes.SseClient
import jakarta.servlet.http.HttpServletResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okio.ByteString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.net.SocketTimeoutException
import java.nio.file.Paths

// todo : 전역의 Assert.state 사용 부분에서 굳이 Exception 을 일으키지 않도록 처리 (파일명 전부 적절히 처리하기)
// todo : 여기서 네트워크 요청시 에러가 발생 했을 때 api-result-code 처리 및 테스트
// todo : SecurityConfig 에서 setHeader 부분에 에러 Http Status Code 처리 및 테스트
@Service
class C4TkRequestFromServerTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // Retrofit2 요청 객체
    val networkRetrofit2: RepositoryNetworkRetrofit2 = RepositoryNetworkRetrofit2.getInstance()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): String? {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.getTkRequestTest().execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        responseObj.body()!!
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse): String? {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.getTkRequestTestRedirectToBlank().execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        responseObj.body()!!
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse): String? {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.getTkRequestTestForwardToBlank().execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        responseObj.body()!!
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api4OutputVo? {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.getTkRequestTestGetRequest(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer"),
                null
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        val responseBody = responseObj.body()!!
                        C4TkRequestFromServerTestController.Api4OutputVo(
                            responseBody.queryParamString,
                            responseBody.queryParamStringNullable,
                            responseBody.queryParamInt,
                            responseBody.queryParamIntNullable,
                            responseBody.queryParamDouble,
                            responseBody.queryParamDoubleNullable,
                            responseBody.queryParamBoolean,
                            responseBody.queryParamBooleanNullable,
                            responseBody.queryParamStringList,
                            responseBody.queryParamStringListNullable
                        )
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api5OutputVo? {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.getTkRequestTestGetRequestPathParamInt(
                1234
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        val responseBody = responseObj.body()!!
                        C4TkRequestFromServerTestController.Api5OutputVo(
                            responseBody.pathParamInt
                        )
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api6(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api6OutputVo? {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.postTkRequestTestPostRequestApplicationJson(
                LocalHostRequestApi.PostTkRequestTestPostRequestApplicationJsonInputVO(
                    "paramFromServer",
                    null,
                    1,
                    null,
                    1.1,
                    null,
                    true,
                    null,
                    listOf("paramFromServer"),
                    null
                )
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        val responseBody = responseObj.body()!!
                        C4TkRequestFromServerTestController.Api6OutputVo(
                            responseBody.requestBodyString,
                            responseBody.requestBodyStringNullable,
                            responseBody.requestBodyInt,
                            responseBody.requestBodyIntNullable,
                            responseBody.requestBodyDouble,
                            responseBody.requestBodyDoubleNullable,
                            responseBody.requestBodyBoolean,
                            responseBody.requestBodyBooleanNullable,
                            responseBody.requestBodyStringList,
                            responseBody.requestBodyStringListNullable
                        )
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api7(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api7OutputVo? {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.postTkRequestTestPostRequestXWwwFormUrlencoded(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer"),
                null
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        val responseBody = responseObj.body()!!
                        C4TkRequestFromServerTestController.Api7OutputVo(
                            responseBody.requestFormString,
                            responseBody.requestFormStringNullable,
                            responseBody.requestFormInt,
                            responseBody.requestFormIntNullable,
                            responseBody.requestFormDouble,
                            responseBody.requestFormDoubleNullable,
                            responseBody.requestFormBoolean,
                            responseBody.requestFormBooleanNullable,
                            responseBody.requestFormStringList,
                            responseBody.requestFormStringListNullable
                        )
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api8(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api8OutputVo? {
        try {
            // 네트워크 요청
            val requestFormString = MultipartBody.Part.createFormData("requestFormString", "paramFromServer")
            val requestFormInt = MultipartBody.Part.createFormData("requestFormInt", 1.toString())
            val requestFormDouble =
                MultipartBody.Part.createFormData("requestFormDouble", 1.1.toString())
            val requestFormBoolean =
                MultipartBody.Part.createFormData("requestFormBoolean", true.toString())

            val requestFormStringList = ArrayList<MultipartBody.Part>()
            for (requestFormString1 in listOf("paramFromServer")) {
                requestFormStringList.add(
                    MultipartBody.Part.createFormData("requestFormStringList", requestFormString1)
                )
            }

            // 전송 하려는 File
            val serverFile =
                Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c4_n8/test.txt")
                    .toFile()
            val multipartFileFormData = MultipartBody.Part.createFormData(
                "multipartFile",
                serverFile.name,
                serverFile.asRequestBody(serverFile.toURI().toURL().openConnection().contentType.toMediaTypeOrNull())
            )

            val responseObj = networkRetrofit2.localHostRequestApi.postTkRequestTestPostRequestMultipartFormData(
                requestFormString,
                null,
                requestFormInt,
                null,
                requestFormDouble,
                null,
                requestFormBoolean,
                null,
                requestFormStringList,
                null,
                multipartFileFormData,
                null
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        val responseBody = responseObj.body()!!
                        C4TkRequestFromServerTestController.Api8OutputVo(
                            responseBody.requestFormString,
                            responseBody.requestFormStringNullable,
                            responseBody.requestFormInt,
                            responseBody.requestFormIntNullable,
                            responseBody.requestFormDouble,
                            responseBody.requestFormDoubleNullable,
                            responseBody.requestFormBoolean,
                            responseBody.requestFormBooleanNullable,
                            responseBody.requestFormStringList,
                            responseBody.requestFormStringListNullable
                        )
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api9(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api9OutputVo? {
        try {
            // 네트워크 요청
            val requestFormString = MultipartBody.Part.createFormData("requestFormString", "paramFromServer")
            val requestFormInt = MultipartBody.Part.createFormData("requestFormInt", 1.toString())
            val requestFormDouble =
                MultipartBody.Part.createFormData("requestFormDouble", 1.1.toString())
            val requestFormBoolean =
                MultipartBody.Part.createFormData("requestFormBoolean", true.toString())

            val requestFormStringList = ArrayList<MultipartBody.Part>()
            for (requestFormString1 in listOf("paramFromServer")) {
                requestFormStringList.add(
                    MultipartBody.Part.createFormData("requestFormStringList", requestFormString1)
                )
            }

            // 전송 하려는 File
            val serverFile1 =
                Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c4_n9/test1.txt")
                    .toFile()
            val serverFile2 =
                Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c4_n9/test2.txt")
                    .toFile()

            val multipartFileListFormData = listOf(
                MultipartBody.Part.createFormData(
                    "multipartFileList",
                    serverFile1.name,
                    serverFile1.asRequestBody(
                        serverFile1.toURI().toURL().openConnection().contentType.toMediaTypeOrNull()
                    )
                ),
                MultipartBody.Part.createFormData(
                    "multipartFileList",
                    serverFile2.name,
                    serverFile2.asRequestBody(
                        serverFile2.toURI().toURL().openConnection().contentType.toMediaTypeOrNull()
                    )
                )
            )

            val responseObj = networkRetrofit2.localHostRequestApi.postTkRequestTestPostRequestMultipartFormData2(
                requestFormString,
                null,
                requestFormInt,
                null,
                requestFormDouble,
                null,
                requestFormBoolean,
                null,
                requestFormStringList,
                null,
                multipartFileListFormData,
                null
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        val responseBody = responseObj.body()!!
                        C4TkRequestFromServerTestController.Api9OutputVo(
                            responseBody.requestFormString,
                            responseBody.requestFormStringNullable,
                            responseBody.requestFormInt,
                            responseBody.requestFormIntNullable,
                            responseBody.requestFormDouble,
                            responseBody.requestFormDoubleNullable,
                            responseBody.requestFormBoolean,
                            responseBody.requestFormBooleanNullable,
                            responseBody.requestFormStringList,
                            responseBody.requestFormStringListNullable
                        )
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api10(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api10OutputVo? {
        try {
            // 네트워크 요청
            val jsonStringFormData = MultipartBody.Part.createFormData(
                "jsonString", Gson().toJson(
                    LocalHostRequestApi.PostTkRequestTestPostRequestMultipartFormDataJsonJsonStringVo(
                        "paramFromServer",
                        null,
                        1,
                        null,
                        1.1,
                        null,
                        true,
                        null,
                        listOf("paramFromServer"),
                        null
                    )
                )
            )

            // 전송 하려는 File
            val serverFile =
                Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c4_n10/test.txt")
                    .toFile()
            val multipartFileFormData = MultipartBody.Part.createFormData(
                "multipartFile",
                serverFile.name,
                serverFile.asRequestBody(serverFile.toURI().toURL().openConnection().contentType.toMediaTypeOrNull())
            )

            val responseObj = networkRetrofit2.localHostRequestApi.postTkRequestTestPostRequestMultipartFormDataJson(
                jsonStringFormData,
                multipartFileFormData,
                null
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                        val responseBody = responseObj.body()!!
                        C4TkRequestFromServerTestController.Api10OutputVo(
                            responseBody.requestFormString,
                            responseBody.requestFormStringNullable,
                            responseBody.requestFormInt,
                            responseBody.requestFormIntNullable,
                            responseBody.requestFormDouble,
                            responseBody.requestFormDoubleNullable,
                            responseBody.requestFormBoolean,
                            responseBody.requestFormBooleanNullable,
                            responseBody.requestFormStringList,
                            responseBody.requestFormStringListNullable
                        )
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            return null
        }
    }


    ////
    fun api11(httpServletResponse: HttpServletResponse) {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.postTkRequestTestGenerateError().execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
                httpServletResponse.setHeader("api-result-code", "1")
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
        }
    }


    ////
    fun api12(httpServletResponse: HttpServletResponse) {
        try {
            // 네트워크 요청
            val responseObj = networkRetrofit2.localHostRequestApi.postTkRequestTestApiResultCodeTest(
                LocalHostRequestApi.PostTkRequestTestApiResultCodeTestErrorTypeEnum.A
            ).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                    }

                    "1" -> {
                        httpServletResponse.setHeader("api-result-code", "1")
                    }

                    "2" -> {
                        httpServletResponse.setHeader("api-result-code", "2")
                    }

                    "3" -> {
                        httpServletResponse.setHeader("api-result-code", "3")
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
        }
    }


    ////
    fun api13(httpServletResponse: HttpServletResponse, delayTimeSec: Int) {
        try {
            // 네트워크 요청
            val responseObj =
                networkRetrofit2.localHostRequestApi.postTkRequestTestGenerateTimeOutError(delayTimeSec).execute()

            // api-result-code 확인
            val responseHeaders = responseObj.headers()
            return if (responseHeaders.names().contains("api-result-code")) {
                val apiResultCode = responseHeaders["api-result-code"]

                // api-result-code 분기
                when (apiResultCode) {
                    "0" -> {
                        httpServletResponse.setHeader("api-result-code", "0")
                    }

                    else -> {
                        // 알수없는 api-result-code
                        classLogger.info("Undefined ApiResultCode")
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                classLogger.info("ApiResultCode Not Exists")
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            classLogger.info("SocketTimeoutException")
            httpServletResponse.setHeader("api-result-code", "1")
        }
    }


    ////
    fun api14(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api14OutputVo? {
        val response = networkRetrofit2.localHostRequestApi.getTkRequestTestReturnTextString().execute()

        httpServletResponse.setHeader("api-result-code", "0")
        return C4TkRequestFromServerTestController.Api14OutputVo(
            response.body()!!
        )
    }


    ////
    fun api15(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api15OutputVo? {
        val response = networkRetrofit2.localHostRequestApi.getTkRequestTestReturnTextHtml().execute()

        httpServletResponse.setHeader("api-result-code", "0")
        return C4TkRequestFromServerTestController.Api15OutputVo(
            response.body()!!
        )
    }


    ////
    fun api16(httpServletResponse: HttpServletResponse): C4TkRequestFromServerTestController.Api16OutputVo? {
        val response = networkRetrofit2.localHostRequestApi.getTkRequestTestAsyncResult().execute()

        httpServletResponse.setHeader("api-result-code", "0")
        return C4TkRequestFromServerTestController.Api16OutputVo(
            response.body()!!.resultMessage
        )
    }


    ////
    fun api17(httpServletResponse: HttpServletResponse) {
        // SSE Subscribe Url 연결 객체 생성
        val serverSentEventObj =
            SseClient("http://127.0.0.1:8080/tk/request-test/sse-test/subscribe")

        // SSE 구독 연결
        serverSentEventObj.connectAsync(
            5000,
            object : SseClient.ListenerCallback {
                override fun onConnectRequestFirstTime(sse: SseClient, originalRequest: Request) {
                    classLogger.info("++++onConnectRequestFirstTime")
                }

                override fun onConnect(sse: SseClient, response: Response) {
                    classLogger.info("++++onOpen")
                }

                override fun onMessageReceive(
                    sse: SseClient,
                    eventId: String?,
                    event: String,
                    message: String
                ) {
                    classLogger.info("++++onMessage : $event $message")
                }

                override fun onCommentReceive(sse: SseClient, comment: String) {
                    classLogger.info("++++onComment : $comment")
                }

                override fun onDisconnected(sse: SseClient) {
                    classLogger.info("++++onClosed")
                }

                override fun onPreRetry(
                    sse: SseClient,
                    originalRequest: Request,
                    throwable: Throwable,
                    response: Response?
                ): Boolean {
                    classLogger.info("++++onPreRetry")
                    return true
                }
            }
        )

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api18(httpServletResponse: HttpServletResponse) {
        val client = OkHttpClient()

        val webSocket = client.newWebSocket(
            Request.Builder()
                .url("ws://localhost:8080/ws/test/websocket")
                .build(),
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    classLogger.info("api18 : onOpen")
                    webSocket.send(
                        Gson().toJson(
                            Api18MessagePayloadVo(
                                "api18 Client",
                                "i am open!"
                            )
                        )
                    )
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    classLogger.info("api18 : onMessage : $text")

                    // 메세지를 받으면 바로 메세지를 보내기
                    webSocket.send(
                        Gson().toJson(
                            Api18MessagePayloadVo(
                                "api18 Client",
                                "reply!"
                            )
                        )
                    )

                    // 웹소켓 종료시
//                    webSocket.close(1000, null)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    classLogger.info("api18 : onMessage : $bytes")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    classLogger.info("api18 : onClosing")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    classLogger.info("api18 : onFailure")
                    t.printStackTrace()
                }
            }
        )
        client.dispatcher.executorService.shutdown()

        classLogger.info(webSocket.toString())

        httpServletResponse.setHeader("api-result-code", "0")
    }

    data class Api18MessagePayloadVo(
        val sender: String, // 송신자 (실제로는 JWT 로 보안 처리를 할 것)
        val message: String
    )
}