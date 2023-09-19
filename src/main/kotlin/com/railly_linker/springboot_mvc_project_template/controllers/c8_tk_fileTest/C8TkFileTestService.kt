package com.railly_linker.springboot_mvc_project_template.controllers.c8_tk_fileTest

import jakarta.servlet.http.HttpServletResponse
import net.lingala.zip4j.ZipFile
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
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        return C8TkFileTestController.Api1OutputVo("http://127.0.0.1:8080/tk/file-test/download-from-temps/$savedFileName")
    }

    fun api2(httpServletResponse: HttpServletResponse, fileName: String): ResponseEntity<Resource>? {
        // 파일명에 '..' 문자가 들어 있다면 오류를 발생하고 아니라면 진행(해킹및 오류방지)
        Assert.state(!fileName.contains(".."), "Name of file cannot contain '..'")

        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명 (프로젝트 루트 경로에 있는 temps 폴더를 기준으로 함)
        val serverFilePathObject =
            Paths.get("$projectRootAbsolutePathString/temps/$fileName")

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
        val filePathString1 =
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/1.txt"
        val filePathString2 =
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/2.xlsx"
        val filePathString3 =
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/3.png"
        val filePathString4 =
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3/4.mp4"

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./temps"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 확장자 포함 파일명 생성
        val saveFileName = "zipped_${timeString}.zip"

        val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()

        // 압축 파일 저장 위치 지정
        val zipFile = ZipFile(fileTargetPath.toFile().absoluteFile)
        zipFile.addFiles( // 파일 리스트 단위 압축시
            arrayListOf(
                File(filePathString1),
                File(filePathString2),
                File(filePathString3),
                File(filePathString4)
            )
        )
//        zipFile.addFolder( // 폴더 단위 압축시
//            File("$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n3")
//        )

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api4(httpServletResponse: HttpServletResponse) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath
        val filePathString =
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c8_n4/test.zip"

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./temps"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 확장자 포함 파일명 생성
        val saveFileName = "unzipped_${timeString}/"

        val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()

        val zipFile = ZipFile(filePathString)
        zipFile.extractAll(fileTargetPath.toFile().absolutePath)

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
}