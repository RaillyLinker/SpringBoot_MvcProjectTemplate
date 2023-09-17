package com.railly_linker.springboot_mvc_project_template.controllers.c7_tk_auth

import com.railly_linker.springboot_mvc_project_template.annotations.CustomRedisTransactional
import com.railly_linker.springboot_mvc_project_template.annotations.CustomTransactional
import com.railly_linker.springboot_mvc_project_template.configurations.SecurityConfig
import com.railly_linker.springboot_mvc_project_template.configurations.database_configs.Database1Config
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories.*
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Member_MemberData
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Member_MemberEmailData
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Member_MemberPhoneData
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.Database1_Member_MemberRoleData
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.repositories.*
import com.railly_linker.springboot_mvc_project_template.data_sources.redis_sources.redis1.tables.*
import com.railly_linker.springboot_mvc_project_template.util_dis.EmailSenderUtilDi
import com.railly_linker.springboot_mvc_project_template.util_objects.AuthorizationTokenUtilObject
import com.railly_linker.springboot_mvc_project_template.util_objects.JwtTokenUtilObject
import com.railly_linker.springboot_mvc_project_template.util_objects.NaverSmsUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class C7TkAuthService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val passwordEncoder: PasswordEncoder,
    private val emailSenderUtilDi: EmailSenderUtilDi,

    // (Database1 Repository)
    private val database1MemberMemberDataRepository: Database1_Member_MemberDataRepository,
    private val database1MemberMemberRoleDataRepository: Database1_Member_MemberRoleDataRepository,
    private val database1MemberMemberEmailDataRepository: Database1_Member_MemberEmailDataRepository,
    private val database1MemberMemberPhoneDataRepository: Database1_Member_MemberPhoneDataRepository,
    private val database1MemberMemberOauth2LoginDataRepository: Database1_Member_MemberOauth2LoginDataRepository,

    // (Redis1 Repository)
    private val redis1SignInAccessTokenInfoRepository: Redis1_SignInAccessTokenInfoRepository,
    private val redis1RefreshTokenInfoRepository: Redis1_RefreshTokenInfoRepository,
    private val redis1RegisterMembershipEmailVerificationRepository: Redis1_RegisterMembershipEmailVerificationRepository,
    private val redis1RegisterMembershipPhoneNumberVerificationRepository: Redis1_RegisterMembershipPhoneNumberVerificationRepository,
    private val redis1FindPasswordEmailVerificationRepository: Redis1_FindPasswordEmailVerificationRepository,
    private val redis1FindPasswordPhoneNumberVerificationRepository: Redis1_FindPasswordPhoneNumberVerificationRepository,
    private val redis1AddEmailVerificationRepository: Redis1_AddEmailVerificationRepository,
    private val redis1AddPhoneNumberVerificationRepository: Redis1_AddPhoneNumberVerificationRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // Retrofit2 요청 객체
    val networkRetrofit2Mbr: RepositoryNetworkRetrofit2 = RepositoryNetworkRetrofit2.getInstance()

    // 회원 가입 이메일 검증 만료시간(초)
    private var signUpEmailVerificationTimeSecMbr: Long = 60 * 10

    // 회원 가입 이메일 검증 완료 후 회원가입을 할 때까지의 만료시간(초)
    private var signUpEmailVerificationTimeUntilJoinSecMbr: Long = 60 * 10

    // 비밀번호 찾기 이메일 검증 만료시간(초)
    private var findPwEmailVerificationTimeSecMbr: Long = 60 * 10

    // 비밀번호 찾기 이메일 검증 완료 후 회원가입을 할 때까지의 만료시간(초)
    private var findPwEmailVerificationTimeUntilJoinSecMbr: Long = 60 * 10

    // 회원 가입 SMS 검증 만료시간(초)
    private var signUpSmsVerificationTimeSecMbr: Long = 60 * 10

    // 회원 가입 SMS 검증 완료 후 회원가입을 할 때까지의 만료시간(초)
    private var signUpSmsVerificationTimeUntilJoinSecMbr: Long = 60 * 10

    // 비밀번호 찾기 SMS 검증 만료시간(초)
    private var findPwSmsVerificationTimeSecMbr: Long = 60 * 10

    // 비밀번호 찾기 SMS 검증 완료 후 비밀번호 찾기 할 때까지의 만료시간(초)
    private var findPwSmsVerificationTimeUntilJoinSecMbr: Long = 60 * 10

    // 이메일 추가 이메일 검증 만료시간(초)
    private var addEmailVerificationTimeSecMbr: Long = 60 * 10

    // 이메일 추가 이메일 검증 완료 후 추가 할 때까지의 만료시간(초)
    private var addEmailVerificationTimeUntilJoinSecMbr: Long = 60 * 10

    // 전화번호 추가 문자 검증 만료시간(초)
    private var addPhoneNumberVerificationTimeSecMbr: Long = 60 * 10

    // 전화번호 추가 문자 검증 완료 후 추가 할 때까지의 만료시간(초)
    private var addPhoneNumberVerificationTimeUntilJoinSecMbr: Long = 60 * 10

    // 회원 가입 OAuth2 검증 만료시간(초)
    private var signUpOAuth2VerificationTimeSecMbr: Long = 60 * 10


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): Map<String, Any>? {
        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "connectTest OK!"
        httpServletResponse.setHeader("api-result-code", "ok")
        return result
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization).toLong()

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"
        httpServletResponse.setHeader("api-result-code", "ok")
        return result

    }

    ////
    fun api3(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization).toLong()

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"
        httpServletResponse.setHeader("api-result-code", "ok")
        return result

    }

    ////
    fun api4(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization).toLong()

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"
        httpServletResponse.setHeader("api-result-code", "ok")
        return result

    }

    ////
    @CustomRedisTransactional(
        [
            Redis1_SignInAccessTokenInfo.TRANSACTION_NAME,
            Redis1_RefreshTokenInfo.TRANSACTION_NAME
        ]
    )
    fun api5(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api5InputVo
    ): C7TkAuthController.Api5OutputVo? {
        val memberUid: Long
        when (inputVo.signInTypeCode) {
            0 -> { // 닉네임
                // (정보 검증 로직 수행)
                val member = database1MemberMemberDataRepository.findByNickNameAndRowActivate(
                    inputVo.id,
                    true
                )

                if (member == null) { // 가입된 회원이 없음
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }
                memberUid = member.uid!!
            }

            1 -> { // 이메일
                // (정보 검증 로직 수행)
                val memberEmail = database1MemberMemberEmailDataRepository.findByEmailAddressAndRowActivate(
                    inputVo.id,
                    true
                )

                if (memberEmail == null) { // 가입된 회원이 없음
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }
                memberUid = memberEmail.memberUid
            }

            2 -> { // 전화번호
                // (정보 검증 로직 수행)
                val memberPhone = database1MemberMemberPhoneDataRepository.findByPhoneNumberAndRowActivate(
                    inputVo.id,
                    true
                )

                if (memberPhone == null) { // 가입된 회원이 없음
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }
                memberUid = memberPhone.memberUid
            }

            else -> {
                throw Exception("signInType Not Supported")
            }
        }

        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            memberUid,
            true
        )

        if (member == null) { // 가입된 회원이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (member.accountPassword == null || // 페스워드는 아직 만들지 않음
            !passwordEncoder.matches(inputVo.password, member.accountPassword!!) // 패스워드 불일치
        ) {
            // 두 상황 모두 비밀번호 찾기를 하면 해결이 됨
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // (토큰 생성 로직 수행)
        val memberUidString: String = memberUid.toString()

        // 멤버 고유번호로 엑세스 토큰 생성
        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(memberUidString)

        val accessTokenExpireWhen: String
        @Suppress("KotlinConstantConditions")
        if (SecurityConfig.SAME_MEMBER_SIGN_IN_COUNT < 0) { // 동시 로그인 무제한으로 설정
            // 로그인 허용 액세스 토큰에 입력
            redis1SignInAccessTokenInfoRepository.saveKeyValue(
                "Bearer $jwtAccessToken",
                Redis1_SignInAccessTokenInfo(
                    memberUidString,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
                ),
                JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS
            )

            accessTokenExpireWhen = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS"
            ).format(Calendar.getInstance().apply {
                this.time = Date()
                this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
            }.time)
        } else { // 동시 로그인 제한 설정
            // loginAccessToken 의 Iterable 가져오기
            val loginAccessTokenIterable = redis1SignInAccessTokenInfoRepository.findAllKeyValues()

            // Iterable 중 내 memberUid 와 동일한 정보를 가져와 리스트화
            val loginAccessTokenArrayList: ArrayList<Redis1_SignInAccessTokenInfoRepository.KeyValueData> = ArrayList()
            for (loginAccessToken in loginAccessTokenIterable) {
                if (loginAccessToken.value.memberUid == memberUidString) {
                    loginAccessTokenArrayList.add(loginAccessToken)
                }
            }

            // 리스트 최신순 정렬
            loginAccessTokenArrayList.sortWith { a, b ->
                java.lang.Long.valueOf(
                    LocalDateTime.parse(
                        b.value.signInDateString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    )
                        .atZone(
                            ZoneId.systemDefault()
                        ).toInstant().toEpochMilli()
                )
                    .compareTo(
                        LocalDateTime.parse(
                            a.value.signInDateString,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                        )
                            .atZone(
                                ZoneId.systemDefault()
                            ).toInstant().toEpochMilli()
                    )
            }

            // 로그인 허용 개수만큼 앞에서 자르기
            val loginAccessTokenListTake = loginAccessTokenArrayList.take(SecurityConfig.SAME_MEMBER_SIGN_IN_COUNT)

            if (loginAccessTokenListTake.size < SecurityConfig.SAME_MEMBER_SIGN_IN_COUNT) { // 현 로그인 개수가 허용치보다 작다면
                // 로그인 허용 액세스 토큰에 입력
                redis1SignInAccessTokenInfoRepository.saveKeyValue(
                    "Bearer $jwtAccessToken",
                    Redis1_SignInAccessTokenInfo(
                        memberUidString,
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
                    ),
                    JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS
                )

                accessTokenExpireWhen = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SSS"
                ).format(Calendar.getInstance().apply {
                    this.time = Date()
                    this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
                }.time)
            } else { // 현 로그인 개수가 허용치 이상일 경우
                if (SecurityConfig.SAME_MEMBER_SIGN_IN_OVER_POLICY == 0) { // 추가 로그인 금지 설정
                    // 혹여 현 설정 로그인 개수를 초과했다면 맞춰주기
                    for (loginAccessToken in loginAccessTokenArrayList) {
                        redis1SignInAccessTokenInfoRepository.deleteKeyValue(loginAccessToken.key)
                    }
                    for (loginAccessToken in loginAccessTokenListTake) {
                        redis1SignInAccessTokenInfoRepository.saveKeyValue(
                            loginAccessToken.key,
                            loginAccessToken.value,
                            loginAccessToken.expireTimeMs
                        )
                    }

                    httpServletResponse.setHeader("api-result-code", "3")
                    return null
                } else { // 기존 로그인 제거 설정
                    // list to mutableList
                    val newLoginAccessTokenArrayList = ArrayList(loginAccessTokenListTake)

                    // 초과분 마지막(=오래된) 액세스 토큰 정보 제거
                    newLoginAccessTokenArrayList.removeLast()
                    // 새 액세스 토큰 정보를 앞에 추가
                    newLoginAccessTokenArrayList.add(
                        0,
                        Redis1_SignInAccessTokenInfoRepository.KeyValueData(
                            "Bearer $jwtAccessToken",
                            Redis1_SignInAccessTokenInfo(
                                memberUidString,
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
                            ),
                            JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS
                        )
                    )

                    accessTokenExpireWhen = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                    ).format(Calendar.getInstance().apply {
                        this.time = Date()
                        this.add(
                            Calendar.MILLISECOND,
                            JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt()
                        )
                    }.time)

                    // 새로 결정된 액세스 로그인 액세스 토큰 리스트를 반영
                    for (loginAccessToken in loginAccessTokenArrayList) {
                        redis1SignInAccessTokenInfoRepository.deleteKeyValue(loginAccessToken.key)
                    }
                    for (loginAccessToken in newLoginAccessTokenArrayList) {
                        redis1SignInAccessTokenInfoRepository.saveKeyValue(
                            loginAccessToken.key,
                            loginAccessToken.value,
                            loginAccessToken.expireTimeMs
                        )
                    }
                }
            }
        }

        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(memberUidString)

        redis1RefreshTokenInfoRepository.saveKeyValue(
            "Bearer $jwtAccessToken",
            Redis1_RefreshTokenInfo(
                jwtRefreshToken
            ),
            JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS
        )

        val refreshTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt())
        }.time)

        // 멤버의 권한 리스트를 조회 후 반환
        val memberRoleList = database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(
            memberUid,
            true
        )

        val roleList: MutableList<Int> = ArrayList()
        for (userRole in memberRoleList) {
            roleList.add(userRole.roleCode.toInt())
        }

        val emailEntityList = database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUid, true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList = database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUid, true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUid, true)
        val myOAuth2List = ArrayList<C7TkAuthController.Api5OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C7TkAuthController.Api5OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api5OutputVo(
            memberUidString,
            member.nickName,
            roleList,
            "Bearer",
            jwtAccessToken,
            jwtRefreshToken,
            accessTokenExpireWhen,
            refreshTokenExpireWhen,
            emailList,
            phoneNumberList,
            myOAuth2List
        )
    }


    ////
    // todo
