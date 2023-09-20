package com.railly_linker.springboot_mvc_project_template.util_objects

import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.deleteIfExists

object ImageProcessUtilObject {
    // (Gif 를 이미지 리스트로 분리)
    fun gifToImageList(inputStream: InputStream): ArrayList<GifUtilObject.GifFrame> {
        return GifUtilObject.decodeGif(inputStream)
    }

    // (이미지 리스트를 Gif 로 병합)
    fun imageListToGif(gifFrameList: ArrayList<GifUtilObject.GifFrame>, outputStream: OutputStream) {
        GifUtilObject.encodeGif(gifFrameList, outputStream, 2, false)
    }

    fun resizeGifImage(multipartImageFile: MultipartFile, newWidth: Int, newHeight: Int): ByteArray {
        val frameList = GifUtilObject.decodeGif(multipartImageFile.inputStream)

        val resizedFrameList = ArrayList<GifUtilObject.GifFrame>()
        for (frame in frameList) {
            // 이미지 리사이징
            val resizedImage: Image =
                frame.frameBufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)

            // 리사이징된 이미지를 버퍼 이미지로 변환
            val resultBufferedImage = BufferedImage(newWidth, newHeight, frame.frameBufferedImage.type)
            val g2d = resultBufferedImage.createGraphics()
            g2d.drawImage(resizedImage, 0, 0, null)
            g2d.dispose()

            resizedFrameList.add(
                GifUtilObject.GifFrame(
                    resultBufferedImage,
                    frame.frameDelay
                )
            )
        }

        val saveDirectoryPathString = "./temps"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)
        val resultFileName =
            "${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))}.gif"
        val fileTargetPath = saveDirectoryPath.resolve(resultFileName).normalize()

        val fileTargetOutputStream = fileTargetPath.toFile().outputStream()
        GifUtilObject.encodeGif(resizedFrameList, fileTargetOutputStream, 2, false)

        val bytes: ByteArray = Files.readAllBytes(fileTargetPath)

        fileTargetPath.deleteIfExists()

        return bytes
    }
}