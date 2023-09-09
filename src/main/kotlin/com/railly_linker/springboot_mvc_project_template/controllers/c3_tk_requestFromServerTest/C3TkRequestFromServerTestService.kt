package com.railly_linker.springboot_mvc_project_template.controllers.c3_tk_requestFromServerTest

import com.google.gson.Gson
import com.railly_linker.springboot_mvc_project_template.retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.retrofit2.request_apis.LocalHostRequestApi
import jakarta.servlet.http.HttpServletResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Paths

@Service
class C3TkRequestFromServerTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfileMbr: String
) {
    // <멤버 변수 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)

    // Retrofit2 요청 객체
    val networkRetrofit2Mbr: RepositoryNetworkRetrofit2 = RepositoryNetworkRetrofit2.getInstance()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): String? {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTest().execute()
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    responseObj.body()!!
                }

                else -> {
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse): String? {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestRedirectToBlank().execute()
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    responseObj.body()!!
                }

                else -> {
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse): String? {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestForwardToBlank().execute()
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    responseObj.body()!!
                }

                else -> {
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse): C3TkRequestFromServerTestController.Api4OutputVo? {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestGetRequest(
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
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    val responseBody = responseObj.body()!!
                    C3TkRequestFromServerTestController.Api4OutputVo(
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
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse): C3TkRequestFromServerTestController.Api5OutputVo? {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestGetRequestPathParamInt(
            1234
        ).execute()
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    val responseBody = responseObj.body()!!
                    C3TkRequestFromServerTestController.Api5OutputVo(
                        responseBody.pathParamInt
                    )
                }

                else -> {
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api6(httpServletResponse: HttpServletResponse): C3TkRequestFromServerTestController.Api6OutputVo? {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestPostRequestApplicationJson(
            LocalHostRequestApi.TkRequestTestPostRequestApplicationJsonInputVO(
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
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    val responseBody = responseObj.body()!!
                    C3TkRequestFromServerTestController.Api6OutputVo(
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
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api7(httpServletResponse: HttpServletResponse): C3TkRequestFromServerTestController.Api7OutputVo? {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestPostRequestXWwwFormUrlencoded(
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
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    val responseBody = responseObj.body()!!
                    C3TkRequestFromServerTestController.Api7OutputVo(
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
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api8(httpServletResponse: HttpServletResponse): C3TkRequestFromServerTestController.Api8OutputVo? {
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
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n8/test.txt")
                .toFile()
        val multipartFileFormData = MultipartBody.Part.createFormData(
            "multipartFile",
            serverFile.name,
            serverFile.asRequestBody(serverFile.toURI().toURL().openConnection().contentType.toMediaTypeOrNull())
        )

        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestPostRequestMultipartFormData(
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
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    val responseBody = responseObj.body()!!
                    C3TkRequestFromServerTestController.Api8OutputVo(
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
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api9(httpServletResponse: HttpServletResponse): C3TkRequestFromServerTestController.Api9OutputVo? {
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
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n9/test1.txt")
                .toFile()
        val serverFile2 =
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n9/test2.txt")
                .toFile()

        val multipartFileListFormData = listOf(
            MultipartBody.Part.createFormData(
                "multipartFileList",
                serverFile1.name,
                serverFile1.asRequestBody(serverFile1.toURI().toURL().openConnection().contentType.toMediaTypeOrNull())
            ),
            MultipartBody.Part.createFormData(
                "multipartFileList",
                serverFile2.name,
                serverFile2.asRequestBody(serverFile2.toURI().toURL().openConnection().contentType.toMediaTypeOrNull())
            )
        )

        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestPostRequestMultipartFormData2(
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
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    val responseBody = responseObj.body()!!
                    C3TkRequestFromServerTestController.Api9OutputVo(
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
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api10(httpServletResponse: HttpServletResponse): C3TkRequestFromServerTestController.Api10OutputVo? {
        val jsonStringFormData = MultipartBody.Part.createFormData(
            "jsonString", Gson().toJson(
                LocalHostRequestApi.TkRequestTestPostRequestMultipartFormDataJsonJsonStringVo(
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
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n10/test.txt")
                .toFile()
        val multipartFileFormData = MultipartBody.Part.createFormData(
            "multipartFile",
            serverFile.name,
            serverFile.asRequestBody(serverFile.toURI().toURL().openConnection().contentType.toMediaTypeOrNull())
        )

        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestPostRequestMultipartFormDataJson(
            jsonStringFormData,
            multipartFileFormData,
            null
        ).execute()
        val responseHeaders = responseObj.headers()

        return if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                    val responseBody = responseObj.body()!!
                    C3TkRequestFromServerTestController.Api10OutputVo(
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
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            throw RuntimeException("ApiResultCode Not Exists")
        }
    }


    ////
    fun api11(httpServletResponse: HttpServletResponse) {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestGenerateError().execute()
        val responseHeaders = responseObj.headers()

        if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
                }

                else -> {
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            httpServletResponse.setHeader("api-result-code", "1")
        }
    }


    ////
    fun api12(httpServletResponse: HttpServletResponse) {
        val responseObj = networkRetrofit2Mbr.localHostRequestApiMbr.tkRequestTestApiResultCodeTest(
            LocalHostRequestApi.TkRequestTestApiResultCodeTestErrorTypeEnum.A
        ).execute()
        val responseHeaders = responseObj.headers()

        if (responseHeaders.names().contains("api-result-code")) {
            val apiResultCode = responseHeaders["api-result-code"]

            when (apiResultCode) {
                "ok" -> {
                    httpServletResponse.setHeader("api-result-code", "ok")
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
                    throw RuntimeException("Undefined ApiResultCode")
                }
            }
        } else {
            httpServletResponse.setHeader("api-result-code", "1")
        }
    }
}