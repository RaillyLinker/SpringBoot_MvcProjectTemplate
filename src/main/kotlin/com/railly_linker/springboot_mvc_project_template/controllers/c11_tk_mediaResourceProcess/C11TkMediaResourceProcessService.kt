package com.railly_linker.springboot_mvc_project_template.controllers.c11_tk_mediaResourceProcess

import jakarta.servlet.http.HttpServletResponse
import net.coobird.thumbnailator.Thumbnails
import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import javax.imageio.stream.FileImageInputStream

@Service
class C11TkMediaResourceProcessService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        inputVo: C11TkMediaResourceProcessController.Api1InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        val contentType = inputVo.multipartImageFile.contentType

        val allowedContentTypes = setOf(
            "image/jpeg",  // jpg and jpeg
            "image/png",
            "image/gif",
            "image/bmp"
        )

        if (contentType !in allowedContentTypes) {
            httpServletResponse.addHeader("api-error-codes", "1")
            return null
        }

        val outputStream = ByteArrayOutputStream()

        Thumbnails.of(inputVo.multipartImageFile.inputStream)
            .size(inputVo.resizingWidth, inputVo.resizingHeight)
            .toOutputStream(outputStream)

        val resizedImage = outputStream.toByteArray()
        val resource: Resource = ByteArrayResource(resizedImage)

        // 전달받은 파일 확장자
        val originFileFormat: String = FilenameUtils.getExtension(inputVo.multipartImageFile.originalFilename)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 결과 파일의 확장자 포함 파일명 생성
        val resultFileName = "resized_${timeString}.${originFileFormat}"

        httpServletResponse.addHeader("api-error-codes", "0")
        return ResponseEntity<Resource>(
            resource,
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(resultFileName, StandardCharsets.UTF_8)
                    .build()
                this.add(
                    HttpHeaders.CONTENT_TYPE,
                    contentType
                )
            },
            HttpStatus.OK
        )
    }

    fun api2(
        inputVo: C11TkMediaResourceProcessController.Api2InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        val contentType = inputVo.multipartImageFile.contentType

        val allowedContentTypes = setOf(
            "image/jpeg",  // jpg and jpeg
            "image/png",
            "image/gif",
            "image/bmp"
        )

        if (contentType !in allowedContentTypes) {
            httpServletResponse.addHeader("api-error-codes", "1")
            return null
        }

        val newFileFormat = inputVo.imageFileFormat.name.lowercase()

        val outputStream = ByteArrayOutputStream()
        Thumbnails
            .of(inputVo.multipartImageFile.inputStream)
            .outputFormat(newFileFormat)
            .scale(1.0)
            .toOutputStream(outputStream)

        val resource = ByteArrayResource(outputStream.toByteArray())

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 결과 파일의 확장자 포함 파일명 생성
        val resultFileName = "change_format_${timeString}.${newFileFormat}"

        httpServletResponse.addHeader("api-error-codes", "0")
        return ResponseEntity<Resource>(
            resource,
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(resultFileName, StandardCharsets.UTF_8)
                    .build()
                this.add(
                    HttpHeaders.CONTENT_TYPE,
                    contentType
                )
            },
            HttpStatus.OK
        )
    }

    fun api3(
        inputVo: C11TkMediaResourceProcessController.Api3InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        val contentType = inputVo.multipartImageFile.contentType

        val allowedContentTypes = setOf(
            "image/jpeg",  // jpg and jpeg
            "image/png",
            "image/gif",
            "image/bmp"
        )

        if (contentType !in allowedContentTypes) {
            httpServletResponse.addHeader("api-error-codes", "1")
            return null
        }

        val newFileFormat = inputVo.imageFileFormat.name.lowercase()

        val outputStream = ByteArrayOutputStream()
        Thumbnails
            .of(inputVo.multipartImageFile.inputStream)
            .outputFormat(newFileFormat)
            .size(inputVo.resizingWidth, inputVo.resizingHeight)
            .toOutputStream(outputStream)

        val resource = ByteArrayResource(outputStream.toByteArray())

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 결과 파일의 확장자 포함 파일명 생성
        val resultFileName = "change_format_and_resize_${timeString}.${newFileFormat}"

        httpServletResponse.addHeader("api-error-codes", "0")
        return ResponseEntity<Resource>(
            resource,
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(resultFileName, StandardCharsets.UTF_8)
                    .build()
                this.add(
                    HttpHeaders.CONTENT_TYPE,
                    contentType
                )
            },
            HttpStatus.OK
        )
    }


    ////
    fun api4(
        httpServletResponse: HttpServletResponse
    ) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath
        val gifFilePathObject =
            Paths.get("$projectRootAbsolutePathString/src/main/resources/static/resource_c11_n4/test.gif")

        // GIF 파일을 ImageReader를 사용하여 읽습니다.
        val imageReader = ImageIO.getImageReaders(FileImageInputStream(gifFilePathObject.toFile())).next()
        imageReader.input = FileImageInputStream(gifFilePathObject.toFile())

        // 저장할 디렉토리를 생성합니다.
        val saveDirectoryPath = Paths.get(
            "./files/temp/${
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))
            }"
        ).toAbsolutePath().normalize()
        if (!Files.exists(saveDirectoryPath)) {
            Files.createDirectories(saveDirectoryPath)
        }

        val frameCount = imageReader.getNumImages(true)
        for (i in 0 until frameCount) {
            val frame: BufferedImage = imageReader.read(i)
            val savePath = saveDirectoryPath.resolve("frame_$i.png").toFile()
            ImageIO.write(frame, "PNG", savePath)
        }
        
        httpServletResponse.addHeader("api-error-codes", "0")
    }

}