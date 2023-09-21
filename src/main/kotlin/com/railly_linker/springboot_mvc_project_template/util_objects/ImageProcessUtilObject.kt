package com.railly_linker.springboot_mvc_project_template.util_objects

import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files

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

        // 임시 파일 생성
        val tempFile = File.createTempFile("resized_", ".gif")

        try {
            GifUtilObject.encodeGif(resizedFrameList, FileOutputStream(tempFile), 2, false)
            return Files.readAllBytes(tempFile.toPath())
        } finally {
            // 임시 파일 삭제
            tempFile.delete()
        }
    }
}