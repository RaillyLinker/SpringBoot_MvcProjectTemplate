package com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.request_apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmGoogleapisComRequestApi {
    // (FCM 메세지 보내기)
    @POST("/fcm/send")
    fun postFcmSend(
        @Header("Authorization") authorization: String,
        @Body inputVo: PostFcmSendInputVo
    ): Call<Unit?>

    data class PostFcmSendInputVo(
        @SerializedName("registration_ids")
        @Expose
        val registrationIds: List<String>, // 수신측 FCM 토큰 리스트
        @SerializedName("priority")
        @Expose
        val priority: String,
        @SerializedName("content_available")
        @Expose
        val contentAvailable: Boolean,
        @SerializedName("notification")
        @Expose
        val notification: Notification?,
        @SerializedName("data")
        @Expose
        val data: Map<String, Any?>?
    ) {
        data class Notification(
            val title: String,
            val body: String
        )
    }
}