//    fun api6(
//        httpServletResponse: HttpServletResponse,
//        oauth2TypeCode: Int,
//        oauth2Code: String
//    ): C7TkAuthController.Api6OutputVo? {
//        val snsAccessToken: String
//
//        // (정보 검증 로직 수행)
//        when (oauth2TypeCode) {
//            1 -> { // NAVER
//                val clientId = "GM1MR5AmXmEWkia_Yumw"
//                val clientSecret = "wpEaHRJ1zY"
//                val redirectUri = "http://localhost/auth.html"
//
//                // Access Token 가져오기
//                val atResponse = networkRetrofit2Mbr.nidNaverComRequestApiMbr.getOauth2Dot0Token(
//                    "authorization_code",
//                    clientId,
//                    clientSecret,
//                    redirectUri,
//                    oauth2Code,
//                    "3789" // 클라이언트와 상의 후 결정
//                ).execute()
//
//                // code 사용 결과 검증
//                if (atResponse.code() != 200 ||
//                    atResponse.body() == null ||
//                    atResponse.body()!!.accessToken == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "1")
//                    return null
//                }
//
//                snsAccessToken = atResponse.body()!!.accessToken!!
//            }
//
//            else -> {
//                throw Exception("SNS Login Type not supported")
//            }
//        }
//
//        httpServletResponse.setHeader("api-result-code", "ok")
//        return C7TkAuthController.Api6OutputVo(
//            snsAccessToken
//        )
//    }


    ////
    // todo
