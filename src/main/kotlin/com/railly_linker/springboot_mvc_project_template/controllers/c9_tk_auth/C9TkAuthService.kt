package com.railly_linker.springboot_mvc_project_template.controllers.c9_tk_auth

import com.railly_linker.springboot_mvc_project_template.annotations.CustomTransactional
import com.railly_linker.springboot_mvc_project_template.configurations.database_configs.Database1Config
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories.*
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.*
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.util_dis.EmailSenderUtilDi
import com.railly_linker.springboot_mvc_project_template.util_objects.AppleOAuthHelperUtilObject
import com.railly_linker.springboot_mvc_project_template.util_objects.AuthorizationTokenUtilObject
import com.railly_linker.springboot_mvc_project_template.util_objects.JwtTokenUtilObject
import com.railly_linker.springboot_mvc_project_template.util_objects.NaverSmsUtilObject
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class C9TkAuthService(
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
    private val database1MemberRegisterEmailVerificationDataRepository: Database1_Verification_RegisterEmailVerificationDataRepository,
    private val database1MemberFindPasswordEmailVerificationDataRepository: Database1_Verification_FindPasswordEmailVerificationDataRepository,
    private val database1MemberAddEmailVerificationDataRepository: Database1_Verification_AddEmailVerificationDataRepository,
    private val database1MemberRegisterPhoneNumberVerificationDataRepository: Database1_Verification_RegisterPhoneNumberVerificationDataRepository,
    private val database1MemberFindPasswordPhoneNumberVerificationDataRepository: Database1_Verification_FindPasswordPhoneNumberVerificationDataRepository,
    private val database1MemberAddPhoneNumberVerificationDataRepository: Database1_Verification_AddPhoneNumberVerificationDataRepository,
    private val database1MemberRegisterOauth2VerificationDataRepository: Database1_Verification_RegisterOauth2VerificationDataRepository,
    private val database1MemberMemberProfileDataRepository: Database1_Member_MemberProfileDataRepository,
    private val database1MemberSignInTokenInfoRepository: Database1_Member_SignInTokenInfoRepository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // Retrofit2 요청 객체
    val networkRetrofit2: RepositoryNetworkRetrofit2 = RepositoryNetworkRetrofit2.getInstance()

    // (현 프로젝트 동작 서버의 외부 접속 주소)
    // 프로필 이미지 로컬 저장 및 다운로드 주소 지정을 위해 필요
    // !!!프로필별 접속 주소 설정하기!!
    // ex : http://127.0.0.1:8080
    private val externalAccessAddress: String
        get() {
            return when (activeProfile) {
                "prod80" -> {
                    "http://127.0.0.1"
                }

                "dev8080" -> {
                    "http://127.0.0.1:8080"
                }

                else -> {
                    "http://127.0.0.1:8080"
                }
            }
        }


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(httpServletResponse: HttpServletResponse): Map<String, Any>? {
        val result: MutableMap<String, Any> = HashMap()
        result["result"] = externalAccessAddress

        httpServletResponse.setHeader("api-result-code", "0")
        return result
    }


    ////
    fun api2(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization).toLong()

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"

        httpServletResponse.setHeader("api-result-code", "0")
        return result

    }

    ////
    fun api3(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization).toLong()

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"

        httpServletResponse.setHeader("api-result-code", "0")
        return result

    }

    ////
    fun api4(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization).toLong()

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"

        httpServletResponse.setHeader("api-result-code", "0")
        return result

    }

    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api5InputVo
    ): C9TkAuthController.Api5OutputVo? {
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
                classLogger.info("signInType ${inputVo.signInTypeCode} Not Supported")
                httpServletResponse.status = 400
                return null
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

        // 멤버의 권한 리스트를 조회 후 반환
        val memberRoleList = database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(
            memberUid,
            true
        )

        val roleList: ArrayList<String> = arrayListOf()
        for (userRole in memberRoleList) {
            roleList.add(userRole.role)
        }

        // (토큰 생성 로직 수행)
        val memberUidString: String = memberUid.toString()

        // 멤버 고유번호로 엑세스 토큰 생성
        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(memberUidString, roleList)

        val accessTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
        }.time)

        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(memberUidString)

        val refreshTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt())
        }.time)

        database1MemberSignInTokenInfoRepository.save(
            Database1_Member_SignInTokenInfo(
                memberUid,
                "Bearer",
                LocalDateTime.now(),
                jwtAccessToken,
                LocalDateTime
                    .parse(
                        accessTokenExpireWhen,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    ),
                jwtRefreshToken,
                LocalDateTime
                    .parse(
                        refreshTokenExpireWhen,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    ),
                true
            )
        )

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
        val myOAuth2List = ArrayList<C9TkAuthController.Api5OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9TkAuthController.Api5OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        val profileData = database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            memberUid,
            true
        )

        var frontProfileFullUrl: String? = null
        for (profile in profileData) {
            if (profile.isSelected) {
                frontProfileFullUrl = profile.imageFullUrl
                break
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api5OutputVo(
            memberUidString,
            member.nickName,
            frontProfileFullUrl,
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
    fun api6(
        httpServletResponse: HttpServletResponse,
        oauth2TypeCode: Int,
        oauth2Code: String
    ): C9TkAuthController.Api6OutputVo? {
        val snsAccessTokenType: String
        val snsAccessToken: String

        // !!!OAuth2 ClientId!!
        val clientId = "TODO"

        // !!!OAuth2 clientSecret!!
        val clientSecret = "TODO"

        // !!!OAuth2 로그인할때 사용한 Redirect Uri!!
        val redirectUri = "TODO"

        // (정보 검증 로직 수행)
        when (oauth2TypeCode) {
            1 -> { // GOOGLE
                // Access Token 가져오기
                val atResponse = networkRetrofit2.accountsGoogleComRequestApi.postOOauth2Token(
                    oauth2Code,
                    clientId,
                    clientSecret,
                    "authorization_code",
                    redirectUri
                ).execute()

                // code 사용 결과 검증
                if (atResponse.code() != 200 ||
                    atResponse.body() == null ||
                    atResponse.body()!!.accessToken == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsAccessTokenType = atResponse.body()!!.tokenType!!
                snsAccessToken = atResponse.body()!!.accessToken!!
            }

            2 -> { // NAVER
                // !!!OAuth2 로그인시 사용한 State!!
                val state = "TODO"

                // Access Token 가져오기
                val atResponse = networkRetrofit2.nidNaverComRequestApi.getOAuth2Dot0Token(
                    "authorization_code",
                    clientId,
                    clientSecret,
                    redirectUri,
                    oauth2Code,
                    state
                ).execute()

                // code 사용 결과 검증
                if (atResponse.code() != 200 ||
                    atResponse.body() == null ||
                    atResponse.body()!!.accessToken == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsAccessTokenType = atResponse.body()!!.tokenType!!
                snsAccessToken = atResponse.body()!!.accessToken!!
            }

            3 -> { // KAKAO
                // Access Token 가져오기
                val atResponse = networkRetrofit2.kauthKakaoComRequestApi.postOOauthToken(
                    "authorization_code",
                    clientId,
                    clientSecret,
                    redirectUri,
                    oauth2Code
                ).execute()

                // code 사용 결과 검증
                if (atResponse.code() != 200 ||
                    atResponse.body() == null ||
                    atResponse.body()!!.accessToken == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsAccessTokenType = atResponse.body()!!.tokenType!!
                snsAccessToken = atResponse.body()!!.accessToken!!
            }

            else -> {
                classLogger.info("SNS Login Type $oauth2TypeCode Not Supported")
                httpServletResponse.status = 400
                return null
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api6OutputVo(
            snsAccessTokenType,
            snsAccessToken
        )
    }


    ////
    fun api7(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api7InputVo
    ): C9TkAuthController.Api7OutputVo? {
        val snsOauth2: Database1_Member_MemberOauth2LoginData?

        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            1 -> { // GOOGLE
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.wwwGoogleapisComRequestApi.getOauth2V1UserInfo(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        1,
                        response.body()!!.id!!,
                        true
                    )
            }

            2 -> { // NAVER
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.openapiNaverComRequestApi.getV1NidMe(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        2,
                        response.body()!!.response.id,
                        true
                    )
            }

            3 -> { // KAKAO
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.kapiKakaoComRequestApi.getV2UserMe(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        3,
                        response.body()!!.id.toString(),
                        true
                    )
            }

            else -> {
                classLogger.info("SNS Login Type ${inputVo.oauth2TypeCode} Not Supported")
                httpServletResponse.status = 400
                return null
            }
        }

        if (snsOauth2 == null) { // 가입된 회원이 없음
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        if (member == null) { // 가입된 회원이 없음
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 멤버의 권한 리스트를 조회 후 반환
        val memberRoleList = database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        val roleList: ArrayList<String> = arrayListOf()
        for (userRole in memberRoleList) {
            roleList.add(userRole.role)
        }

        // (토큰 생성 로직 수행)
        // 멤버 고유번호로 엑세스 토큰 생성
        val memberUidString: String = snsOauth2.memberUid.toString()

        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(memberUidString, roleList)

        val accessTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
        }.time)

        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(memberUidString)

        val refreshTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt())
        }.time)

        database1MemberSignInTokenInfoRepository.save(
            Database1_Member_SignInTokenInfo(
                snsOauth2.memberUid,
                "Bearer",
                LocalDateTime.now(),
                jwtAccessToken,
                LocalDateTime
                    .parse(
                        accessTokenExpireWhen,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    ),
                jwtRefreshToken,
                LocalDateTime
                    .parse(
                        refreshTokenExpireWhen,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    ),
                true
            )
        )

        val emailEntityList =
            database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList =
            database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val myOAuth2List = ArrayList<C9TkAuthController.Api7OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9TkAuthController.Api7OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        val profileData = database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        var frontProfileFullUrl: String? = null
        for (profile in profileData) {
            if (profile.isSelected) {
                frontProfileFullUrl = profile.imageFullUrl
                break
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api7OutputVo(
            memberUidString,
            member.nickName,
            frontProfileFullUrl,
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
    fun api7Dot1(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api7Dot1InputVo
    ): C9TkAuthController.Api7Dot1OutputVo? {
        val snsOauth2: Database1_Member_MemberOauth2LoginData?

        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            4 -> { // APPLE
                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2IdToken)

                val loginId: String
                if (appleInfo != null) {
                    loginId = appleInfo.snsId
                } else {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1MemberMemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        4,
                        loginId,
                        true
                    )
            }

            else -> {
                classLogger.info("SNS Login Type ${inputVo.oauth2TypeCode} Not Supported")
                httpServletResponse.status = 400
                return null
            }
        }

        if (snsOauth2 == null) { // 가입된 회원이 없음
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        if (member == null) { // 가입된 회원이 없음
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 멤버의 권한 리스트를 조회 후 반환
        val memberRoleList = database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        val roleList: ArrayList<String> = arrayListOf()
        for (userRole in memberRoleList) {
            roleList.add(userRole.role)
        }

        // (토큰 생성 로직 수행)
        // 멤버 고유번호로 엑세스 토큰 생성
        val memberUidString: String = snsOauth2.memberUid.toString()

        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(memberUidString, roleList)

        val accessTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
        }.time)

        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(memberUidString)

        val refreshTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(Calendar.MILLISECOND, JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt())
        }.time)

        database1MemberSignInTokenInfoRepository.save(
            Database1_Member_SignInTokenInfo(
                snsOauth2.memberUid,
                "Bearer",
                LocalDateTime.now(),
                jwtAccessToken,
                LocalDateTime
                    .parse(
                        accessTokenExpireWhen,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    ),
                jwtRefreshToken,
                LocalDateTime
                    .parse(
                        refreshTokenExpireWhen,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    ),
                true
            )
        )

        val emailEntityList =
            database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList =
            database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val myOAuth2List = ArrayList<C9TkAuthController.Api7Dot1OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9TkAuthController.Api7Dot1OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        val profileData = database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        var frontProfileFullUrl: String? = null
        for (profile in profileData) {
            if (profile.isSelected) {
                frontProfileFullUrl = profile.imageFullUrl
                break
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api7Dot1OutputVo(
            memberUidString,
            member.nickName,
            frontProfileFullUrl,
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
    // 주의점 : 클라이언트 입장에선 강제종료 등의 이유로 항상 로그인과 로그아웃이 쌍을 이루는 것은 아니기에 이점을 유의
    fun api8(authorization: String, httpServletResponse: HttpServletResponse) {
        // 해당 멤버의 토큰 발행 정보 삭제
        val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
        val tokenType = authorizationSplit[0].trim().lowercase() // (ex : "bearer")
        val token = authorizationSplit[1].trim() // (ex : "abcd1234")

        val tokenInfo = database1MemberSignInTokenInfoRepository.findByTokenTypeAndAccessTokenAndRowActivate(
            tokenType,
            token,
            true
        )

        if (tokenInfo != null) {
            tokenInfo.rowActivate = false
            database1MemberSignInTokenInfoRepository.save(tokenInfo)
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api9(
        authorization: String,
        inputVo: C9TkAuthController.Api9InputVo,
        httpServletResponse: HttpServletResponse
    ): C9TkAuthController.Api9OutputVo? {
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
                    val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
                    val tokenType1 = authorizationSplit[0].trim().lowercase() // (ex : "bearer")
                    val token = authorizationSplit[1].trim() // (ex : "abcd1234")

                    val tokenInfo =
                        database1MemberSignInTokenInfoRepository.findByTokenTypeAndAccessTokenAndRowActivate(
                            tokenType1,
                            token,
                            true
                        )

                    if (JwtTokenUtilObject.getRemainSeconds(jwtRefreshToken) == 0L || // 만료시간 지남
                        tokenInfo == null // jwtAccessToken 의 리프레시 토큰이 저장소에 없음
                    ) {
                        httpServletResponse.setHeader("api-result-code", "3")
                        return null
                    }

                    if (jwtRefreshToken != tokenInfo.refreshToken) {
                        // 건내받은 토큰이 해당 액세스 토큰의 가용 토큰과 맞지 않음
                        httpServletResponse.setHeader("api-result-code", "4")
                        return null
                    }

                    // 먼저 로그아웃 처리
                    tokenInfo.rowActivate = false
                    database1MemberSignInTokenInfoRepository.save(tokenInfo)

                    // 멤버의 권한 리스트를 조회 후 반환
                    val memberRoleList = database1MemberMemberRoleDataRepository.findAllByMemberUidAndRowActivate(
                        accessTokenMemberUid.toLong(),
                        true
                    )

                    val roleList: ArrayList<String> = arrayListOf()
                    for (userRole in memberRoleList) {
                        roleList.add(userRole.role)
                    }

                    // 새 토큰 생성 및 로그인 처리
                    val newJwtAccessToken = JwtTokenUtilObject.generateAccessToken(accessTokenMemberUid, roleList)

                    val accessTokenExpireWhen: String = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                    ).format(Calendar.getInstance().apply {
                        this.time = Date()
                        this.add(Calendar.MILLISECOND, JwtTokenUtilObject.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt())
                    }.time)

                    val newRefreshToken = JwtTokenUtilObject.generateRefreshToken(accessTokenMemberUid)

                    val refreshTokenExpireWhen: String = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                    ).format(Calendar.getInstance().apply {
                        this.time = Date()
                        this.add(Calendar.MILLISECOND, JwtTokenUtilObject.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt())
                    }.time)

                    database1MemberSignInTokenInfoRepository.save(
                        Database1_Member_SignInTokenInfo(
                            accessTokenMemberUid.toLong(),
                            "Bearer",
                            LocalDateTime.now(),
                            newJwtAccessToken,
                            LocalDateTime
                                .parse(
                                    accessTokenExpireWhen,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                                ),
                            newRefreshToken,
                            LocalDateTime
                                .parse(
                                    refreshTokenExpireWhen,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                                ),
                            true
                        )
                    )

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
                    val myOAuth2List = ArrayList<C9TkAuthController.Api9OutputVo.OAuth2Info>()
                    for (oAuth2Entity in oAuth2EntityList) {
                        myOAuth2List.add(
                            C9TkAuthController.Api9OutputVo.OAuth2Info(
                                oAuth2Entity.oauth2TypeCode.toInt(),
                                oAuth2Entity.oauth2Id
                            )
                        )
                    }

                    val profileData = database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(
                        accessTokenMemberUid.toLong(),
                        true
                    )

                    var frontProfileFullUrl: String? = null
                    for (profile in profileData) {
                        if (profile.isSelected) {
                            frontProfileFullUrl = profile.imageFullUrl
                            break
                        }
                    }

                    httpServletResponse.setHeader("api-result-code", "0")
                    return C9TkAuthController.Api9OutputVo(
                        accessTokenMemberUid,
                        memberInfo.nickName,
                        frontProfileFullUrl,
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
    fun api10(authorization: String, httpServletResponse: HttpServletResponse) {
        val memberUid = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // loginAccessToken 의 Iterable 가져오기
        val tokenInfoList = database1MemberSignInTokenInfoRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        // 발행되었던 모든 액세스 토큰 무효화 (다른 디바이스에선 사용중 로그아웃된 것과 동일한 효과)
        for (tokenInfo in tokenInfoList) {
            tokenInfo.rowActivate = false

            database1MemberSignInTokenInfoRepository.save(tokenInfo)
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api11(httpServletResponse: HttpServletResponse, nickName: String): C9TkAuthController.Api11OutputVo? {
        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api11OutputVo(
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

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api13(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api13InputVo
    ): C9TkAuthController.Api13OutputVo? {
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
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberRegisterEmailVerificationData =
            database1MemberRegisterEmailVerificationDataRepository.save(
                Database1_Verification_RegisterEmailVerificationData(
                    inputVo.email,
                    verificationCode,
                    LocalDateTime.now().plusSeconds(verificationTimeSec),
                    true
                )
            )

        emailSenderUtilDi.sendThymeLeafHtmlMail(
            "Springboot Mvc Project Template",
            arrayOf(inputVo.email),
            null,
            "Springboot Mvc Project Template 회원가입 - 본인 계정 확인용 이메일입니다.",
            "template_c9_n13/email_verification_email",
            hashMapOf(
                Pair("verificationCode", verificationCode)
            ),
            null,
            null,
            null,
            null
        )

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api13OutputVo(
            database1MemberRegisterEmailVerificationData.uid!!,
            database1MemberRegisterEmailVerificationData.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api14(
        httpServletResponse: HttpServletResponse,
        verificationUid: Long,
        email: String,
        verificationCode: String
    ): C9TkAuthController.Api14OutputVo? {
        val emailVerificationOpt = database1MemberRegisterEmailVerificationDataRepository.findById(verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != email
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            emailVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1MemberRegisterEmailVerificationDataRepository.save(
                emailVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9TkAuthController.Api14OutputVo(
                emailVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api15(httpServletResponse: HttpServletResponse, inputVo: C9TkAuthController.Api15InputVo) {
        val emailVerificationOpt =
            database1MemberRegisterEmailVerificationDataRepository.findById(inputVo.verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != inputVo.email
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1MemberMemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            if (database1MemberMemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
                httpServletResponse.setHeader("api-result-code", "5")
                return
            }

            val password: String? = passwordEncoder.encode(inputVo.password) // 비밀번호 암호화

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
                    inputVo.email,
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

            if (inputVo.profileImageFile != null) {
                // 저장된 프로필 이미지 파일을 다운로드 할 수 있는 URL
                val savedProfileImageUrl: String

                // 프로필 이미지 파일 저장

                //----------------------------------------------------------------------------------------------------------
                // 프로필 이미지를 서버 스토리지에 저장할 때 사용하는 방식
                // 파일 저장 기본 디렉토리 경로
                val saveDirectoryPath: Path = Paths.get("./files/member/profile").toAbsolutePath().normalize()

                // 파일 저장 기본 디렉토리 생성
                Files.createDirectories(saveDirectoryPath)

                // 원본 파일명(with suffix)
                val multiPartFileNameString = StringUtils.cleanPath(inputVo.profileImageFile.originalFilename!!)

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
                inputVo.profileImageFile.transferTo(
                    // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                    saveDirectoryPath.resolve(savedFileName).normalize()
                )

                savedProfileImageUrl = "${externalAccessAddress}/tk/auth/member-profile/$savedFileName"
                //----------------------------------------------------------------------------------------------------------

                database1MemberMemberProfileDataRepository.save(
                    Database1_Member_MemberProfileData(
                        database1MemberUser.uid!!,
                        savedProfileImageUrl,
                        true,
                        rowActivate = true
                    )
                )
            }

            // 확인 완료된 검증 요청 정보 삭제
            emailVerification.rowActivate = false
            database1MemberRegisterEmailVerificationDataRepository.save(emailVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api16(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api16InputVo
    ): C9TkAuthController.Api16OutputVo? {
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

        // 정보 저장 후 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberRegisterPhoneNumberVerificationData =
            database1MemberRegisterPhoneNumberVerificationDataRepository.save(
                Database1_Verification_RegisterPhoneNumberVerificationData(
                    inputVo.phoneNumber,
                    verificationCode,
                    LocalDateTime.now().plusSeconds(verificationTimeSec),
                    true
                )
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
                "[Springboot Mvc Project Template - 회원가입] 인증번호 [${verificationCode}]"
            )
        )

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api16OutputVo(
            database1MemberRegisterPhoneNumberVerificationData.uid!!,
            database1MemberRegisterPhoneNumberVerificationData.verificationExpireWhen.format(
                DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss.SSS"
                )
            )
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api17(
        httpServletResponse: HttpServletResponse,
        verificationUid: Long,
        phoneNumber: String,
        verificationCode: String
    ): C9TkAuthController.Api17OutputVo? {
        val phoneNumberVerificationOpt =
            database1MemberRegisterPhoneNumberVerificationDataRepository.findById(verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != phoneNumber
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            phoneNumberVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1MemberRegisterPhoneNumberVerificationDataRepository.save(
                phoneNumberVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9TkAuthController.Api17OutputVo(
                phoneNumberVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api18(httpServletResponse: HttpServletResponse, inputVo: C9TkAuthController.Api18InputVo) {
        val phoneNumberVerificationOpt =
            database1MemberRegisterPhoneNumberVerificationDataRepository.findById(inputVo.verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != inputVo.phoneNumber
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1MemberMemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            if (database1MemberMemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
                httpServletResponse.setHeader("api-result-code", "5")
                return
            }

            val password: String? = passwordEncoder.encode(inputVo.password) // 비밀번호 암호화

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
                    inputVo.phoneNumber,
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

            if (inputVo.profileImageFile != null) {
                // 저장된 프로필 이미지 파일을 다운로드 할 수 있는 URL
                val savedProfileImageUrl: String

                // 프로필 이미지 파일 저장

                //----------------------------------------------------------------------------------------------------------
                // 프로필 이미지를 서버 스토리지에 저장할 때 사용하는 방식
                // 파일 저장 기본 디렉토리 경로
                val saveDirectoryPath: Path = Paths.get("./files/member/profile").toAbsolutePath().normalize()

                // 파일 저장 기본 디렉토리 생성
                Files.createDirectories(saveDirectoryPath)

                // 원본 파일명(with suffix)
                val multiPartFileNameString = StringUtils.cleanPath(inputVo.profileImageFile.originalFilename!!)

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
                inputVo.profileImageFile.transferTo(
                    // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                    saveDirectoryPath.resolve(savedFileName).normalize()
                )

                savedProfileImageUrl = "${externalAccessAddress}/tk/auth/member-profile/$savedFileName"
                //----------------------------------------------------------------------------------------------------------

                database1MemberMemberProfileDataRepository.save(
                    Database1_Member_MemberProfileData(
                        database1MemberUser.uid!!,
                        savedProfileImageUrl,
                        true,
                        rowActivate = true
                    )
                )
            }

            // 확인 완료된 검증 요청 정보 삭제
            phoneNumberVerification.rowActivate = false
            database1MemberRegisterPhoneNumberVerificationDataRepository.save(phoneNumberVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api19(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api19InputVo
    ): C9TkAuthController.Api19OutputVo? {
        val verificationUid: Long
        val verificationCode: String
        val expireWhen: String
        val loginId: String

        val verificationTimeSec: Long = 60 * 10
        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            1 -> { // GOOGLE
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.wwwGoogleapisComRequestApi.getOauth2V1UserInfo(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                loginId = response.body()!!.id!!

                val isDatabase1MemberUserExists =
                    database1MemberMemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        1,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1MemberRegisterOauth2VerificationDataRepository.save(
                        Database1_Verification_RegisterOauth2VerificationData(
                            1,
                            loginId,
                            verificationCode,
                            LocalDateTime.now().plusSeconds(verificationTimeSec),
                            true
                        )
                    )

                verificationUid = database1MemberRegisterOauth2VerificationData.uid!!

                expireWhen = database1MemberRegisterOauth2VerificationData.verificationExpireWhen.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                )
            }

            2 -> { // NAVER
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.openapiNaverComRequestApi.getV1NidMe(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                loginId = response.body()!!.response.id

                val isDatabase1MemberUserExists =
                    database1MemberMemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        2,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1MemberRegisterOauth2VerificationDataRepository.save(
                        Database1_Verification_RegisterOauth2VerificationData(
                            2,
                            loginId,
                            verificationCode,
                            LocalDateTime.now().plusSeconds(verificationTimeSec),
                            true
                        )
                    )

                verificationUid = database1MemberRegisterOauth2VerificationData.uid!!

                expireWhen = database1MemberRegisterOauth2VerificationData.verificationExpireWhen.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                )
            }

            3 -> { // KAKAO TALK
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.kapiKakaoComRequestApi.getV2UserMe(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                loginId = response.body()!!.id.toString()

                val isDatabase1MemberUserExists =
                    database1MemberMemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        3,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1MemberRegisterOauth2VerificationDataRepository.save(
                        Database1_Verification_RegisterOauth2VerificationData(
                            3,
                            loginId,
                            verificationCode,
                            LocalDateTime.now().plusSeconds(verificationTimeSec),
                            true
                        )
                    )

                verificationUid = database1MemberRegisterOauth2VerificationData.uid!!

                expireWhen = database1MemberRegisterOauth2VerificationData.verificationExpireWhen.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                )
            }

            else -> {
                classLogger.info("SNS Login Type ${inputVo.oauth2TypeCode} Not Supported")
                httpServletResponse.status = 400
                return null
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api19OutputVo(
            verificationUid,
            verificationCode,
            loginId,
            expireWhen
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api19Dot1(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api19Dot1InputVo
    ): C9TkAuthController.Api19Dot1OutputVo? {
        val verificationUid: Long
        val verificationCode: String
        val expireWhen: String
        val loginId: String

        val verificationTimeSec: Long = 60 * 10
        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            4 -> { // Apple
                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2IdToken)

                if (appleInfo != null) {
                    loginId = appleInfo.snsId
                } else {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                val isDatabase1MemberUserExists =
                    database1MemberMemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        4,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1MemberRegisterOauth2VerificationDataRepository.save(
                        Database1_Verification_RegisterOauth2VerificationData(
                            4,
                            loginId,
                            verificationCode,
                            LocalDateTime.now().plusSeconds(verificationTimeSec),
                            true
                        )
                    )

                verificationUid = database1MemberRegisterOauth2VerificationData.uid!!

                expireWhen = database1MemberRegisterOauth2VerificationData.verificationExpireWhen.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                )
            }

            else -> {
                classLogger.info("SNS Login Type ${inputVo.oauth2TypeCode} Not Supported")
                httpServletResponse.status = 400
                return null
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api19Dot1OutputVo(
            verificationUid,
            verificationCode,
            loginId,
            expireWhen
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api20(httpServletResponse: HttpServletResponse, inputVo: C9TkAuthController.Api20InputVo) {
        // oauth2 종류 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)
        val oauth2TypeCode: Int

        when (inputVo.oauth2TypeCode) {
            1 -> {
                oauth2TypeCode = 1
            }

            2 -> {
                oauth2TypeCode = 2
            }

            3 -> {
                oauth2TypeCode = 3
            }

            4 -> {
                oauth2TypeCode = 4
            }

            else -> {
                httpServletResponse.status = 400
                return
            }
        }

        val oauth2VerificationOpt =
            database1MemberRegisterOauth2VerificationDataRepository.findById(inputVo.verificationUid)

        if (oauth2VerificationOpt.isEmpty) { // 해당 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val oauth2Verification = oauth2VerificationOpt.get()

        if (!oauth2Verification.rowActivate ||
            oauth2Verification.oauth2TypeCode != oauth2TypeCode.toByte() ||
            oauth2Verification.oauth2Id != inputVo.oauth2Id
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(oauth2Verification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = oauth2Verification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1MemberMemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                    inputVo.oauth2TypeCode.toByte(),
                    inputVo.oauth2Id,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            if (database1MemberMemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
                httpServletResponse.setHeader("api-result-code", "5")
                return
            }

            // 회원가입
            val database1MemberUser = database1MemberMemberDataRepository.save(
                Database1_Member_MemberData(
                    inputVo.nickName,
                    null,
                    true
                )
            )

            // SNS OAUth2 저장
            database1MemberMemberOauth2LoginDataRepository.save(
                Database1_Member_MemberOauth2LoginData(
                    database1MemberUser.uid!!,
                    inputVo.oauth2TypeCode.toByte(),
                    inputVo.oauth2Id,
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

            if (inputVo.profileImageFile != null) {
                // 저장된 프로필 이미지 파일을 다운로드 할 수 있는 URL
                val savedProfileImageUrl: String

                // 프로필 이미지 파일 저장

                //----------------------------------------------------------------------------------------------------------
                // 프로필 이미지를 서버 스토리지에 저장할 때 사용하는 방식
                // 파일 저장 기본 디렉토리 경로
                val saveDirectoryPath: Path = Paths.get("./files/member/profile").toAbsolutePath().normalize()

                // 파일 저장 기본 디렉토리 생성
                Files.createDirectories(saveDirectoryPath)

                // 원본 파일명(with suffix)
                val multiPartFileNameString = StringUtils.cleanPath(inputVo.profileImageFile.originalFilename!!)

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
                inputVo.profileImageFile.transferTo(
                    // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
                    saveDirectoryPath.resolve(savedFileName).normalize()
                )

                savedProfileImageUrl = "${externalAccessAddress}/tk/auth/member-profile/$savedFileName"
                //----------------------------------------------------------------------------------------------------------

                database1MemberMemberProfileDataRepository.save(
                    Database1_Member_MemberProfileData(
                        database1MemberUser.uid!!,
                        savedProfileImageUrl,
                        true,
                        rowActivate = true
                    )
                )
            }

            // 확인 완료된 검증 요청 정보 삭제
            oauth2Verification.rowActivate = false
            database1MemberRegisterOauth2VerificationDataRepository.save(oauth2Verification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api21(
        httpServletResponse: HttpServletResponse,
        authorization: String,
        inputVo: C9TkAuthController.Api21InputVo
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

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api22(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api22InputVo
    ): C9TkAuthController.Api22OutputVo? {
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
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberFindPasswordEmailVerificationData =
            database1MemberFindPasswordEmailVerificationDataRepository.save(
                Database1_Verification_FindPasswordEmailVerificationData(
                    inputVo.email,
                    verificationCode,
                    LocalDateTime.now().plusSeconds(verificationTimeSec),
                    true
                )
            )

        emailSenderUtilDi.sendThymeLeafHtmlMail(
            "Springboot Mvc Project Template",
            arrayOf(inputVo.email),
            null,
            "Springboot Mvc Project Template 비밀번호 찾기 - 본인 계정 확인용 이메일입니다.",
            "template_c9_n22/find_password_email_verification_email",
            hashMapOf(
                Pair("verificationCode", verificationCode)
            ),
            null,
            null,
            null,
            null
        )

        return C9TkAuthController.Api22OutputVo(
            database1MemberFindPasswordEmailVerificationData.uid!!,
            database1MemberFindPasswordEmailVerificationData.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api23(
        httpServletResponse: HttpServletResponse,
        verificationUid: Long,
        email: String,
        verificationCode: String
    ): C9TkAuthController.Api23OutputVo? {
        val emailVerificationOpt = database1MemberFindPasswordEmailVerificationDataRepository.findById(verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != email
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            emailVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1MemberFindPasswordEmailVerificationDataRepository.save(
                emailVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9TkAuthController.Api23OutputVo(
                emailVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api24(httpServletResponse: HttpServletResponse, inputVo: C9TkAuthController.Api24InputVo) {
        val emailVerificationOpt =
            database1MemberFindPasswordEmailVerificationDataRepository.findById(inputVo.verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != inputVo.email
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            // 입력 데이터 검증
            val memberEmail =
                database1MemberMemberEmailDataRepository.findByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )

            if (memberEmail == null) {
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
                memberEmail.memberUid,
                true
            )

            if (member == null) {
                httpServletResponse.setHeader("api-result-code", "4")
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
                "template_c9_n24/find_password_new_password_email",
                hashMapOf(
                    Pair("newPassword", newPassword)
                ),
                null,
                null,
                null,
                null
            )

            // 확인 완료된 검증 요청 정보 삭제
            emailVerification.rowActivate = false
            database1MemberFindPasswordEmailVerificationDataRepository.save(emailVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api25(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api25InputVo
    ): C9TkAuthController.Api25OutputVo? {
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

        // 정보 저장 후 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberFindPasswordPhoneNumberVerificationData =
            database1MemberFindPasswordPhoneNumberVerificationDataRepository.save(
                Database1_Verification_FindPasswordPhoneNumberVerificationData(
                    inputVo.phoneNumber,
                    verificationCode,
                    LocalDateTime.now().plusSeconds(verificationTimeSec),
                    true
                )
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

        return C9TkAuthController.Api25OutputVo(
            database1MemberFindPasswordPhoneNumberVerificationData.uid!!,
            database1MemberFindPasswordPhoneNumberVerificationData.verificationExpireWhen.format(
                DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss.SSS"
                )
            )
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api26(
        httpServletResponse: HttpServletResponse,
        verificationUid: Long,
        phoneNumber: String,
        verificationCode: String
    ): C9TkAuthController.Api26OutputVo? {
        val phoneNumberVerificationOpt =
            database1MemberFindPasswordPhoneNumberVerificationDataRepository.findById(verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != phoneNumber
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            phoneNumberVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1MemberFindPasswordPhoneNumberVerificationDataRepository.save(
                phoneNumberVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9TkAuthController.Api26OutputVo(
                phoneNumberVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api27(httpServletResponse: HttpServletResponse, inputVo: C9TkAuthController.Api27InputVo) {
        val phoneNumberVerificationOpt =
            database1MemberFindPasswordPhoneNumberVerificationDataRepository.findById(inputVo.verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != inputVo.phoneNumber
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            // 입력 데이터 검증
            val memberPhone =
                database1MemberMemberPhoneDataRepository.findByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )

            if (memberPhone == null) {
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
                memberPhone.memberUid,
                true
            )

            if (member == null) {
                httpServletResponse.setHeader("api-result-code", "4")
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
            phoneNumberVerification.rowActivate = false
            database1MemberFindPasswordPhoneNumberVerificationDataRepository.save(phoneNumberVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    fun api28(httpServletResponse: HttpServletResponse, authorization: String): C9TkAuthController.Api28OutputVo {
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
        val myOAuth2List = ArrayList<C9TkAuthController.Api28OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9TkAuthController.Api28OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api28OutputVo(
            emailList,
            phoneNumberList,
            myOAuth2List
        )
    }


    ////
    fun api29(httpServletResponse: HttpServletResponse, authorization: String): C9TkAuthController.Api29OutputVo {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val emailEntityList =
            database1MemberMemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api29OutputVo(
            emailList
        )
    }


    ////
    fun api30(httpServletResponse: HttpServletResponse, authorization: String): C9TkAuthController.Api30OutputVo {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val phoneEntityList =
            database1MemberMemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api30OutputVo(
            phoneNumberList
        )
    }


    ////
    fun api31(httpServletResponse: HttpServletResponse, authorization: String): C9TkAuthController.Api31OutputVo {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val oAuth2EntityList =
            database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val myOAuth2List = ArrayList<C9TkAuthController.Api31OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9TkAuthController.Api31OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api31OutputVo(
            myOAuth2List
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api32(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api32InputVo,
        authorization: String
    ): C9TkAuthController.Api32OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

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
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberRegisterEmailVerificationData = database1MemberAddEmailVerificationDataRepository.save(
            Database1_Verification_AddEmailVerificationData(
                memberUid.toLong(),
                inputVo.email,
                verificationCode,
                LocalDateTime.now().plusSeconds(verificationTimeSec),
                true
            )
        )

        emailSenderUtilDi.sendThymeLeafHtmlMail(
            "Springboot Mvc Project Template",
            arrayOf(inputVo.email),
            null,
            "Springboot Mvc Project Template 이메일 추가 - 본인 계정 확인용 이메일입니다.",
            "template_c9_n32/add_email_verification_email",
            hashMapOf(
                Pair("verificationCode", verificationCode)
            ),
            null,
            null,
            null,
            null
        )

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api32OutputVo(
            database1MemberRegisterEmailVerificationData.uid!!,
            database1MemberRegisterEmailVerificationData.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api33(
        httpServletResponse: HttpServletResponse,
        verificationUid: Long,
        email: String,
        verificationCode: String,
        authorization: String
    ): C9TkAuthController.Api33OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val emailVerificationOpt = database1MemberAddEmailVerificationDataRepository.findById(verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.memberUid != memberUid.toLong() ||
            emailVerification.emailAddress != email
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            emailVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1MemberAddEmailVerificationDataRepository.save(
                emailVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9TkAuthController.Api33OutputVo(
                emailVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api34(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api34InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val emailVerificationOpt =
            database1MemberAddEmailVerificationDataRepository.findById(inputVo.verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.memberUid != memberUid.toLong() ||
            emailVerification.emailAddress != inputVo.email
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1MemberMemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.setHeader("api-result-code", "4")
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
            emailVerification.rowActivate = false
            database1MemberAddEmailVerificationDataRepository.save(emailVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api35(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api35InputVo,
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
            httpServletResponse.setHeader("api-result-code", "0")
            return
        }

        // 지우려는 이메일 정보 인덱스 찾기
        val selectedEmailIdx = myEmailList.indexOfFirst {
            it.emailAddress == inputVo.email
        }

        if (selectedEmailIdx == -1) {
            // 지우려는 이메일 정보가 없다면
            httpServletResponse.setHeader("api-result-code", "0")
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

            httpServletResponse.setHeader("api-result-code", "0")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api36(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api36InputVo,
        authorization: String
    ): C9TkAuthController.Api36OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

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

        // 정보 저장 후 이메일 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberAddPhoneNumberVerificationData =
            database1MemberAddPhoneNumberVerificationDataRepository.save(
                Database1_Verification_AddPhoneNumberVerificationData(
                    memberUid.toLong(),
                    inputVo.phoneNumber,
                    verificationCode,
                    LocalDateTime.now().plusSeconds(verificationTimeSec),
                    true
                )
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

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api36OutputVo(
            database1MemberAddPhoneNumberVerificationData.uid!!,
            database1MemberAddPhoneNumberVerificationData.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api37(
        httpServletResponse: HttpServletResponse,
        verificationUid: Long,
        phoneNumber: String,
        verificationCode: String,
        authorization: String
    ): C9TkAuthController.Api37OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val phoneNumberVerificationOpt =
            database1MemberAddPhoneNumberVerificationDataRepository.findById(verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.memberUid != memberUid.toLong() ||
            phoneNumberVerification.phoneNumber != phoneNumber
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            phoneNumberVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1MemberAddPhoneNumberVerificationDataRepository.save(
                phoneNumberVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9TkAuthController.Api37OutputVo(
                phoneNumberVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api38(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api38InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val phoneNumberVerificationOpt =
            database1MemberAddPhoneNumberVerificationDataRepository.findById(inputVo.verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.memberUid != memberUid.toLong() ||
            phoneNumberVerification.phoneNumber != inputVo.phoneNumber
        ) {
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1MemberMemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.setHeader("api-result-code", "4")
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
            phoneNumberVerification.rowActivate = false
            database1MemberAddPhoneNumberVerificationDataRepository.save(phoneNumberVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api39(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api39InputVo,
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

            httpServletResponse.setHeader("api-result-code", "0")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api40(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api40InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val snsTypeCode: Int
        val snsId: String

        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            1 -> { // GOOGLE
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.wwwGoogleapisComRequestApi.getOauth2V1UserInfo(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return
                }

                snsTypeCode = 1
                snsId = response.body()!!.id!!
            }

            2 -> { // NAVER
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.openapiNaverComRequestApi.getV1NidMe(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return
                }

                snsTypeCode = 2
                snsId = response.body()!!.response.id
            }

            3 -> { // KAKAO TALK
                // 클라이언트에서 받은 access 토큰으로 멤버 정보 요청
                val response = networkRetrofit2.kapiKakaoComRequestApi.getV2UserMe(
                    inputVo.oauth2AccessToken
                ).execute()

                // 액세트 토큰 정상 동작 확인
                if (response.code() != 200 ||
                    response.body() == null
                ) {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return
                }

                snsTypeCode = 3
                snsId = response.body()!!.id.toString()
            }

            else -> {
                classLogger.info("SNS Login Type ${inputVo.oauth2TypeCode} Not Supported")
                httpServletResponse.status = 400
                return
            }
        }

        // 검증됨
        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        if (member == null) {
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 사용중인지 아닌지 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                snsTypeCode.toByte(),
                snsId,
                true
            )

        if (isDatabase1MemberUserExists) { // 이미 사용중인 SNS 인증
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }

        // SNS 인증 추가
        database1MemberMemberOauth2LoginDataRepository.save(
            Database1_Member_MemberOauth2LoginData(
                memberUid.toLong(),
                snsTypeCode.toByte(),
                snsId,
                true
            )
        )
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api40Dot1(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api40Dot1InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val snsTypeCode: Int
        val snsId: String

        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            4 -> { // Apple
                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2IdToken)

                if (appleInfo != null) {
                    snsId = appleInfo.snsId
                } else {
                    httpServletResponse.setHeader("api-result-code", "1")
                    return
                }

                snsTypeCode = 4
            }

            else -> {
                classLogger.info("SNS Login Type ${inputVo.oauth2TypeCode} Not Supported")
                httpServletResponse.status = 400
                return
            }
        }

        // 검증됨
        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        if (member == null) {
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 사용중인지 아닌지 검증
        val isDatabase1MemberUserExists =
            database1MemberMemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                snsTypeCode.toByte(),
                snsId,
                true
            )

        if (isDatabase1MemberUserExists) { // 이미 사용중인 SNS 인증
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }

        // SNS 인증 추가
        database1MemberMemberOauth2LoginDataRepository.save(
            Database1_Member_MemberOauth2LoginData(
                memberUid.toLong(),
                snsTypeCode.toByte(),
                snsId,
                true
            )
        )
        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api41(
        httpServletResponse: HttpServletResponse,
        inputVo: C9TkAuthController.Api41InputVo,
        authorization: String
    ) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)
        val memberUidLong = memberUid.toLong()

        // 내 계정에 등록된 모든 인증 리스트 가져오기
        val myOAuth2List = database1MemberMemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        if (myOAuth2List.isEmpty()) {
            // 리스트가 비어있다면
            return
        }

        // 지우려는 정보 인덱스 찾기
        val selectedOAuth2Idx = myOAuth2List.indexOfFirst {
            it.oauth2TypeCode == inputVo.oauth2Type.toByte() && it.oauth2Id == inputVo.oauth2Id
        }

        if (selectedOAuth2Idx == -1) {
            // 지우려는 정보가 없다면
            return
        }

        val selectedOAuth2Entity = myOAuth2List[selectedOAuth2Idx]

        val isMemberEmailExists = database1MemberMemberEmailDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        val isMemberPhoneExists = database1MemberMemberPhoneDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        // 지우려는 정보 외에 로그인 방법이 존재하는지 확인
        val member = database1MemberMemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        )!!

        if (myOAuth2List.size > 1 || (member.accountPassword != null && (isMemberEmailExists || isMemberPhoneExists))) {
            // 사용 가능한 계정 로그인 정보가 존재

            // 로그인 정보 지우기
            selectedOAuth2Entity.rowActivate = false
            database1MemberMemberOauth2LoginDataRepository.save(
                selectedOAuth2Entity
            )

            httpServletResponse.setHeader("api-result-code", "0")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
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

        // member_phone, member_email, member_role, member_sns_oauth2, member_profile 비활성화
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

        val profileData =
            database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (profile in profileData) {
            profile.rowActivate = false
            database1MemberMemberProfileDataRepository.save(profile)
        }


        // !!!회원과 관계된 처리!!

        // loginAccessToken 의 Iterable 가져오기
        val tokenInfoList = database1MemberSignInTokenInfoRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        // 발행되었던 모든 액세스 토큰 무효화 (다른 디바이스에선 사용중 로그아웃된 것과 동일한 효과)
        for (tokenInfo in tokenInfoList) {
            tokenInfo.rowActivate = false

            database1MemberSignInTokenInfoRepository.save(tokenInfo)
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api43(httpServletResponse: HttpServletResponse, authorization: String): C9TkAuthController.Api43OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val profileData = database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        val myProfileList: ArrayList<C9TkAuthController.Api43OutputVo.ProfileInfo> = ArrayList()
        for (profile in profileData) {
            myProfileList.add(
                C9TkAuthController.Api43OutputVo.ProfileInfo(
                    profile.uid!!,
                    profile.imageFullUrl,
                    profile.isSelected
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api43OutputVo(
            myProfileList
        )
    }


    ////
    fun api44(httpServletResponse: HttpServletResponse, authorization: String): C9TkAuthController.Api44OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        val profileData = database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        var myProfile: C9TkAuthController.Api44OutputVo.ProfileInfo? = null
        for (profile in profileData) {
            if (profile.isSelected) {
                myProfile = C9TkAuthController.Api44OutputVo.ProfileInfo(
                    profile.uid!!,
                    profile.imageFullUrl,
                    profile.isSelected
                )
                break
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9TkAuthController.Api44OutputVo(
            myProfile
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api45(httpServletResponse: HttpServletResponse, authorization: String, profileUid: Long) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // 내 프로필 리스트 가져오기
        val profileData = database1MemberMemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        if (profileData.isEmpty()) {
            // 내 프로필이 하나도 없을 때
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        // 기존에 선택된 프로필
        var frontProfile: Database1_Member_MemberProfileData? = null

        // 이번에 선택하려는 프로필
        var selectedProfile: Database1_Member_MemberProfileData? = null
        for (profile in profileData) {
            if (profile.isSelected) {
                frontProfile = profile
            }
            if (profileUid == profile.uid) {
                selectedProfile = profile
            }
        }

        if (selectedProfile == null) {
            // 이번에 선택하려는 프로필이 없을 때
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (frontProfile != null) {
            // 기존에 선택된 프로필 선택 해제
            frontProfile.isSelected = false
            database1MemberMemberProfileDataRepository.save(frontProfile)
        }

        // 이번에 선택하려는 프로필을 선택하기
        selectedProfile.isSelected = true
        database1MemberMemberProfileDataRepository.save(selectedProfile)

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api46(authorization: String, httpServletResponse: HttpServletResponse, profileUid: Long) {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // 프로필 가져오기
        val profileDataOpt = database1MemberMemberProfileDataRepository.findById(profileUid)

        if (profileDataOpt.isPresent) {
            // 프로필이 존재할 때
            val profileData = profileDataOpt.get()

            if (profileData.rowActivate &&
                profileData.memberUid == memberUid.toLong()
            ) {
                // 프로필이 활성 상태이고, 멤버 고유번호가 내 고유 번호와 같을 때
                // 프로필 비활성화
                profileData.rowActivate = false
                database1MemberMemberProfileDataRepository.save(profileData)
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api47(
        httpServletResponse: HttpServletResponse,
        authorization: String,
        inputVo: C9TkAuthController.Api47InputVo
    ): C9TkAuthController.Api47OutputVo? {
        val memberUid: String = AuthorizationTokenUtilObject.getTokenMemberUid(authorization)

        // 저장된 프로필 이미지 파일을 다운로드 할 수 있는 URL
        val savedProfileImageUrl: String

        // 프로필 이미지 파일 저장

        //----------------------------------------------------------------------------------------------------------
        // 프로필 이미지를 서버 스토리지에 저장할 때 사용하는 방식
        // 파일 저장 기본 디렉토리 경로
        val saveDirectoryPath: Path = Paths.get("./files/member/profile").toAbsolutePath().normalize()

        // 파일 저장 기본 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 원본 파일명(with suffix)
        val multiPartFileNameString = StringUtils.cleanPath(inputVo.profileImageFile.originalFilename!!)

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
        inputVo.profileImageFile.transferTo(
            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            saveDirectoryPath.resolve(savedFileName).normalize()
        )

        savedProfileImageUrl = "${externalAccessAddress}/tk/auth/member-profile/$savedFileName"
        //----------------------------------------------------------------------------------------------------------

        val profileData = database1MemberMemberProfileDataRepository.save(
            Database1_Member_MemberProfileData(
                memberUid.toLong(),
                savedProfileImageUrl,
                false,
                rowActivate = true
            )
        )

        httpServletResponse.setHeader("api-result-code", "0")

        return C9TkAuthController.Api47OutputVo(
            profileData.uid!!,
            profileData.imageFullUrl
        )
    }


    ////
    fun api48(httpServletResponse: HttpServletResponse, fileName: String): ResponseEntity<Resource>? {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명 (프로젝트 루트 경로에 있는 files/temp 폴더를 기준으로 함)
        val serverFilePathObject =
            Paths.get("$projectRootAbsolutePathString/files/member/profile/$fileName")

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
}