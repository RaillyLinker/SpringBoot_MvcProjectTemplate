package com.railly_linker.springboot_mvc_project_template.controllers.tk_requestTest

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import org.springframework.web.servlet.ModelAndView
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TkRequestTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfileMbr: String
) {
    // <멤버 변수 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): String? {
        httpServletResponse.setHeader("api-result-code", "ok")
        return activeProfileMbr
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "redirect:/tk/request-test"

        httpServletResponse.setHeader("api-result-code", "ok")
        return mv
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse): ModelAndView? {
        val mv = ModelAndView()
        mv.viewName = "forward:/tk/request-test"

        httpServletResponse.setHeader("api-result-code", "ok")
        return mv
    }


    ////
    fun api4(
        httpServletResponse: HttpServletResponse,
        queryParamString: String,
        queryParamStringNullable: String?,
        queryParamInt: Int,
        queryParamIntNullable: Int?,
        queryParamDouble: Double,
        queryParamDoubleNullable: Double?,
        queryParamBoolean: Boolean,
        queryParamBooleanNullable: Boolean?,
        queryParamStringList: List<String>,
        queryParamStringListNullable: List<String>?
    ): TkRequestTestController.Api4OutputVo? {
        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRequestTestController.Api4OutputVo(
            queryParamString,
            queryParamStringNullable,
            queryParamInt,
            queryParamIntNullable,
            queryParamDouble,
            queryParamDoubleNullable,
            queryParamBoolean,
            queryParamBooleanNullable,
            queryParamStringList,
            queryParamStringListNullable
        )
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse, pathParamInt: Int): TkRequestTestController.Api5OutputVo? {
        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRequestTestController.Api5OutputVo(pathParamInt)
    }


    ////
    fun api6(
        httpServletResponse: HttpServletResponse,
        inputVo: TkRequestTestController.Api6InputVo
    ): TkRequestTestController.Api6OutputVo? {
        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRequestTestController.Api6OutputVo(
            inputVo.requestBodyString,
            inputVo.requestBodyStringNullable,
            inputVo.requestBodyInt,
            inputVo.requestBodyIntNullable,
            inputVo.requestBodyDouble,
            inputVo.requestBodyDoubleNullable,
            inputVo.requestBodyBoolean,
            inputVo.requestBodyBooleanNullable,
            inputVo.requestBodyStringList,
            inputVo.requestBodyStringListNullable
        )
    }


    ////
    fun api7(
        httpServletResponse: HttpServletResponse,
        inputVo: TkRequestTestController.Api7InputVo
    ): TkRequestTestController.Api7OutputVo? {
        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRequestTestController.Api7OutputVo(
            inputVo.requestFormString,
            inputVo.requestFormStringNullable,
            inputVo.requestFormInt,
            inputVo.requestFormIntNullable,
            inputVo.requestFormDouble,
            inputVo.requestFormDoubleNullable,
            inputVo.requestFormBoolean,
            inputVo.requestFormBooleanNullable,
            inputVo.requestFormStringList,
            inputVo.requestFormStringListNullable
        )
    }


    ////
    fun api8(
        httpServletResponse: HttpServletResponse,
        inputVo: TkRequestTestController.Api8InputVo
    ): TkRequestTestController.Api8OutputVo? {
        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./temps").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 원본 파일명(with suffix)
        val multiPartFileNameString = StringUtils.cleanPath(inputVo.multipartFile.originalFilename!!)

        // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
        Assert.state(!multiPartFileNameString.contains(".."), "Name of file cannot contain '..'")

        // 파일 확장자 구분 위치
        val fileExtensionSplitIdx = multiPartFileNameString.lastIndexOf('.')

        // 확장자가 없는 파일명
        val fileNameWithOutExtension: String
        // 확장자
        val fileExtension: String

        if (fileExtensionSplitIdx == -1) {
            fileNameWithOutExtension = multiPartFileNameString
            fileExtension = ""
        } else {
            fileNameWithOutExtension = multiPartFileNameString.substring(0, fileExtensionSplitIdx)
            fileExtension =
                multiPartFileNameString.substring(fileExtensionSplitIdx + 1, multiPartFileNameString.length)
        }

        // multipartFile 을 targetPath 에 저장
        inputVo.multipartFile.transferTo(
            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            saveDirectoryPath.resolve(
                "${fileNameWithOutExtension}(${
                    LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                    )
                }).$fileExtension"
            ).normalize()
        )

        if (inputVo.multipartFileNullable != null) {
            // 원본 파일명(with suffix)
            val multiPartFileNullableNameString =
                StringUtils.cleanPath(inputVo.multipartFileNullable.originalFilename!!)

            // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
            Assert.state(!multiPartFileNullableNameString.contains(".."), "Name of file cannot contain '..'")

            // 파일 확장자 구분 위치
            val nullableFileExtensionSplitIdx = multiPartFileNullableNameString.lastIndexOf('.')

            // 확장자가 없는 파일명
            val nullableFileNameWithOutExtension: String
            // 확장자
            val nullableFileExtension: String

            if (nullableFileExtensionSplitIdx == -1) {
                nullableFileNameWithOutExtension = multiPartFileNullableNameString
                nullableFileExtension = ""
            } else {
                nullableFileNameWithOutExtension =
                    multiPartFileNullableNameString.substring(0, nullableFileExtensionSplitIdx)
                nullableFileExtension =
                    multiPartFileNullableNameString.substring(
                        nullableFileExtensionSplitIdx + 1,
                        multiPartFileNullableNameString.length
                    )
            }

            // multipartFile 을 targetPath 에 저장
            inputVo.multipartFileNullable.transferTo(
                // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                saveDirectoryPath.resolve(
                    "${nullableFileNameWithOutExtension}(${
                        LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                        )
                    }).$nullableFileExtension"
                ).normalize()
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRequestTestController.Api8OutputVo(
            inputVo.requestFormString,
            inputVo.requestFormStringNullable,
            inputVo.requestFormInt,
            inputVo.requestFormIntNullable,
            inputVo.requestFormDouble,
            inputVo.requestFormDoubleNullable,
            inputVo.requestFormBoolean,
            inputVo.requestFormBooleanNullable,
            inputVo.requestFormStringList,
            inputVo.requestFormStringListNullable
        )
    }


    ////
    fun api9(
        httpServletResponse: HttpServletResponse,
        inputVo: TkRequestTestController.Api9InputVo
    ): TkRequestTestController.Api9OutputVo? {
        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./temps").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        for (multipartFile in inputVo.multipartFileList) {
            // 원본 파일명(with suffix)
            val multiPartFileNameString = StringUtils.cleanPath(multipartFile.originalFilename!!)

            // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
            Assert.state(!multiPartFileNameString.contains(".."), "Name of file cannot contain '..'")

            // 파일 확장자 구분 위치
            val fileExtensionSplitIdx = multiPartFileNameString.lastIndexOf('.')

            // 확장자가 없는 파일명
            val fileNameWithOutExtension: String
            // 확장자
            val fileExtension: String

            if (fileExtensionSplitIdx == -1) {
                fileNameWithOutExtension = multiPartFileNameString
                fileExtension = ""
            } else {
                fileNameWithOutExtension = multiPartFileNameString.substring(0, fileExtensionSplitIdx)
                fileExtension =
                    multiPartFileNameString.substring(fileExtensionSplitIdx + 1, multiPartFileNameString.length)
            }

            // multipartFile 을 targetPath 에 저장
            multipartFile.transferTo(
                // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                saveDirectoryPath.resolve(
                    "${fileNameWithOutExtension}(${
                        LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                        )
                    }).$fileExtension"
                ).normalize()
            )
        }

        if (inputVo.multipartFileNullableList != null) {
            for (multipartFileNullable in inputVo.multipartFileNullableList) {
                // 원본 파일명(with suffix)
                val multiPartFileNullableNameString =
                    StringUtils.cleanPath(multipartFileNullable.originalFilename!!)

                // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
                Assert.state(!multiPartFileNullableNameString.contains(".."), "Name of file cannot contain '..'")

                // 파일 확장자 구분 위치
                val nullableFileExtensionSplitIdx = multiPartFileNullableNameString.lastIndexOf('.')

                // 확장자가 없는 파일명
                val nullableFileNameWithOutExtension: String
                // 확장자
                val nullableFileExtension: String

                if (nullableFileExtensionSplitIdx == -1) {
                    nullableFileNameWithOutExtension = multiPartFileNullableNameString
                    nullableFileExtension = ""
                } else {
                    nullableFileNameWithOutExtension =
                        multiPartFileNullableNameString.substring(0, nullableFileExtensionSplitIdx)
                    nullableFileExtension =
                        multiPartFileNullableNameString.substring(
                            nullableFileExtensionSplitIdx + 1,
                            multiPartFileNullableNameString.length
                        )
                }

                // multipartFile 을 targetPath 에 저장
                multipartFileNullable.transferTo(
                    // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                    saveDirectoryPath.resolve(
                        "${nullableFileNameWithOutExtension}(${
                            LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                            )
                        }).$nullableFileExtension"
                    ).normalize()
                )
            }
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRequestTestController.Api9OutputVo(
            inputVo.requestFormString,
            inputVo.requestFormStringNullable,
            inputVo.requestFormInt,
            inputVo.requestFormIntNullable,
            inputVo.requestFormDouble,
            inputVo.requestFormDoubleNullable,
            inputVo.requestFormBoolean,
            inputVo.requestFormBooleanNullable,
            inputVo.requestFormStringList,
            inputVo.requestFormStringListNullable
        )
    }


    ////
    fun api10(
        httpServletResponse: HttpServletResponse,
        inputVo: TkRequestTestController.Api10InputVo
    ): TkRequestTestController.Api10OutputVo? {
        // input Json String to Object
        val inputJsonObject = Gson().fromJson<TkRequestTestController.Api10InputVo.InputJsonObject>(
            inputVo.jsonString, // 해석하려는 json 형식의 String
            object : TypeToken<TkRequestTestController.Api10InputVo.InputJsonObject>() {}.type // 파싱할 데이터 스키마 객체 타입
        )

        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./temps").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 원본 파일명(with suffix)
        val multiPartFileNameString = StringUtils.cleanPath(inputVo.multipartFile.originalFilename!!)

        // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
        Assert.state(!multiPartFileNameString.contains(".."), "Name of file cannot contain '..'")

        // 파일 확장자 구분 위치
        val fileExtensionSplitIdx = multiPartFileNameString.lastIndexOf('.')

        // 확장자가 없는 파일명
        val fileNameWithOutExtension: String
        // 확장자
        val fileExtension: String

        if (fileExtensionSplitIdx == -1) {
            fileNameWithOutExtension = multiPartFileNameString
            fileExtension = ""
        } else {
            fileNameWithOutExtension = multiPartFileNameString.substring(0, fileExtensionSplitIdx)
            fileExtension =
                multiPartFileNameString.substring(fileExtensionSplitIdx + 1, multiPartFileNameString.length)
        }

        // multipartFile 을 targetPath 에 저장
        inputVo.multipartFile.transferTo(
            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            saveDirectoryPath.resolve(
                "${fileNameWithOutExtension}(${
                    LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                    )
                }).$fileExtension"
            ).normalize()
        )

        if (inputVo.multipartFileNullable != null) {
            // 원본 파일명(with suffix)
            val multiPartFileNullableNameString =
                StringUtils.cleanPath(inputVo.multipartFileNullable.originalFilename!!)

            // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
            Assert.state(!multiPartFileNullableNameString.contains(".."), "Name of file cannot contain '..'")

            // 파일 확장자 구분 위치
            val nullableFileExtensionSplitIdx = multiPartFileNullableNameString.lastIndexOf('.')

            // 확장자가 없는 파일명
            val nullableFileNameWithOutExtension: String
            // 확장자
            val nullableFileExtension: String

            if (nullableFileExtensionSplitIdx == -1) {
                nullableFileNameWithOutExtension = multiPartFileNullableNameString
                nullableFileExtension = ""
            } else {
                nullableFileNameWithOutExtension =
                    multiPartFileNullableNameString.substring(0, nullableFileExtensionSplitIdx)
                nullableFileExtension =
                    multiPartFileNullableNameString.substring(
                        nullableFileExtensionSplitIdx + 1,
                        multiPartFileNullableNameString.length
                    )
            }

            // multipartFile 을 targetPath 에 저장
            inputVo.multipartFileNullable.transferTo(
                // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                saveDirectoryPath.resolve(
                    "${nullableFileNameWithOutExtension}(${
                        LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                        )
                    }).$nullableFileExtension"
                ).normalize()
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return TkRequestTestController.Api10OutputVo(
            inputJsonObject.requestFormString,
            inputJsonObject.requestFormStringNullable,
            inputJsonObject.requestFormInt,
            inputJsonObject.requestFormIntNullable,
            inputJsonObject.requestFormDouble,
            inputJsonObject.requestFormDoubleNullable,
            inputJsonObject.requestFormBoolean,
            inputJsonObject.requestFormBooleanNullable,
            inputJsonObject.requestFormStringList,
            inputJsonObject.requestFormStringListNullable
        )
    }


    ////
    fun api11(httpServletResponse: HttpServletResponse) {
        throw RuntimeException("Test Error")
        httpServletResponse.setHeader("api-result-code", "ok")
    }

    ////
    fun api12(httpServletResponse: HttpServletResponse, errorType: TkRequestTestController.Api12ErrorTypeEnum?) {
        if (errorType == null) {
            httpServletResponse.setHeader("api-result-code", "ok")
        } else {
            when (errorType) {
                TkRequestTestController.Api12ErrorTypeEnum.A -> {
                    httpServletResponse.setHeader("api-result-code", "1")
                }

                TkRequestTestController.Api12ErrorTypeEnum.B -> {
                    httpServletResponse.setHeader("api-result-code", "2")
                }

                TkRequestTestController.Api12ErrorTypeEnum.C -> {
                    httpServletResponse.setHeader("api-result-code", "3")
                }
            }
        }
    }
}