//    @CustomRedisTransactional(
//        [
//            Redis1_SignInAccessTokenInfo.TRANSACTION_NAME,
//            Redis1_RefreshTokenInfo.TRANSACTION_NAME
//        ]
//    )
//    fun api7(
//        httpServletResponse: HttpServletResponse,
//        inputVo: C7TkAuthController.Api7InputVo
//    ): C7TkAuthController.Api7OutputVo? {
//        val snsOauth2: Database1_Member_MemberOauth2LoginData?
//
//        // (정보 검증 로직 수행)
//        when (inputVo.oauth2TypeCode) {
//            1 -> { // GOOGLE
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.googleApisComRequestApiMbr.getOauth2V1Userinfo(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.code() != 200 ||
//                    response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                snsOauth2 = database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                    1,
//                    response.body()!!.id,
//                    true
//                )
//            }
//
//            2 -> { // APPLE
//                // (아래는 Id 토큰으로 검증)
//                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2Secret)
//
//                val loginId: String
//                if (appleInfo != null) {
//                    loginId = appleInfo.snsId
//                } else {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                snsOauth2 = database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                    2,
//                    loginId,
//                    true
//                )
//            }
//
//            3 -> { // NAVER
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.openapiNaverComRequestApiMbr.getV1NidMe(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.body() == null) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                snsOauth2 = database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                    3,
//                    response.body()!!.response.id,
//                    true
//                )
//            }
//
//            4 -> { // KAKAO
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.kapiKakaoComRequestApiMbr.getV2UserMe(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.code() != 200 ||
//                    response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                snsOauth2 = database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                    4,
//                    response.body()!!.id.toString(),
//                    true
//                )
//            }
//
//            else -> {
//                throw Exception("SNS Login Type not supported")
//            }
//        }
//
//        if (snsOauth2 == null) { // 가입된 회원이 없음
//            httpServletResponse.setHeader("api-result-code", "1")
//            return null
//        }
//
//        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
//            snsOauth2.memberUid,
//            true
//        )
//
//        if (member == null) { // 가입된 회원이 없음
//            httpServletResponse.setHeader("api-result-code", "1")
//            return null
//        }
//
//        // (토큰 생성 로직 수행)
//        // 멤버 고유번호로 엑세스 토큰 생성
//        val memberUidString: String = snsOauth2.memberUid.toString()
//
//        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(memberUidString)
//
//        val accessTokenExpireWhen: String
//        @Suppress("KotlinConstantConditions")
//        if (SecurityConfig.SAME_MEMBER_SIGN_IN_COUNT < 0) { // 동시 로그인 무제한으로 설정
//            // 로그인 허용 액세스 토큰에 입력
//            redis1SignInAccessTokenInfoRepository.saveKeyValue(
//                "Bearer $jwtAccessToken",
//                Redis1_SignInAccessTokenInfo(
//                    memberUidString,
//                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
//                ),
//                JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS
//            )
//
//            accessTokenExpireWhen = SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss.SSS"
//            ).format(Calendar.getInstance().apply {
//                this.time = Date()
//                this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
//            }.time)
//        } else { // 동시 로그인 제한 설정
//            // loginAccessToken 의 Iterable 가져오기
//            val loginAccessTokenIterable = redis1SignInAccessTokenInfoRepository.findAllKeyValues()
//
//            // Iterable 중 내 memberUid 와 동일한 정보를 가져와 리스트화
//            val loginAccessTokenArrayList: ArrayList<Redis1_SignInAccessTokenInfoRepository.KeyValueData> = ArrayList()
//            for (loginAccessToken in loginAccessTokenIterable) {
//                if (loginAccessToken.value.memberUid == memberUidString) {
//                    loginAccessTokenArrayList.add(loginAccessToken)
//                }
//            }
//
//            // 리스트 최신순 정렬
//            loginAccessTokenArrayList.sortWith { a, b ->
//                java.lang.Long.valueOf(
//                    LocalDateTime.parse(
//                        b.value.signInDateString,
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//                    )
//                        .atZone(
//                            ZoneId.systemDefault()
//                        ).toInstant().toEpochMilli()
//                )
//                    .compareTo(
//                        LocalDateTime.parse(
//                            a.value.signInDateString,
//                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//                        )
//                            .atZone(
//                                ZoneId.systemDefault()
//                            ).toInstant().toEpochMilli()
//                    )
//            }
//
//            // 로그인 허용 개수만큼 앞에서 자르기
//            val loginAccessTokenListTake = loginAccessTokenArrayList.take(SecurityConfig.SAME_MEMBER_SIGN_IN_COUNT)
//
//            if (loginAccessTokenListTake.size < SecurityConfig.SAME_MEMBER_SIGN_IN_COUNT) { // 현 로그인 개수가 허용치보다 작다면
//                // 로그인 허용 액세스 토큰에 입력
//                redis1SignInAccessTokenInfoRepository.saveKeyValue(
//                    "Bearer $jwtAccessToken",
//                    Redis1_SignInAccessTokenInfo(
//                        memberUidString,
//                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
//                    ),
//                    JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS
//                )
//
//                accessTokenExpireWhen = SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss.SSS"
//                ).format(Calendar.getInstance().apply {
//                    this.time = Date()
//                    this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
//                }.time)
//            } else { // 현 로그인 개수가 허용치 이상일 경우
//                if (SecurityConfig.SAME_MEMBER_SIGN_IN_OVER_POLICY == 0) { // 추가 로그인 금지 설정
//                    // 혹여 현 설정 로그인 개수를 초과했다면 맞춰주기
//                    for (loginAccessToken in loginAccessTokenArrayList) {
//                        redis1SignInAccessTokenInfoRepository.deleteKeyValue(loginAccessToken.key)
//                    }
//                    for (loginAccessToken in loginAccessTokenListTake) {
//                        redis1SignInAccessTokenInfoRepository.saveKeyValue(
//                            loginAccessToken.key,
//                            loginAccessToken.value,
//                            loginAccessToken.expireTimeMs
//                        )
//                    }
//
//                    httpServletResponse.setHeader("api-result-code", "3")
//                    return null
//                } else { // 기존 로그인 제거 설정
//                    // list to mutableList
//                    val newLoginAccessTokenArrayList = ArrayList(loginAccessTokenListTake)
//
//                    // 초과분 마지막(=오래된) 액세스 토큰 정보 제거
//                    newLoginAccessTokenArrayList.removeLast()
//                    // 새 액세스 토큰 정보를 앞에 추가
//                    newLoginAccessTokenArrayList.add(
//                        0,
//                        Redis1_SignInAccessTokenInfoRepository.KeyValueData(
//                            "Bearer $jwtAccessToken",
//                            Redis1_SignInAccessTokenInfo(
//                                memberUidString,
//                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
//                            ),
//                            JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS
//                        )
//                    )
//
//                    accessTokenExpireWhen = SimpleDateFormat(
//                        "yyyy-MM-dd HH:mm:ss.SSS"
//                    ).format(Calendar.getInstance().apply {
//                        this.time = Date()
//                        this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
//                    }.time)
//
//                    // 새로 결정된 액세스 로그인 액세스 토큰 리스트를 반영
//                    for (loginAccessToken in loginAccessTokenArrayList) {
//                        redis1SignInAccessTokenInfoRepository.deleteKeyValue(loginAccessToken.key)
//                    }
//                    for (loginAccessToken in newLoginAccessTokenArrayList) {
//                        redis1SignInAccessTokenInfoRepository.saveKeyValue(
//                            loginAccessToken.key,
//                            loginAccessToken.value,
//                            loginAccessToken.expireTimeMs
//                        )
//                    }
//                }
//            }
//        }
//
//        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
//        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(memberUidString)
//
//        redis1RefreshTokenInfoRepository.saveKeyValue(
//            "Bearer $jwtAccessToken",
//            Redis1_RefreshTokenInfo(
//                jwtRefreshToken
//            ),
//            JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS
//        )
//
//        val refreshTokenExpireWhen: String = SimpleDateFormat(
//            "yyyy-MM-dd HH:mm:ss.SSS"
//        ).format(Calendar.getInstance().apply {
//            this.time = Date()
//            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt())
//        }.time)
//
//        // 멤버의 권한 리스트를 조회 후 반환
//        val memberRoleList = database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(
//            snsOauth2.memberUid,
//            true
//        )
//
//        val roleList: MutableList<Int> = ArrayList()
//        for (userRole in memberRoleList) {
//            roleList.add(userRole.roleCode.toInt())
//        }
//
//        val emailEntityList = database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
//        val emailList = ArrayList<String>()
//        for (emailEntity in emailEntityList) {
//            emailList.add(
//                emailEntity.emailAddress
//            )
//        }
//
//        val phoneEntityList = database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
//        val phoneNumberList = ArrayList<String>()
//        for (emailEntity in phoneEntityList) {
//            phoneNumberList.add(
//                emailEntity.phoneNumber
//            )
//        }
//
//        val oAuth2EntityList =
//            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
//        val myOAuth2List = ArrayList<C7TkAuthController.Api7OutputVo.OAuth2Info>()
//        for (oAuth2Entity in oAuth2EntityList) {
//            myOAuth2List.add(
//                C7TkAuthController.Api7OutputVo.OAuth2Info(
//                    oAuth2Entity.oauth2TypeCode.toInt(),
//                    oAuth2Entity.oauth2Id
//                )
//            )
//        }
//
//        httpServletResponse.setHeader("api-result-code", "ok")
//        return C7TkAuthController.Api7OutputVo(
//            memberUidString,
//            member.nickName,
//            roleList,
//            "Bearer",
//            jwtAccessToken,
//            jwtRefreshToken,
//            accessTokenExpireWhen,
//            refreshTokenExpireWhen,
//            emailList,
//            phoneNumberList,
//            myOAuth2List
//        )
//    }


    ////
    // 주의점 : 클라이언트 입장에선 강제종료 등의 이유로 항상 로그인과 로그아웃이 쌍을 이루는 것은 아니기에 이점을 유의
    @CustomRedisTransactional(
        [
            Redis1_SignInAccessTokenInfo.TRANSACTION_NAME,
            Redis1_RefreshTokenInfo.TRANSACTION_NAME
        ]
    )
    fun api8(authorization: String, httpServletResponse: HttpServletResponse) {
        // 해당 멤버의 리프레시 토큰 정보 삭제
        redis1RefreshTokenInfoRepository.deleteKeyValue(authorization)

        // 로그인 가능 액세스 토큰 정보 삭제
        redis1SignInAccessTokenInfoRepository.deleteKeyValue(authorization)

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_SignInAccessTokenInfo.TRANSACTION_NAME,
            Redis1_RefreshTokenInfo.TRANSACTION_NAME
        ]
    )
    fun api9(
        authorization: String,
        inputVo: C7TkAuthController.Api9InputVo,
        httpServletResponse: HttpServletResponse
    ): C7TkAuthController.Api9OutputVo? {
        val accessTokenMemberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val memberInfo =
            database1MemberMemberDataRepository.findByUidAndRowActivate(accessTokenMemberUid.toLong(), true)

        if (memberInfo == null) {
            // 가입되지 않은 회원
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 타입과 토큰을 분리
        val refreshTokenInputSplit = inputVo.refreshToken.split(" ") // ex : ["Bearer", "qwer1234"]

        if (refreshTokenInputSplit.size >= 2) { // 타입으로 추정되는 문장이 존재할 때
            // 타입 분리
            val tokenType = refreshTokenInputSplit[0].trim() // 첫번째 단어는 토큰 타입
            val jwtRefreshToken = refreshTokenInputSplit[1].trim() // 앞의 타입을 자르고 남은 토큰

            when (tokenType.lowercase()) { // 타입 검증
                "bearer" -> { // Bearer JWT 토큰 검증
                    // 토큰 문자열 해석 가능여부 확인
                    val refreshTokenType: String? = try {
                        JwtTokenUtilObject.getTokenType(jwtRefreshToken)
                    } catch (_: Exception) {
                        null
                    }

                    // 리프레시 토큰 검증
                    if (refreshTokenType == null || // 해석 불가능한 리프레시 토큰
                        !JwtTokenUtilObject.validateSignature(jwtRefreshToken) || // 시크릿 검증이 유효하지 않을 때 = 시크릿 검증시 정보가 틀림 = 위변조된 토큰
                        JwtTokenUtilObject.getTokenUsage(jwtRefreshToken)
                            .lowercase() != "refresh" || // 토큰 타입이 Refresh 토큰이 아닐 때
                        JwtTokenUtilObject.getIssuer(jwtRefreshToken) != JwtTokenUtilObject.ISSUER || // 발행인이 다를 때
                        refreshTokenType.lowercase() != "jwt" || // 토큰 타입이 JWT 가 아닐 때
                        JwtTokenUtilObject.getRemainSeconds(jwtRefreshToken) * 1000 > JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS || // 최대 만료시간을 초과
                        JwtTokenUtilObject.getMemberUid(jwtRefreshToken) != accessTokenMemberUid // 리프레시 토큰의 멤버 고유번호와 액세스 토큰 멤버 고유번호가 다를시
                    ) {
                        httpServletResponse.setHeader("api-result-code", "2")
                        return null
                    }

                    // 저장된 현재 인증된 멤버의 리프레시 토큰 가져오기
                    val refreshTokenInfoOptional = redis1RefreshTokenInfoRepository.findKeyValue(authorization)

                    if (JwtTokenUtilObject.getRemainSeconds(jwtRefreshToken) == 0L || // 만료시간 지남
                        refreshTokenInfoOptional == null // jwtAccessToken 의 리프레시 토큰이 저장소에 없음
                    ) {
                        httpServletResponse.setHeader("api-result-code", "3")
                        return null
                    }

                    if (jwtRefreshToken != refreshTokenInfoOptional.value.refreshToken) {
                        // 건내받은 토큰이 해당 액세스 토큰의 가용 토큰과 맞지 않음
                        httpServletResponse.setHeader("api-result-code", "4")
                        return null
                    }

                    // 먼저 로그아웃 처리
                    // DB 내 해당 멤버의 리프레시 토큰 정보 삭제
                    redis1RefreshTokenInfoRepository.deleteKeyValue(authorization)

                    // 로그인 가능 액세스 토큰 정보 삭제
                    redis1SignInAccessTokenInfoRepository.deleteKeyValue(authorization)

                    // 새 토큰 생성 및 로그인 처리
                    val newJwtAccessToken = JwtTokenUtilObject.generateAccessToken(accessTokenMemberUid)

                    // 로그인 허용 액세스 토큰에 입력
                    redis1SignInAccessTokenInfoRepository.saveKeyValue(
                        "Bearer $newJwtAccessToken",
                        Redis1_SignInAccessTokenInfo(
                            accessTokenMemberUid,
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
                        ),
                        JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS
                    )

                    val accessTokenExpireWhen: String = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                    ).format(Calendar.getInstance().apply {
                        this.time = Date()
                        this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
                    }.time)

                    val newRefreshToken = JwtTokenUtilObject.generateRefreshToken(accessTokenMemberUid)
                    redis1RefreshTokenInfoRepository.saveKeyValue(
                        "Bearer $newJwtAccessToken",
                        Redis1_RefreshTokenInfo(
                            newRefreshToken
                        ),
                        JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS
                    )

                    val refreshTokenExpireWhen: String = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                    ).format(Calendar.getInstance().apply {
                        this.time = Date()
                        this.add(Calendar.MILLISECOND, JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt())
                    }.time)

                    // 멤버의 권한 리스트를 조회 후 반환
                    val userRoleList = database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(
                        accessTokenMemberUid.toLong(),
                        true
                    )

                    val roleList: MutableList<Int> = ArrayList()
                    for (userRole in userRoleList) {
                        roleList.add(userRole.roleCode.toInt())
                    }

                    val emailEntityList = database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(
                        accessTokenMemberUid.toLong(),
                        true
                    )
                    val emailList = ArrayList<String>()
                    for (emailEntity in emailEntityList) {
                        emailList.add(
                            emailEntity.emailAddress
                        )
                    }

                    val phoneEntityList = database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(
                        accessTokenMemberUid.toLong(),
                        true
                    )
                    val phoneNumberList = ArrayList<String>()
                    for (emailEntity in phoneEntityList) {
                        phoneNumberList.add(
                            emailEntity.phoneNumber
                        )
                    }

                    val oAuth2EntityList =
                        database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(
                            accessTokenMemberUid.toLong(),
                            true
                        )
                    val myOAuth2List = ArrayList<C7TkAuthController.Api9OutputVo.OAuth2Info>()
                    for (oAuth2Entity in oAuth2EntityList) {
                        myOAuth2List.add(
                            C7TkAuthController.Api9OutputVo.OAuth2Info(
                                oAuth2Entity.oauth2TypeCode.toInt(),
                                oAuth2Entity.oauth2Id
                            )
                        )
                    }

                    httpServletResponse.setHeader("api-result-code", "ok")
                    return C7TkAuthController.Api9OutputVo(
                        accessTokenMemberUid,
                        memberInfo.nickName,
                        roleList,
                        "Bearer",
                        newJwtAccessToken,
                        newRefreshToken,
                        accessTokenExpireWhen,
                        refreshTokenExpireWhen,
                        emailList,
                        phoneNumberList,
                        myOAuth2List
                    )
                }

                else -> {
                    // 처리 가능한 토큰 타입이 아닐 때
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }
            }
        } else {
            // 타입을 전달 하지 않았을 때
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_SignInAccessTokenInfo.TRANSACTION_NAME,
            Redis1_RefreshTokenInfo.TRANSACTION_NAME
        ]
    )
    fun api10(authorization: String, httpServletResponse: HttpServletResponse) {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // loginAccessToken 의 Iterable 가져오기
        val loginAccessTokenIterable = redis1SignInAccessTokenInfoRepository.findAllKeyValues()

        // 발행되었던 모든 액세스 토큰 무효화 (다른 디바이스에선 사용중 로그아웃된 것과 동일한 효과)
        for (loginAccessToken in loginAccessTokenIterable) {
            if (loginAccessToken.value.memberUid == memberUid) {
                // DB 내 해당 멤버의 리프레시 토큰 정보 삭제
                redis1RefreshTokenInfoRepository.deleteKeyValue(loginAccessToken.key)

                // 로그인 가능 액세스 토큰 정보 삭제
                redis1SignInAccessTokenInfoRepository.deleteKeyValue(loginAccessToken.key)
            }
        }

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    fun api11(httpServletResponse: HttpServletResponse, nickName: String): C7TkAuthController.Api11OutputVo? {
        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api11OutputVo(
            database1MemberMemberDataRepository.existsByNickNameAndRowActivate(nickName.trim(), true)
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api12(httpServletResponse: HttpServletResponse, authorization: String, nickName: String) {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val userInfo = database1MemberMemberDataRepository.findById(memberUid.toLong()).get()

        if (database1MemberMemberDataRepository.existsByNickNameAndRowActivate(nickName, true)) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        userInfo.nickName = nickName
        database1MemberMemberDataRepository.save(
            userInfo
        )

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_RegisterMembershipEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api13(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api13InputVo
    ): C7TkAuthController.Api13OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                inputVo.email,
                true
            )

        if (isDatabase1MemberUserExists) { // 기존 회원 존재
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 이메일 발송
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        redis1RegisterMembershipEmailVerificationRepository.saveKeyValue(
            inputVo.email,
            Redis1_RegisterMembershipEmailVerification(
                verificationCode
            ),
            signUpEmailVerificationTimeSecMbr * 1000
        )

        val expireWhen = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.SECOND, signUpEmailVerificationTimeSecMbr.toInt())
        }.time)

        emailSenderUtilDi.sendThymeLeafHtmlMail(
            "Springboot Mvc Project Template",
            arrayOf(inputVo.email),
            null,
            "Springboot Mvc Project Template 회원가입 - 본인 계정 확인용 이메일입니다.",
            "template_c7_n13/email_verification_email",
            hashMapOf(
                Pair("verificationCode", verificationCode)
            ),
            null,
            null,
            null,
            null
        )

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api13OutputVo(
            expireWhen
        )
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_RegisterMembershipEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api14(
        httpServletResponse: HttpServletResponse,
        email: String,
        verificationCode: String
    ): C7TkAuthController.Api14OutputVo? {
        val emailVerification = redis1RegisterMembershipEmailVerificationRepository.findKeyValue(email)

        if (emailVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.value.secret == verificationCode

        if (codeMatched) { // 코드 일치
            redis1RegisterMembershipEmailVerificationRepository.saveKeyValue(
                email,
                emailVerification.value,
                signUpEmailVerificationTimeUntilJoinSecMbr * 1000
            )

            val expireWhen = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS"
            ).format(Calendar.getInstance().apply {
                this.time = Date()
                this.add(Calendar.SECOND, signUpEmailVerificationTimeUntilJoinSecMbr.toInt())
            }.time)

            httpServletResponse.setHeader("api-result-code", "ok")
            return C7TkAuthController.Api14OutputVo(
                expireWhen
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    @CustomRedisTransactional(
        [
            Redis1_RegisterMembershipEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api15(httpServletResponse: HttpServletResponse, inputVo: C7TkAuthController.Api15InputVo) {
        val loginId: String = inputVo.email // 검증 로직에서 정해지면 충족시키기
        // 기존 회원 확인
        val isUserExists =
            database1MemberMemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                loginId,
                true
            )
        if (isUserExists) { // 기존 회원이 있을 때
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (database1MemberMemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 본인 이메일 검증 여부 확인
        val emailVerification = redis1RegisterMembershipEmailVerificationRepository.findKeyValue(loginId)

        if (emailVerification == null) { // 이메일 검증 요청을 하지 않거나 만료됨
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.value.secret == inputVo.verificationCode

        if (!codeMatched) { // 입력한 코드와 일치하지 않음 == 이메일 검증 요청을 하지 않거나 만료됨
            httpServletResponse.setHeader("api-result-code", "4")
            return
        }

        val password: String? = passwordEncoder.encode(inputVo.password) // 검증 로직에서 정해지면 충족시키기 // 비밀번호는 암호화

        // 회원가입
        val database1MemberUser = database1MemberMemberDataRepository.save(
            Database1_Member_MemberData(
                inputVo.nickName,
                password,
                true
            )
        )

        // 이메일 저장
        database1MemberMemberEmailDataRepository.save(
            Database1_Member_MemberEmailData(
                database1MemberUser.uid!!,
                loginId,
                true
            )
        )

        // 역할 저장
        val database1MemberUserRoleList = ArrayList<Database1_Member_MemberRoleData>()
        // 기본 권한 추가
//        database1MemberUserRoleList.add(
//            Database1_Member_MemberRole(
//                database1MemberUser.uid!!,
//                2,
//                true
//            )
//        )
        database1MemberMemberRoleDataRepository.saveAll(database1MemberUserRoleList)

        // 확인 완료된 검증 요청 정보 삭제
        redis1RegisterMembershipEmailVerificationRepository.deleteKeyValue(loginId)
        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_RegisterMembershipPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api16(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api16InputVo
    ): C7TkAuthController.Api16OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                inputVo.phoneNumber,
                true
            )
        if (isDatabase1MemberUserExists) { // 기존 회원 존재
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 문자 발송
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        redis1RegisterMembershipPhoneNumberVerificationRepository.saveKeyValue(
            inputVo.phoneNumber,
            Redis1_RegisterMembershipPhoneNumberVerification(
                verificationCode
            ),
            signUpSmsVerificationTimeSecMbr * 1000
        )

        val expireWhen = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.SECOND, signUpSmsVerificationTimeSecMbr.toInt())
        }.time)

        val phoneNumberSplit = inputVo.phoneNumber.split(")") // ["82", "010-0000-0000"]

        // 국가 코드 (ex : 82)
        val countryCode = phoneNumberSplit[0]

        // 전화번호 (ex : "01000000000")
        val phoneNumber = (phoneNumberSplit[1].replace("-", "")).replace(" ", "")

        NaverSmsUtilObject.sendSms(
            NaverSmsUtilObject.SendSmsInputVo(
                countryCode,
                phoneNumber,
                "[Springboot Mvc Project Template - 회원가입] 인증번호 [${verificationCode}]"
            )
        )

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api16OutputVo(
            expireWhen
        )
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_RegisterMembershipPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api17(
        httpServletResponse: HttpServletResponse,
        phoneNumber: String,
        verificationCode: String
    ): C7TkAuthController.Api17OutputVo? {
        val smsVerification = redis1RegisterMembershipPhoneNumberVerificationRepository.findKeyValue(phoneNumber)

        if (smsVerification == null) { // 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = smsVerification.value.secret == verificationCode

        if (codeMatched) { // 코드 일치
            redis1RegisterMembershipPhoneNumberVerificationRepository.saveKeyValue(
                phoneNumber,
                smsVerification.value,
                signUpSmsVerificationTimeUntilJoinSecMbr * 1000
            )

            val expireWhen = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS"
            ).format(Calendar.getInstance().apply {
                this.time = Date()
                this.add(Calendar.SECOND, signUpSmsVerificationTimeUntilJoinSecMbr.toInt())
            }.time)

            httpServletResponse.setHeader("api-result-code", "ok")
            return C7TkAuthController.Api17OutputVo(
                expireWhen
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    @CustomRedisTransactional(
        [
            Redis1_RegisterMembershipPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api18(httpServletResponse: HttpServletResponse, inputVo: C7TkAuthController.Api18InputVo) {
        val loginId: String = inputVo.phoneNumber // 검증 로직에서 정해지면 충족시키기
        // 기존 회원 확인
        val isUserExists =
            database1MemberMemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                loginId,
                true
            )
        if (isUserExists) { // 기존 회원이 있을 때
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (database1MemberMemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName, true)) {
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 본인 이메일 검증 여부 확인
        val emailVerification = redis1RegisterMembershipPhoneNumberVerificationRepository.findKeyValue(loginId)

        if (emailVerification == null) { // 검증 요청을 하지 않거나 만료됨
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.value.secret == inputVo.verificationCode

        if (!codeMatched) { // 입력한 코드와 일치하지 않음 == 이메일 검증 요청을 하지 않거나 만료됨
            httpServletResponse.setHeader("api-result-code", "4")
            return
        }

        val password: String? = passwordEncoder.encode(inputVo.password) // 검증 로직에서 정해지면 충족시키기 // 비밀번호는 암호화


        // 회원가입
        val database1MemberUser = database1MemberMemberDataRepository.save(
            Database1_Member_MemberData(
                inputVo.nickName,
                password,
                true
            )
        )

        // 전화번호 저장
        database1MemberMemberPhoneDataRepository.save(
            Database1_Member_MemberPhoneData(
                database1MemberUser.uid!!,
                loginId,
                true
            )
        )

        // 역할 저장
        val database1MemberUserRoleList = ArrayList<Database1_Member_MemberRoleData>()
        // 기본 권한 추가
//        database1MemberUserRoleList.add(
//            Database1_Member_MemberRole(
//                database1MemberUser.uid!!,
//                2,
//                true
//            )
//        )
        database1MemberMemberRoleDataRepository.saveAll(database1MemberUserRoleList)

        // 확인 완료된 검증 요청 정보 삭제
        redis1RegisterMembershipPhoneNumberVerificationRepository.deleteKeyValue(loginId)

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    // todo
//    @ProwdTransactional([Database1DatasourceConfig.platformTransactionManagerBeanName])
//    fun api19(
//        httpServletResponse: HttpServletResponse,
//        inputVo: C7TkAuthController.Api19InputVo
//    ): C7TkAuthController.Api19OutputVo? {
//        // (정보 검증 로직 수행)
//        when (inputVo.oauth2TypeCode) {
//            1 -> { // GOOGLE
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.googleApisComRequestApiMbr.getOauth2V1Userinfo(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.code() != 200 ||
//                    response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                val loginId = response.body()!!.id
//
//                val isDatabase1MemberUserExists =
//                    database1MemberMemberSnsOauth2Repository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                        1,
//                        loginId,
//                        true
//                    )
//                if (isDatabase1MemberUserExists) { // 기존 회원 존재
//                    httpServletResponse.setHeader("api-result-code", "1")
//                    return null
//                }
//
//                val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
//                RedisUtilObject.putValue<RegisterMembershipOAuth2GoogleVerification>(
//                    redis1RedisTemplate,
//                    loginId,
//                    RegisterMembershipOAuth2GoogleVerification(
//                        verificationCode
//                    ),
//                    signUpOAuth2VerificationTimeSecMbr * 1000
//                )
//
//                val expireWhen = SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss.SSS"
//                ).format(Calendar.getInstance().apply {
//                    this.time = Date()
//                    this.add(Calendar.SECOND, signUpOAuth2VerificationTimeSecMbr.toInt())
//                }.time)
//
//                return C7TkAuthController.Api16OutputVo(
//                    verificationCode,
//                    loginId,
//                    expireWhen
//                )
//            }
//
//            2 -> { // APPLE
//                // (아래는 Id 토큰으로 검증)
//                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2Secret)
//
//                val loginId: String
//                if (appleInfo != null) {
//                    loginId = appleInfo.snsId
//                } else {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                val isDatabase1MemberUserExists =
//                    database1MemberMemberSnsOauth2Repository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                        2,
//                        loginId,
//                        true
//                    )
//                if (isDatabase1MemberUserExists) { // 기존 회원 존재
//                    httpServletResponse.setHeader("api-result-code", "1")
//                    return null
//                }
//
//                val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
//                RedisUtilObject.putValue<RegisterMembershipOAuth2AppleVerification>(
//                    redis1RedisTemplate,
//                    loginId,
//                    RegisterMembershipOAuth2AppleVerification(
//                        verificationCode
//                    ),
//                    signUpOAuth2VerificationTimeSecMbr * 1000
//                )
//
//                val expireWhen = SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss.SSS"
//                ).format(Calendar.getInstance().apply {
//                    this.time = Date()
//                    this.add(Calendar.SECOND, signUpOAuth2VerificationTimeSecMbr.toInt())
//                }.time)
//
//                return C7TkAuthController.Api16OutputVo(
//                    verificationCode,
//                    loginId,
//                    expireWhen
//                )
//            }
//
//            3 -> { // NAVER
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.openapiNaverComRequestApiMbr.getV1NidMe(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                val isDatabase1MemberUserExists =
//                    database1MemberMemberSnsOauth2Repository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                        3,
//                        response.body()!!.response.id,
//                        true
//                    )
//                if (isDatabase1MemberUserExists) { // 기존 회원 존재
//                    httpServletResponse.setHeader("api-result-code", "1")
//                    return null
//                }
//
//                val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
//                RedisUtilObject.putValue<RegisterMembershipOAuth2NaverVerification>(
//                    redis1RedisTemplate,
//                    response.body()!!.response.id,
//                    RegisterMembershipOAuth2NaverVerification(
//                        verificationCode
//                    ),
//                    signUpOAuth2VerificationTimeSecMbr * 1000
//                )
//
//                val expireWhen = SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss.SSS"
//                ).format(Calendar.getInstance().apply {
//                    this.time = Date()
//                    this.add(Calendar.SECOND, signUpOAuth2VerificationTimeSecMbr.toInt())
//                }.time)
//
//                return C7TkAuthController.Api16OutputVo(
//                    verificationCode,
//                    response.body()!!.response.id,
//                    expireWhen
//                )
//            }
//
//            4 -> { // KAKAO
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.kapiKakaoComRequestApiMbr.getV2UserMe(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.code() != 200 ||
//                    response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return null
//                }
//
//                val isDatabase1MemberUserExists =
//                    database1MemberMemberSnsOauth2Repository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                        4,
//                        response.body()!!.id.toString(),
//                        true
//                    )
//                if (isDatabase1MemberUserExists) { // 기존 회원 존재
//                    httpServletResponse.setHeader("api-result-code", "1")
//                    return null
//                }
//
//                val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
//                RedisUtilObject.putValue<RegisterMembershipOAuth2KakaoVerification>(
//                    redis1RedisTemplate,
//                    response.body()!!.id.toString(),
//                    RegisterMembershipOAuth2KakaoVerification(
//                        verificationCode
//                    ),
//                    signUpOAuth2VerificationTimeSecMbr * 1000
//                )
//
//                val expireWhen = SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss.SSS"
//                ).format(Calendar.getInstance().apply {
//                    this.time = Date()
//                    this.add(Calendar.SECOND, signUpOAuth2VerificationTimeSecMbr.toInt())
//                }.time)
//
//                return C7TkAuthController.Api16OutputVo(
//                    verificationCode,
//                    response.body()!!.id.toString(),
//                    expireWhen
//                )
//            }
//
//            else -> {
//                throw Exception("SNS Login Type not supported")
//            }
//        }
//    }


    ////
    // todo
//    @ProwdTransactional([Database1DatasourceConfig.platformTransactionManagerBeanName])
//    fun api20(httpServletResponse: HttpServletResponse, inputVo: C7TkAuthController.Api20InputVo) {
//        // 기존 회원 확인
//        val isUserExists =
//            database1MemberMemberSnsOauth2Repository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                inputVo.oauth2TypeCode,
//                inputVo.oauth2Id,
//                true
//            )
//        if (isUserExists) { // 기존 회원이 있을 때
//            httpServletResponse.setHeader("api-result-code", "1")
//            return
//        }
//
//        // 본인 이메일 검증 여부 확인
//        val secret: String
//        when (inputVo.oauth2TypeCode) {
//            1 -> {
//                // GOOGLE
//                val verification = RedisUtilObject.getValue<RegisterMembershipOAuth2GoogleVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//
//                if (verification == null) { // 검증 요청을 하지 않거나 만료됨
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                secret = (verification.value as RegisterMembershipOAuth2GoogleVerification).secret
//            }
//
//            2 -> {
//                // APPLE
//                val verification = RedisUtilObject.getValue<RegisterMembershipOAuth2AppleVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//
//                if (verification == null) { // 검증 요청을 하지 않거나 만료됨
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                secret = (verification.value as RegisterMembershipOAuth2AppleVerification).secret
//            }
//
//            3 -> {
//                // NAVER
//                val verification = RedisUtilObject.getValue<RegisterMembershipOAuth2NaverVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//
//                if (verification == null) { // 검증 요청을 하지 않거나 만료됨
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                secret = (verification.value as RegisterMembershipOAuth2NaverVerification).secret
//            }
//
//            4 -> {
//                // KAKAO
//                val verification = RedisUtilObject.getValue<RegisterMembershipOAuth2KakaoVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//
//                if (verification == null) { // 검증 요청을 하지 않거나 만료됨
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                secret = (verification.value as RegisterMembershipOAuth2KakaoVerification).secret
//            }
//
//            else -> {
//                httpServletResponse.status = 400
//                return
//            }
//        }
//
//        // 입력 코드와 발급된 코드와의 매칭
//        val codeMatched = secret == inputVo.verificationCode
//
//        if (!codeMatched) { // 입력한 코드와 일치하지 않음 == 이메일 검증 요청을 하지 않거나 만료됨
//            httpServletResponse.setHeader("api-result-code", "4")
//            return
//        }
//
//        if (database1MemberMemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
//            httpServletResponse.setHeader("api-result-code", "3")
//            return
//        }
//
//        // 회원가입
//        val database1MemberUser: Database1_Member_Member = database1MemberMemberDataRepository.save(
//            Database1_Member_Member(
//                inputVo.nickName,
//                null,
//                true
//            )
//        )
//
//        // SNS OAUth2 저장
//        database1MemberMemberSnsOauth2Repository.save(
//            Database1_Member_MemberOauth2(
//                database1MemberUser.uid!!,
//                inputVo.oauth2TypeCode,
//                inputVo.oauth2Id,
//                true
//            )
//        )
//
//        // 역할 저장
//        val database1MemberUserRoleList = ArrayList<Database1_Member_MemberRole>()
//        // 기본 권한 추가
////        database1MemberUserRoleList.add(
////            Database1_Member_MemberRole(
////                database1MemberUser.uid!!,
////                2,
////                true
////            )
////        )
//        database1MemberMemberRoleRepository.saveAll(database1MemberUserRoleList)
//
//        // 확인 완료된 검증 요청 정보 삭제
//        when (inputVo.oauth2TypeCode) {
//            1 -> {
//                // GOOGLE
//                RedisUtilObject.deleteValue<RegisterMembershipOAuth2GoogleVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//            }
//
//            2 -> {
//                // APPLE
//                RedisUtilObject.deleteValue<RegisterMembershipOAuth2AppleVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//            }
//
//            3 -> {
//                // NAVER
//                RedisUtilObject.deleteValue<RegisterMembershipOAuth2NaverVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//            }
//
//            4 -> {
//                // KAKAO
//                RedisUtilObject.deleteValue<RegisterMembershipOAuth2KakaoVerification>(
//                    redis1RedisTemplate,
//                    inputVo.oauth2Id
//                )
//            }
//        }
//    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api21(
        httpServletResponse: HttpServletResponse,
        authorization: String,
        inputVo: C7TkAuthController.Api21InputVo
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(memberUid.toLong(), true)

        if (member == null) { // 멤버 정보가 없을 때
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (member.accountPassword == null) { // 기존 비번이 존재하지 않음
            if (inputVo.oldPassword != null) { // 비밀번호 불일치
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }
        } else { // 기존 비번 존재
            if (inputVo.oldPassword == null || !passwordEncoder.matches(
                    inputVo.oldPassword,
                    member.accountPassword
                )
            ) { // 비밀번호 불일치
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }
        }

        if (inputVo.newPassword == null) {
            val oAuth2EntityList =
                database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(
                    memberUid.toLong(),
                    true
                )

            if (oAuth2EntityList.isEmpty()) {
                // null 로 만들려고 할 때 account 외의 OAuth2 인증이 없다면 제거 불가
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            member.accountPassword = null
        } else {
            member.accountPassword = passwordEncoder.encode(inputVo.newPassword) // 비밀번호는 암호화
        }
        database1MemberMemberDataRepository.save(member)

        httpServletResponse.setHeader("api-result-code", "ok")
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_FindPasswordEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api22(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api22InputVo
    ): C7TkAuthController.Api22OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                inputVo.email,
                true
            )
        if (!isDatabase1MemberUserExists) { // 회원 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 이메일 발송
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        redis1FindPasswordEmailVerificationRepository.saveKeyValue(
            inputVo.email,
            Redis1_FindPasswordEmailVerification(verificationCode),
            findPwEmailVerificationTimeSecMbr * 1000
        )

        emailSenderUtilDi.sendThymeLeafHtmlMail(
            "Springboot Mvc Project Template",
            arrayOf(inputVo.email),
            null,
            "Springboot Mvc Project Template 비밀번호 찾기 - 본인 계정 확인용 이메일입니다.",
            "template_c7_n22/find_password_email_verification_email",
            hashMapOf(
                Pair("verificationCode", verificationCode)
            ),
            null,
            null,
            null,
            null
        )

        val expireWhen = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.SECOND, findPwEmailVerificationTimeSecMbr.toInt())
        }.time)

        return C7TkAuthController.Api22OutputVo(
            expireWhen
        )
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_FindPasswordEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api23(
        httpServletResponse: HttpServletResponse,
        email: String,
        verificationCode: String
    ): C7TkAuthController.Api23OutputVo? {
        val emailVerification = redis1FindPasswordEmailVerificationRepository.findKeyValue(email)

        if (emailVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.value.secret == verificationCode

        if (codeMatched) { // 코드 일치
            redis1FindPasswordEmailVerificationRepository.saveKeyValue(
                email,
                emailVerification.value,
                findPwEmailVerificationTimeUntilJoinSecMbr * 1000
            )

            val expireWhen = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS"
            ).format(Calendar.getInstance().apply {
                this.time = Date()
                this.add(Calendar.SECOND, findPwEmailVerificationTimeUntilJoinSecMbr.toInt())
            }.time)

            httpServletResponse.setHeader("api-result-code", "ok")
            return C7TkAuthController.Api23OutputVo(
                expireWhen
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    @CustomRedisTransactional(
        [
            Redis1_FindPasswordEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api24(httpServletResponse: HttpServletResponse, inputVo: C7TkAuthController.Api24InputVo) {
        // 코드 체크
        val emailVerification =
            redis1FindPasswordEmailVerificationRepository.findKeyValue(inputVo.email)

        if (emailVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        } else {
            // 입력 코드와 발급된 코드와의 매칭
            val codeMatched = emailVerification.value.secret == inputVo.verificationCode

            if (!codeMatched) { // 입력한 코드와 일치하지 않음 == 이메일 검증 요청을 하지 않거나 만료됨
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }

            // 입력 데이터 검증
            val memberEmail =
                database1MemberMemberEmailDataRepository.findByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )

            if (memberEmail == null) {
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
                memberEmail.memberUid,
                true
            )

            if (member == null) {
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            // 랜덤 비번 생성 후 세팅
            val newPassword = String.format("%09d", Random().nextInt(999999999)) // 랜덤 9자리 숫자
            member.accountPassword = passwordEncoder.encode(newPassword) // 비밀번호는 암호화
            database1MemberMemberDataRepository.save(member)

            // 생성된 비번 이메일 전송
            emailSenderUtilDi.sendThymeLeafHtmlMail(
                "Springboot Mvc Project Template",
                arrayOf(inputVo.email),
                null,
                "Springboot Mvc Project Template 새 비밀번호 발급",
                "template_c7_n24/find_password_new_password_email",
                hashMapOf(
                    Pair("newPassword", newPassword)
                ),
                null,
                null,
                null,
                null
            )

            // 확인 완료된 검증 요청 정보 삭제
            redis1FindPasswordEmailVerificationRepository.deleteKeyValue(inputVo.email)

            httpServletResponse.setHeader("api-result-code", "ok")
        }
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_FindPasswordPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api25(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api25InputVo
    ): C7TkAuthController.Api25OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                inputVo.phoneNumber,
                true
            )
        if (!isDatabase1MemberUserExists) { // 회원 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 문자 발송
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        redis1FindPasswordPhoneNumberVerificationRepository.saveKeyValue(
            inputVo.phoneNumber,
            Redis1_FindPasswordPhoneNumberVerification(verificationCode),
            findPwSmsVerificationTimeSecMbr * 1000
        )

        val phoneNumberSplit = inputVo.phoneNumber.split(")") // ["82", "010-0000-0000"]

        // 국가 코드 (ex : 82)
        val countryCode = phoneNumberSplit[0]

        // 전화번호 (ex : "01000000000")
        val phoneNumber = (phoneNumberSplit[1].replace("-", "")).replace(" ", "")

        NaverSmsUtilObject.sendSms(
            NaverSmsUtilObject.SendSmsInputVo(
                countryCode,
                phoneNumber,
                "[Springboot Mvc Project Template - 비밀번호 찾기] 인증번호 [${verificationCode}]"
            )
        )

        val expireWhen = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.SECOND, findPwSmsVerificationTimeSecMbr.toInt())
        }.time)

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api25OutputVo(
            expireWhen
        )
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_FindPasswordPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api26(
        httpServletResponse: HttpServletResponse,
        phoneNumber: String,
        verificationCode: String
    ): C7TkAuthController.Api26OutputVo? {
        val smsVerification =
            redis1FindPasswordPhoneNumberVerificationRepository.findKeyValue(phoneNumber)

        if (smsVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = smsVerification.value.secret == verificationCode

        if (codeMatched) { // 코드 일치
            redis1FindPasswordPhoneNumberVerificationRepository.saveKeyValue(
                phoneNumber,
                smsVerification.value,
                findPwSmsVerificationTimeUntilJoinSecMbr * 1000
            )
            val expireWhen = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS"
            ).format(Calendar.getInstance().apply {
                this.time = Date()
                this.add(Calendar.SECOND, findPwSmsVerificationTimeUntilJoinSecMbr.toInt())
            }.time)

            httpServletResponse.setHeader("api-result-code", "ok")
            return C7TkAuthController.Api26OutputVo(
                expireWhen
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    @CustomRedisTransactional(
        [
            Redis1_FindPasswordPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api27(httpServletResponse: HttpServletResponse, inputVo: C7TkAuthController.Api27InputVo) {
        // 코드 체크
        val smsVerification = redis1FindPasswordPhoneNumberVerificationRepository.findKeyValue(inputVo.phoneNumber)

        if (smsVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        } else {
            // 입력 코드와 발급된 코드와의 매칭
            val codeMatched = smsVerification.value.secret == inputVo.verificationCode

            if (!codeMatched) { // 입력한 코드와 일치하지 않음 == 검증 요청을 하지 않거나 만료됨
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }

            // 입력 데이터 검증
            val memberPhone =
                database1MemberMemberPhoneDataRepository.findByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )

            if (memberPhone == null) {
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
                memberPhone.memberUid,
                true
            )

            if (member == null) {
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            // 랜덤 비번 생성 후 세팅
            val newPassword = String.format("%09d", Random().nextInt(999999999)) // 랜덤 9자리 숫자
            member.accountPassword = passwordEncoder.encode(newPassword) // 비밀번호는 암호화
            database1MemberMemberDataRepository.save(member)

            val phoneNumberSplit = inputVo.phoneNumber.split(")") // ["82", "010-0000-0000"]

            // 국가 코드 (ex : 82)
            val countryCode = phoneNumberSplit[0]

            // 전화번호 (ex : "01000000000")
            val phoneNumber = (phoneNumberSplit[1].replace("-", "")).replace(" ", "")

            NaverSmsUtilObject.sendSms(
                NaverSmsUtilObject.SendSmsInputVo(
                    countryCode,
                    phoneNumber,
                    "[Springboot Mvc Project Template - 새 비밀번호] $newPassword"
                )
            )

            // 확인 완료된 검증 요청 정보 삭제
            redis1FindPasswordPhoneNumberVerificationRepository.deleteKeyValue(inputVo.phoneNumber)

            httpServletResponse.setHeader("api-result-code", "ok")
        }
    }


    ////
    fun api28(httpServletResponse: HttpServletResponse, authorization: String): C7TkAuthController.Api28OutputVo {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val emailEntityList =
            database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList =
            database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val myOAuth2List = ArrayList<C7TkAuthController.Api28OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C7TkAuthController.Api28OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api28OutputVo(
            emailList,
            phoneNumberList,
            myOAuth2List
        )
    }


    ////
    fun api29(httpServletResponse: HttpServletResponse, authorization: String): C7TkAuthController.Api29OutputVo {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val emailEntityList =
            database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api29OutputVo(
            emailList
        )
    }


    ////
    fun api30(httpServletResponse: HttpServletResponse, authorization: String): C7TkAuthController.Api30OutputVo {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val phoneEntityList =
            database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api30OutputVo(
            phoneNumberList
        )
    }


    ////
    fun api31(httpServletResponse: HttpServletResponse, authorization: String): C7TkAuthController.Api31OutputVo {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val oAuth2EntityList =
            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val myOAuth2List = ArrayList<C7TkAuthController.Api31OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C7TkAuthController.Api31OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api31OutputVo(
            myOAuth2List
        )
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_AddEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api32(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api32InputVo,
        authorization: String
    ): C7TkAuthController.Api32OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                inputVo.email,
                true
            )
        if (isDatabase1MemberUserExists) { // 이미 사용중인 이메일
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 이메일 발송
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        redis1AddEmailVerificationRepository.saveKeyValue(
            "${memberUid}_${inputVo.email}",
            Redis1_AddEmailVerification(
                verificationCode
            ),
            addEmailVerificationTimeSecMbr * 1000
        )

        emailSenderUtilDi.sendThymeLeafHtmlMail(
            "Springboot Mvc Project Template",
            arrayOf(inputVo.email),
            null,
            "Springboot Mvc Project Template 이메일 추가 - 본인 계정 확인용 이메일입니다.",
            "template_c7_n32/add_email_verification_email",
            hashMapOf(
                Pair("verificationCode", verificationCode)
            ),
            null,
            null,
            null,
            null
        )

        val expireWhen = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.SECOND, addEmailVerificationTimeSecMbr.toInt())
        }.time)

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api32OutputVo(
            expireWhen
        )
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_AddEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api33(
        httpServletResponse: HttpServletResponse,
        email: String,
        verificationCode: String,
        authorization: String
    ): C7TkAuthController.Api33OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val emailVerification = redis1AddEmailVerificationRepository.findKeyValue("${memberUid}_${email}")

        if (emailVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.value.secret == verificationCode

        if (codeMatched) { // 코드 일치
            redis1AddEmailVerificationRepository.saveKeyValue(
                "${memberUid}_${email}",
                emailVerification.value,
                addEmailVerificationTimeUntilJoinSecMbr * 1000
            )
            val expireWhen = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS"
            ).format(Calendar.getInstance().apply {
                this.time = Date()
                this.add(Calendar.SECOND, addEmailVerificationTimeUntilJoinSecMbr.toInt())
            }.time)

            httpServletResponse.setHeader("api-result-code", "ok")
            return C7TkAuthController.Api33OutputVo(
                expireWhen
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    @CustomRedisTransactional(
        [
            Redis1_AddEmailVerification.TRANSACTION_NAME
        ]
    )
    fun api34(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api34InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // 코드 체크
        val emailVerification =
            redis1AddEmailVerificationRepository.findKeyValue("${memberUid}_${inputVo.email}")

        if (emailVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        } else {
            val isDatabase1MemberUserExists =
                database1MemberMemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )

            if (isDatabase1MemberUserExists) { // 이미 사용중인 이메일
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }

            // 입력 코드와 발급된 코드와의 매칭
            val codeMatched = emailVerification.value.secret == inputVo.verificationCode

            if (!codeMatched) { // 입력한 코드와 일치하지 않음 == 이메일 검증 요청을 하지 않거나 만료됨
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            // 이메일 추가
            database1MemberMemberEmailDataRepository.save(
                Database1_Member_MemberEmailData(
                    memberUid.toLong(),
                    inputVo.email,
                    true
                )
            )

            // 확인 완료된 검증 요청 정보 삭제
            redis1AddEmailVerificationRepository.deleteKeyValue("${memberUid}_${inputVo.email}")

            httpServletResponse.setHeader("api-result-code", "ok")
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api35(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api35InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val memberUidLong = memberUid.toLong()

        // 내 계정에 등록된 모든 이메일 리스트 가져오기
        val myEmailList = database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        if (myEmailList.isEmpty()) {
            // 이메일 리스트가 비어있다면
            httpServletResponse.setHeader("api-result-code", "ok")
            return
        }

        // 지우려는 이메일 정보 인덱스 찾기
        val selectedEmailIdx = myEmailList.indexOfFirst {
            it.emailAddress == inputVo.email
        }

        if (selectedEmailIdx == -1) {
            // 지우려는 이메일 정보가 없다면
            httpServletResponse.setHeader("api-result-code", "ok")
            return
        }

        val isOauth2Exists = database1MemberMemberOauth2LoginDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        // 지우려는 이메일 외에 로그인 방법이 존재하는지 확인
        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        )!!

        val isMemberPhoneExists = database1MemberMemberPhoneDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        if (isOauth2Exists || (member.accountPassword != null && (isMemberPhoneExists || myEmailList.size > 1))) {
            // 사용 가능한 계정 로그인 정보가 존재
            val selectedEmailEntity = myEmailList[selectedEmailIdx]

            // 이메일 지우기
            selectedEmailEntity.rowActivate = false
            database1MemberMemberEmailDataRepository.save(
                selectedEmailEntity
            )

            httpServletResponse.setHeader("api-result-code", "ok")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_AddPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api36(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api36InputVo,
        authorization: String
    ): C7TkAuthController.Api36OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                inputVo.phoneNumber,
                true
            )
        if (isDatabase1MemberUserExists) { // 이미 사용중인 전화번호
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 문자 발송
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        redis1AddPhoneNumberVerificationRepository.saveKeyValue(
            "${memberUid}_${inputVo.phoneNumber}",
            Redis1_AddPhoneNumberVerification(
                verificationCode
            ),
            addPhoneNumberVerificationTimeSecMbr * 1000
        )

        val phoneNumberSplit = inputVo.phoneNumber.split(")") // ["82", "010-0000-0000"]

        // 국가 코드 (ex : 82)
        val countryCode = phoneNumberSplit[0]

        // 전화번호 (ex : "01000000000")
        val phoneNumber = (phoneNumberSplit[1].replace("-", "")).replace(" ", "")

        NaverSmsUtilObject.sendSms(
            NaverSmsUtilObject.SendSmsInputVo(
                countryCode,
                phoneNumber,
                "[Springboot Mvc Project Template - 전화번호 추가] 인증번호 [${verificationCode}]"
            )
        )

        val expireWhen = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.SECOND, addPhoneNumberVerificationTimeSecMbr.toInt())
        }.time)

        httpServletResponse.setHeader("api-result-code", "ok")
        return C7TkAuthController.Api36OutputVo(
            expireWhen
        )
    }


    ////
    @CustomRedisTransactional(
        [
            Redis1_AddPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api37(
        httpServletResponse: HttpServletResponse,
        phoneNumber: String,
        verificationCode: String,
        authorization: String
    ): C7TkAuthController.Api37OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val smsVerification =
            redis1AddPhoneNumberVerificationRepository.findKeyValue("${memberUid}_${phoneNumber}")

        if (smsVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = smsVerification.value.secret == verificationCode

        if (codeMatched) { // 코드 일치
            redis1AddPhoneNumberVerificationRepository.saveKeyValue(
                "${memberUid}_${phoneNumber}",
                smsVerification.value,
                addPhoneNumberVerificationTimeUntilJoinSecMbr * 1000
            )
            val expireWhen = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS"
            ).format(Calendar.getInstance().apply {
                this.time = Date()
                this.add(Calendar.SECOND, addPhoneNumberVerificationTimeUntilJoinSecMbr.toInt())
            }.time)

            httpServletResponse.setHeader("api-result-code", "ok")
            return C7TkAuthController.Api37OutputVo(
                expireWhen
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    @CustomRedisTransactional(
        [
            Redis1_AddPhoneNumberVerification.TRANSACTION_NAME
        ]
    )
    fun api38(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api38InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // 코드 체크
        val smsVerification =
            redis1AddPhoneNumberVerificationRepository.findKeyValue("${memberUid}_${inputVo.phoneNumber}")

        if (smsVerification == null) { // 해당 이메일 검증이 만료되거나 요청한적 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        } else {
            val isDatabase1MemberUserExists =
                database1MemberMemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )

            if (isDatabase1MemberUserExists) { // 이미 사용중인 전화번호
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }

            // 입력 코드와 발급된 코드와의 매칭
            val codeMatched = smsVerification.value.secret == inputVo.verificationCode

            if (!codeMatched) { // 입력한 코드와 일치하지 않음 == 이메일 검증 요청을 하지 않거나 만료됨
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            // 이메일 추가
            database1MemberMemberPhoneDataRepository.save(
                Database1_Member_MemberPhoneData(
                    memberUid.toLong(),
                    inputVo.phoneNumber,
                    true
                )
            )

            // 확인 완료된 검증 요청 정보 삭제
            redis1AddPhoneNumberVerificationRepository.deleteKeyValue("${memberUid}_${inputVo.phoneNumber}")
            httpServletResponse.setHeader("api-result-code", "ok")
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api39(
        httpServletResponse: HttpServletResponse,
        inputVo: C7TkAuthController.Api39InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val memberUidLong = memberUid.toLong()

        // 내 계정에 등록된 모든 전화번호 리스트 가져오기
        val myPhoneList = database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        if (myPhoneList.isEmpty()) {
            // 전화번호 리스트가 비어있다면
            return
        }

        // 지우려는 전화번호 정보 인덱스 찾기
        val selectedPhoneIdx = myPhoneList.indexOfFirst {
            it.phoneNumber == inputVo.phoneNumber
        }

        if (selectedPhoneIdx == -1) {
            // 지우려는 전화번호 정보가 없다면
            return
        }

        val selectedPhoneEntity = myPhoneList[selectedPhoneIdx]

        val isOauth2Exists = database1MemberMemberOauth2LoginDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        // 지우려는 전화번호 외에 로그인 방법이 존재하는지 확인
        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        )!!

        val isMemberEmailExists = database1MemberMemberEmailDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        if (isOauth2Exists || (member.accountPassword != null && (isMemberEmailExists || myPhoneList.size > 1))) {
            // 사용 가능한 계정 로그인 정보가 존재

            // 전화번호 지우기
            selectedPhoneEntity.rowActivate = false
            database1MemberMemberPhoneDataRepository.save(
                selectedPhoneEntity
            )

            httpServletResponse.setHeader("api-result-code", "ok")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    // todo
//    fun api40(
//        httpServletResponse: HttpServletResponse,
//        inputVo: C7TkAuthController.Api40InputVo,
//        authorization: String
//    ) {
//        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
//
//        val snsTypeCode: Int
//        val snsId: String
//
//        // (정보 검증 로직 수행)
//        when (inputVo.oauth2TypeCode) {
//            1 -> { // GOOGLE
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.googleApisComRequestApiMbr.getOauth2V1Userinfo(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.code() != 200 ||
//                    response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                snsTypeCode = 1
//                snsId = response.body()!!.id
//            }
//
//            2 -> { // APPLE
//                // (아래는 Id 토큰으로 검증)
//                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2Secret)
//
//                val loginId: String
//                if (appleInfo != null) {
//                    loginId = appleInfo.snsId
//                } else {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                snsTypeCode = 2
//                snsId = loginId
//            }
//
//            3 -> { // NAVER
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.openapiNaverComRequestApiMbr.getV1NidMe(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                snsTypeCode = 3
//                snsId = response.body()!!.response.id
//            }
//
//            4 -> { // KAKAO
//                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
//                val response = networkRetrofit2Mbr.kapiKakaoComRequestApiMbr.getV2UserMe(
//                    "Bearer ${inputVo.oauth2Secret}"
//                ).execute()
//
//                // 액세트 토큰 정상 동작 확인
//                if (response.code() != 200 ||
//                    response.body() == null
//                ) {
//                    httpServletResponse.setHeader("api-result-code", "2")
//                    return
//                }
//
//                snsTypeCode = 4
//                snsId = response.body()!!.id.toString()
//            }
//
//            else -> {
//                throw Exception("SNS Login Type not supported")
//            }
//        }
//
//        // 검증됨
//        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
//            memberUid.toLong(),
//            true
//        )
//
//        if (member == null) {
//            httpServletResponse.setHeader("api-result-code", "1")
//            return
//        }
//
//        // 사용중인지 아닌지 검증
//        val isDatabase1MemberUserExists =
//            database1MemberMemberSnsOauth2Repository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                snsTypeCode,
//                snsId,
//                true
//            )
//
//        if (isDatabase1MemberUserExists) { // 이미 사용중인 SNS 인증
//            httpServletResponse.setHeader("api-result-code", "3")
//            return
//        }
//
//        // SNS 인증 추가
//        database1MemberMemberSnsOauth2Repository.save(
//            Database1_Member_MemberOauth2(
//                memberUid.toLong(),
//                snsTypeCode,
//                snsId,
//                true
//            )
//        )
//    }


    ////
    // todo
//    fun api41(
//        httpServletResponse: HttpServletResponse,
//        inputVo: C7TkAuthController.Api41InputVo,
//        authorization: String
//    ) {
//        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
//        val memberUidLong = memberUid.toLong()
//
//        // 내 계정에 등록된 모든 인증 리스트 가져오기
//        val myOAuth2List = database1MemberMemberSnsOauth2Repository.findAllByMemberUidAndRowActivate(
//            memberUidLong,
//            true
//        )
//
//        if (myOAuth2List.isEmpty()) {
//            // 리스트가 비어있다면
//            return
//        }
//
//        // 지우려는 정보 인덱스 찾기
//        val selectedOAuth2Idx = myOAuth2List.indexOfFirst {
//            it.oauth2TypeCode == inputVo.oauth2Type && it.oauth2Id == inputVo.oauth2Id
//        }
//
//        if (selectedOAuth2Idx == -1) {
//            // 지우려는 정보가 없다면
//            return
//        }
//
//        val selectedOAuth2Entity = myOAuth2List[selectedOAuth2Idx]
//
//        val isMemberEmailExists = database1MemberMemberEmailDataRepository.existsByMemberUidAndRowActivate(
//            memberUidLong,
//            true
//        )
//
//        val isMemberPhoneExists = database1MemberMemberPhoneDataRepository.existsByMemberUidAndRowActivate(
//            memberUidLong,
//            true
//        )
//
//        // 지우려는 정보 외에 로그인 방법이 존재하는지 확인
//        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
//            memberUidLong,
//            true
//        )!!
//
//        if (myOAuth2List.size > 1 || (member.accountPassword != null && (isMemberEmailExists || isMemberPhoneExists))) {
//            // 사용 가능한 계정 로그인 정보가 존재
//
//            // 로그인 정보 지우기
//            selectedOAuth2Entity.rowActivate = false
//            database1MemberMemberSnsOauth2Repository.save(
//                selectedOAuth2Entity
//            )
//
//            return
//        }
//
//        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
//        httpServletResponse.setHeader("api-result-code", "1")
//        return
//    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    @CustomRedisTransactional(
        [
            Redis1_SignInAccessTokenInfo.TRANSACTION_NAME,
            Redis1_RefreshTokenInfo.TRANSACTION_NAME
        ]
    )
    fun api42(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val memberUidLong = memberUid.toLong()

        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        ) ?: return  // 가입된 회원이 없음 = 회원탈퇴한 것으로 처리

        // 회원탈퇴 처리
        member.rowActivate = false
        database1MemberMemberDataRepository.save(
            member
        )

        // member_phone, member_email, member_role, member_sns_oauth2 비활성화
        val emailList = database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (email in emailList) {
            email.rowActivate = false
            database1MemberMemberEmailDataRepository.save(email)
        }

        val memberRoleList =
            database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (memberRole in memberRoleList) {
            memberRole.rowActivate = false
            database1MemberMemberRoleDataRepository.save(memberRole)
        }

        val memberSnsOauth2List =
            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (memberSnsOauth2 in memberSnsOauth2List) {
            memberSnsOauth2.rowActivate = false
            database1MemberMemberOauth2LoginDataRepository.save(memberSnsOauth2)
        }

        val memberPhoneList =
            database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (memberPhone in memberPhoneList) {
            memberPhone.rowActivate = false
            database1MemberMemberPhoneDataRepository.save(memberPhone)
        }


        // !!!회원과 관계된 처리!!

        // loginAccessToken 의 Iterable 가져오기
        val loginAccessTokenIterable = redis1SignInAccessTokenInfoRepository.findAllKeyValues()

        // 발행되었던 모든 액세스 토큰 무효화 (다른 디바이스에선 사용중 로그아웃된 것과 동일한 효과)
        for (loginAccessToken in loginAccessTokenIterable) {
            if (loginAccessToken.value.memberUid == memberUid) {
                // DB 내 해당 멤버의 리프레시 토큰 정보 삭제
                redis1RefreshTokenInfoRepository.deleteKeyValue(loginAccessToken.key)

                // 로그인 가능 액세스 토큰 정보 삭제
                redis1SignInAccessTokenInfoRepository.deleteKeyValue(loginAccessToken.key)
            }
        }
    }
}