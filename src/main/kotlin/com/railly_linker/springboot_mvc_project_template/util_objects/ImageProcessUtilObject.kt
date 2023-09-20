package com.railly_linker.springboot_mvc_project_template.util_objects

import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList

object ImageProcessUtilObject {
    // (Gif 를 이미지 리스트로 분리)
    fun gifToImageList(inputStream: InputStream): ArrayList<GifUtilObject.GifFrame> {
        return GifUtilObject.decodeGif(inputStream)
    }

    // (이미지 리스트를 Gif 로 병합)
    fun imageListToGif(gifFrameList: ArrayList<GifUtilObject.GifFrame>, outputStream: OutputStream) {
        GifUtilObject.encodeGif(gifFrameList, outputStream, 2, false)
    }
}