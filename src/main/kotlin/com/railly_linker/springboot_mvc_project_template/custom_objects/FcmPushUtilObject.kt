package com.railly_linker.springboot_mvc_project_template.custom_objects

import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.request_apis.FcmGoogleapisComRequestApi

// [FCM 메세지 발송 유틸]
object FcmPushUtilObject {
    // <멤버 변수 공간>
    // Retrofit2 요청 객체
    val networkRetrofit2: RepositoryNetworkRetrofit2 = RepositoryNetworkRetrofit2.getInstance()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun sendMessageTo(
        serverKey: String, // 서버 키(앞의 "key=" 은 빼기)
        inputVo: SendMessageToInputVo
    ) {
        networkRetrofit2.fcmGoogleapisComRequestApi.postFcmSend(
            "key=$serverKey",
            FcmGoogleapisComRequestApi.PostFcmSendInputVo(
                inputVo.registrationIds,
                inputVo.priority,
                inputVo.contentAvailable,
                inputVo.notification,
                inputVo.data
            )
        ).execute()
    }


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class SendMessageToInputVo(
        val registrationIds: List<String>, // 메세지 수신자의 FCM 토큰 리스트
        val priority: String, // 기본 "high"
        val contentAvailable: Boolean, // 기본 true
        val notification: FcmGoogleapisComRequestApi.PostFcmSendInputVo.Notification?,
        val data: Map<String, Any?>?
    )
}