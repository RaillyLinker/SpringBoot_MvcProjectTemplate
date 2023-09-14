package com.railly_linker.springboot_mvc_project_template.util_objects

import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.request_apis.SensApigwNtrussComRequestApi
import org.apache.commons.codec.binary.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

// [Naver SMS 발송 유틸 객체]
object NaverSmsUtilObject {
    // <멤버 변수 공간>
    // Retrofit2 요청 객체
    val networkRetrofit2Mbr: RepositoryNetworkRetrofit2 = RepositoryNetworkRetrofit2.getInstance()

    // !!!아래는 Naver Cloud (https://auth.ncloud.com/login) 의 Simple & Easy Notification Service 에서 받아온 개인정보를 입력할것.!!
    private val accessKey = "TODO" // 인증키 관리에서 얻은 액세스 키
    private val secretKey = "TODO" // 인증키 관리에서 얻은 시크릿 키
    private val serviceId = "TODO" // 네이버 클라우드 콘솔 SMS 서비스 프로젝트를 만들때 발급된 서비스 키
    private val phone = "TODO" // SMS > CallingNumber 에서 등록한 발신번호("-" 는 제외)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun sendSms(inputVo: SendSmsInputVo) {
        val time = System.currentTimeMillis()

        // timeStamp 시그니쳐 생성
        val message = StringBuilder()
            .append("POST")
            .append(" ")
            .append("/sms/v2/services/$serviceId/messages")
            .append("\n")
            .append(time.toString())
            .append("\n")
            .append(accessKey)
            .toString()
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "HmacSHA256"))

        networkRetrofit2Mbr.sensApigwNtrussComRequestApiMbr.postSmsV2ServicesNaverSmsServiceIdMessages(
            serviceId,
            time.toString(),
            accessKey,
            Base64.encodeBase64String(mac.doFinal(message.toByteArray(charset("UTF-8")))),
            SensApigwNtrussComRequestApi.SmsV2ServicesNaverSmsServiceIdMessagesInputVO(
                "SMS",
                "COMM",
                inputVo.countryCode,
                phone,
                inputVo.content,
                listOf(
                    SensApigwNtrussComRequestApi.SmsV2ServicesNaverSmsServiceIdMessagesInputVO.MessageVo(
                        inputVo.phoneNumber,
                        inputVo.content
                    )
                )
            )
        ).execute()
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class SendSmsInputVo(
        // 국가 코드 (ex : 82)
        val countryCode: String,
        // 전화번호 (ex : 01000000000)
        var phoneNumber: String,
        // 문자 본문
        var content: String
    )
}