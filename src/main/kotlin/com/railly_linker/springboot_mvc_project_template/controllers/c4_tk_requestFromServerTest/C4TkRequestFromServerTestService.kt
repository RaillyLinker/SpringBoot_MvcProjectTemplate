package com.railly_linker.springboot_mvc_project_template.controllers.c4_tk_requestFromServerTest

import com.google.gson.Gson
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.request_apis.LocalHostRequestApi
import jakarta.servlet.http.HttpServletResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.net.SocketTimeoutException
import java.nio.file.Paths

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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                        return null
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                return null
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
                httpServletResponse.setHeader("api-result-code", "1")
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
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
                        println("Undefined ApiResultCode")
                    }
                }
            } else {
                // 반환되어야 할 api-result-code 가 오지 않음 = 서버측 에러
                println("ApiResultCode Not Exists")
            }
        } catch (e: SocketTimeoutException) {
            // 타임아웃 에러 처리 = 런타임에서 처리해야하는 유일한 클라이언트 측 에러
            println("SocketTimeoutException")
            httpServletResponse.setHeader("api-result-code", "1")
        }
    }
}