package com.railly_linker.springboot_mvc_project_template.controllers.c10_tk_mediaResourceProcess

import com.railly_linker.springboot_mvc_project_template.util_objects.GifUtilObject
import com.railly_linker.springboot_mvc_project_template.util_objects.ImageProcessUtilObject
import jakarta.servlet.http.HttpServletResponse
import net.coobird.thumbnailator.Thumbnails
import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

@Service
class C10TkMediaResourceProcessService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        inputVo: C10TkMediaResourceProcessController.Api1InputVo,
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


    ////
    fun api2(
        inputVo: C10TkMediaResourceProcessController.Api2InputVo,
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


    ////
    fun api3(
        inputVo: C10TkMediaResourceProcessController.Api3InputVo,
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
            Paths.get("$projectRootAbsolutePathString/src/main/resources/static/resource_c10_n4/test.gif")

        val frameSplit = ImageProcessUtilObject.gifToImageList(Files.newInputStream(gifFilePathObject))

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp/$timeString"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 받은 파일 순회
        for (bufferedImageIndexedValue in frameSplit.withIndex()) {
            val bufferedImage = bufferedImageIndexedValue.value

            // 확장자 포함 파일명 생성
            val saveFileName = "${bufferedImageIndexedValue.index + 1}.png"

            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()

            // 파일 저장
            ImageIO.write(bufferedImage.frameBufferedImage, "png", fileTargetPath.toFile())
        }

        httpServletResponse.addHeader("api-error-codes", "0")
    }


    ////
    fun api5(httpServletResponse: HttpServletResponse) {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val bufferedImageList = ArrayList<BufferedImage>()
        for (idx in 1..15) {
            val imageFilePathString =
                "$projectRootAbsolutePathString/src/main/resources/static/resource_c10_n5/gif_frame_images/${idx}.png"
            bufferedImageList.add(
                ImageIO.read(
                    Paths.get(imageFilePathString).toFile()
                )
            )
        }

        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)
        val resultFileName = "${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))}.gif"
        val fileTargetPath = saveDirectoryPath.resolve(resultFileName).normalize()

        val gifFrameList: ArrayList<GifUtilObject.GifFrame> = arrayListOf()
        for (bufferedImage in bufferedImageList) {
            gifFrameList.add(
                GifUtilObject.GifFrame(
                    bufferedImage,
                    30
                )
            )
        }

        ImageProcessUtilObject.imageListToGif(
            gifFrameList,
            fileTargetPath.toFile().outputStream()
        )

        httpServletResponse.addHeader("api-error-codes", "0")
    }


    ////
    fun api6(
        inputVo: C10TkMediaResourceProcessController.Api6InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        val contentType = inputVo.multipartImageFile.contentType

        val allowedContentTypes = setOf(
            "image/gif"
        )

        if (contentType !in allowedContentTypes) {
            httpServletResponse.addHeader("api-error-codes", "1")
            return null
        }

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 결과 파일의 확장자 포함 파일명 생성
        val resultFileName = "resized_${timeString}.gif"

        // 리사이징
        val resizedImageByteArray = ImageProcessUtilObject.resizeGifImage(
            inputVo.multipartImageFile,
            inputVo.resizingWidth,
            inputVo.resizingHeight
        )

        httpServletResponse.addHeader("api-error-codes", "0")
        return ResponseEntity<Resource>(
            InputStreamResource(ByteArrayInputStream(resizedImageByteArray)),
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(resultFileName, StandardCharsets.UTF_8)
                    .build()
                this.add(
                    HttpHeaders.CONTENT_TYPE,
                    "image/gif"
                )
            },
            HttpStatus.OK
        )
    }


    ////
    fun api7(
        inputVo: C10TkMediaResourceProcessController.Api7InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        val tempVideoFile = File.createTempFile("video", null)
        inputVo.multipartVideoFile.transferTo(tempVideoFile)

        val gifFile = File.createTempFile("converted", ".gif")

        // todo : OS 별 FFMPEG 사용 처리하기
        // FFMPEG 명령어 작성
        val command =
            "./samples/ffmpeg-win64/bin/ffmpeg -y -i ${tempVideoFile.absolutePath} -ss ${inputVo.startTime} -t ${inputVo.duration} -an ${gifFile.absolutePath}"
        val process = ProcessBuilder(*command.split(" ").toTypedArray())
            .redirectErrorStream(true) // 오류 스트림을 표준 출력 스트림에 병합
            .start()

        // 로그에 프로세스의 출력 내용 출력
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                classLogger.debug(line)
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            classLogger.error("FFmpeg process failed with exit code $exitCode")
        }

        val resource = InputStreamResource(FileInputStream(gifFile))

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 결과 파일의 확장자 포함 파일명 생성
        val resultFileName = "result_${timeString}.gif"

        httpServletResponse.addHeader("api-error-codes", "0")
        return ResponseEntity<Resource>(
            resource,
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(resultFileName, StandardCharsets.UTF_8)
                    .build()
                this.add(
                    HttpHeaders.CONTENT_TYPE,
                    "image/gif"
                )
            },
            HttpStatus.OK
        )
    }


    ////
    fun api8(
        inputVo: C10TkMediaResourceProcessController.Api8InputVo,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<Resource>? {
        val contentType = inputVo.multipartGifFile.contentType

        val allowedContentTypes = setOf(
            "image/gif"
        )

        if (contentType !in allowedContentTypes) {
            httpServletResponse.addHeader("api-error-codes", "1")
            return null
        }

        val tempGifFile = File.createTempFile("input", ".gif")
        inputVo.multipartGifFile.transferTo(tempGifFile)

        val videoFile = File.createTempFile("converted", ".mp4")

        // todo : OS 별 FFMPEG 사용 처리하기
        val command =
            "./samples/ffmpeg-win64/bin/ffmpeg -y -i ${tempGifFile.absolutePath} ${videoFile.absolutePath}"
        val process = ProcessBuilder(*command.split(" ").toTypedArray())
            .redirectErrorStream(true) // 오류 스트림을 표준 출력 스트림에 병합
            .start()

        // 로그에 프로세스의 출력 내용 출력
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                classLogger.debug(line)
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            classLogger.error("FFmpeg process failed with exit code $exitCode")
        }

        val resource = InputStreamResource(FileInputStream(videoFile))

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 결과 파일의 확장자 포함 파일명 생성
        val resultFileName = "result_${timeString}.mp4"

        httpServletResponse.addHeader("api-error-codes", "0")
        return ResponseEntity<Resource>(
            resource,
            HttpHeaders().apply {
                this.contentDisposition = ContentDisposition.builder("attachment")
                    .filename(resultFileName, StandardCharsets.UTF_8)
                    .build()
                this.add(
                    HttpHeaders.CONTENT_TYPE,
                    "video/mp4"
                )
            },
            HttpStatus.OK
        )
    }

}