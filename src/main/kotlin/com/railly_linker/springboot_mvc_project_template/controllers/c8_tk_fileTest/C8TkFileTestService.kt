package com.railly_linker.springboot_mvc_project_template.controllers.c8_tk_fileTest

import com.railly_linker.springboot_mvc_project_template.util_objects.CustomUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.ZipOutputStream

@Service
class C8TkFileTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        httpServletResponse: HttpServletResponse,
        inputVo: C8TkFileTestController.Api1InputVo
    ): C8TkFileTestController.Api1OutputVo? {
        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./files/temp").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 원본 파일명(with suffix)
        val multiPartFileNameString = StringUtils.cleanPath(inputVo.multipartFile.originalFilename!!)

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

        val savedFileName = "${fileNameWithOutExtension}(${
            LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
            )
        }).$fileExtension"

        // multipartFile 을 targetPath 에 저장
        inputVo.multipartFile.transferTo(
            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            saveDirectoryPath.resolve(savedFileName).normalize()
        )

        httpServletResponse.setHeader("api-result-code", "0")

        return C8TkFileTestController.Api1OutputVo("http://127.0.0.1:8080/tk/file-test/download-from-temp/$savedFileName")
    }

    fun api2(httpServletResponse: HttpServletResponse, fileName: String): ResponseEntity<Resource>? {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명 (프로젝트 루트 경로에 있는 files/temp 폴더를 기준으로 함)
        val serverFilePathObject =
            Paths.get("$projectRootAbsolutePathString/files/temp/$fileName")

        when {
            Files.isDirectory(serverFilePathObject) -> {
                // 파일이 디렉토리일때
                httpServletResponse.setHeader("api-result-code", "1")
                return null
            }

            Files.notExists(serverFilePathObject) -> {
                // 파일이 없을 때
                httpServletResponse.setHeader("api-result-code", "1")
                return null
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return ResponseEntity<Resource>(
            InputStreamResource(Files.newInputStream(serverFilePathObject)),
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(fileName, StandardCharsets.UTF_8)
                    .build()
                this.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(serverFilePathObject))
            },
            HttpStatus.OK
        )
    }


    ////
    fun api3(httpServletResponse: HttpServletResponse) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 경로 리스트
        val filePathList = listOf(
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/1.txt",
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/2.xlsx",
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/3.png",
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/4.mp4"
        )

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 확장자 포함 파일명 생성
        val fileTargetPath = saveDirectoryPath.resolve(
            "zipped_${
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))
            }.zip"
        ).normalize()

        // 압축 파일 생성
        val zipOutputStream = ZipOutputStream(FileOutputStream(fileTargetPath.toFile()))

        for (filePath in filePathList) {
            val file = File(filePath)
            if (file.exists()) {
                CustomUtilObject.addToZip(file, file.name, zipOutputStream)
            }
        }

        zipOutputStream.close()

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api3Dot1(httpServletResponse: HttpServletResponse) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 압축 대상 디렉토리
        val sourceDir = File("$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3")

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 확장자 포함 파일명 생성
        val fileTargetPath = saveDirectoryPath.resolve(
            "zipped_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))}.zip"
        ).normalize()

        // 압축 파일 생성
        ZipOutputStream(FileOutputStream(fileTargetPath.toFile())).use { zipOutputStream ->
            CustomUtilObject.compressDirectoryToZip(sourceDir, sourceDir.name, zipOutputStream)
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath
        val filePathString =
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n4/test.zip"

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 확장자 포함 파일명 생성
        val saveFileName = "unzipped_${timeString}/"

        val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()

        CustomUtilObject.unzipFile(filePathString, fileTargetPath)

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
}