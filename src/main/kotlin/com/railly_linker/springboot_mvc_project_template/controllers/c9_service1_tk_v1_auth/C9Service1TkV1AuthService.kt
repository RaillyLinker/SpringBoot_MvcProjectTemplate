package com.railly_linker.springboot_mvc_project_template.controllers.c9_service1_tk_v1_auth

import com.railly_linker.springboot_mvc_project_template.annotations.CustomTransactional
import com.railly_linker.springboot_mvc_project_template.configurations.SecurityConfig
import com.railly_linker.springboot_mvc_project_template.configurations.database_configs.Database1Config
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.repositories.*
import com.railly_linker.springboot_mvc_project_template.data_sources.database_sources.database1.tables.*
import com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.RepositoryNetworkRetrofit2
import com.railly_linker.springboot_mvc_project_template.util_dis.EmailSenderUtilDi
import com.railly_linker.springboot_mvc_project_template.util_objects.AppleOAuthHelperUtilObject
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
class C9Service1TkV1AuthService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    private val passwordEncoder: PasswordEncoder,
    private val emailSenderUtilDi: EmailSenderUtilDi,

    // (Database1 Repository)
    private val database1Service1MemberDataRepository: Database1_Service1_MemberDataRepository,
    private val database1Service1MemberRoleDataRepository: Database1_Service1_MemberRoleDataRepository,
    private val database1Service1MemberEmailDataRepository: Database1_Service1_MemberEmailDataRepository,
    private val database1Service1MemberPhoneDataRepository: Database1_Service1_MemberPhoneDataRepository,
    private val database1Service1MemberOauth2LoginDataRepository: Database1_Service1_MemberOauth2LoginDataRepository,
    private val database1Service1JoinTheMembershipWithPhoneNumberVerificationDataRepository: Database1_Service1_JoinTheMembershipWithPhoneNumberVerificationDataRepository,
    private val database1Service1JoinTheMembershipWithEmailVerificationDataRepository: Database1_Service1_JoinTheMembershipWithEmailVerificationDataRepository,
    private val database1Service1JoinTheMembershipWithOauth2VerificationDataRepository: Database1_Service1_JoinTheMembershipWithOauth2VerificationDataRepository,
    private val database1Service1FindPasswordWithPhoneNumberVerificationDataRepository: Database1_Service1_FindPasswordWithPhoneNumberVerificationDataRepository,
    private val database1Service1FindPasswordWithEmailVerificationDataRepository: Database1_Service1_FindPasswordWithEmailVerificationDataRepository,
    private val database1Service1AddEmailVerificationDataRepository: Database1_Service1_AddEmailVerificationDataRepository,
    private val database1Service1AddPhoneNumberVerificationDataRepository: Database1_Service1_AddPhoneNumberVerificationDataRepository,
    private val database1Service1MemberProfileDataRepository: Database1_Service1_MemberProfileDataRepository,
    private val database1Service1LogInTokenInfoRepository: Database1_Service1_LogInTokenInfoRepository
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
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"

        httpServletResponse.setHeader("api-result-code", "0")
        return result

    }

    ////
    fun api3(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"

        httpServletResponse.setHeader("api-result-code", "0")
        return result

    }

    ////
    fun api4(httpServletResponse: HttpServletResponse, authorization: String): Map<String, Any>? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val result: MutableMap<String, Any> = HashMap()
        result["result"] = "Member No.$memberUid : Test Success"

        httpServletResponse.setHeader("api-result-code", "0")
        return result

    }

    ////
    fun api5(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api5InputVo
    ): C9Service1TkV1AuthController.Api5OutputVo? {
        val memberUid: Long
        when (inputVo.loginTypeCode) {
            0 -> { // 닉네임
                // (정보 검증 로직 수행)
                val member = database1Service1MemberDataRepository.findByNickNameAndRowActivate(
                    inputVo.id,
                    true
                )

                if (member == null) { // 가입된 회원이 없음
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }
                memberUid = member.uid!!
            }

            1 -> { // 이메일
                // (정보 검증 로직 수행)
                val memberEmail = database1Service1MemberEmailDataRepository.findByEmailAddressAndRowActivate(
                    inputVo.id,
                    true
                )

                if (memberEmail == null) { // 가입된 회원이 없음
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }
                memberUid = memberEmail.memberUid
            }

            2 -> { // 전화번호
                // (정보 검증 로직 수행)
                val memberPhone = database1Service1MemberPhoneDataRepository.findByPhoneNumberAndRowActivate(
                    inputVo.id,
                    true
                )

                if (memberPhone == null) { // 가입된 회원이 없음
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }
                memberUid = memberPhone.memberUid
            }

            else -> {
                classLogger.info("loginTypeCode ${inputVo.loginTypeCode} Not Supported")
                httpServletResponse.status = 400
                return null
            }
        }

        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            memberUid,
            true
        )

        if (member == null) { // 가입된 회원이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (member.accountPassword == null || // 페스워드는 아직 만들지 않음
            !passwordEncoder.matches(inputVo.password, member.accountPassword!!) // 패스워드 불일치
        ) {
            // 두 상황 모두 비밀번호 찾기를 하면 해결이 됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 멤버의 권한 리스트를 조회 후 반환
        val memberRoleList = database1Service1MemberRoleDataRepository.findAllByMemberUidAndRowActivate(
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
        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(
            memberUidString,
            roleList,
            SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
            SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
        )

        val accessTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(
                Calendar.MILLISECOND,
                SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt()
            )
        }.time)

        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(
            memberUidString,
            SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
            SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
        )

        val refreshTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(
                Calendar.MILLISECOND,
                SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt()
            )
        }.time)

        database1Service1LogInTokenInfoRepository.save(
            Database1_Service1_LogInTokenInfo(
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

        val emailEntityList =
            database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUid, true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList =
            database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUid, true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUid, true)
        val myOAuth2List = ArrayList<C9Service1TkV1AuthController.Api5OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9Service1TkV1AuthController.Api5OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        val profileData = database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(
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
        return C9Service1TkV1AuthController.Api5OutputVo(
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
            myOAuth2List,
            member.accountPassword == null
        )
    }


    ////
    fun api6(
        httpServletResponse: HttpServletResponse,
        oauth2TypeCode: Int,
        oauth2Code: String
    ): C9Service1TkV1AuthController.Api6OutputVo? {
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
                    httpServletResponse.status = 500
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
                    httpServletResponse.status = 500
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
                    httpServletResponse.status = 500
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
        return C9Service1TkV1AuthController.Api6OutputVo(
            snsAccessTokenType,
            snsAccessToken
        )
    }


    ////
    fun api7(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api7InputVo
    ): C9Service1TkV1AuthController.Api7OutputVo? {
        val snsOauth2: Database1_Service1_MemberOauth2LoginData?

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
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1Service1MemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
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
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1Service1MemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
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
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1Service1MemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
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
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        if (member == null) { // 가입된 회원이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 멤버의 권한 리스트를 조회 후 반환
        val memberRoleList = database1Service1MemberRoleDataRepository.findAllByMemberUidAndRowActivate(
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

        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(
            memberUidString, roleList,
            SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
            SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
        )

        val accessTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(
                Calendar.MILLISECOND,
                SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt()
            )
        }.time)

        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(
            memberUidString,
            SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
            SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
        )

        val refreshTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(
                Calendar.MILLISECOND,
                SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt()
            )
        }.time)

        database1Service1LogInTokenInfoRepository.save(
            Database1_Service1_LogInTokenInfo(
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
            database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList =
            database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val myOAuth2List = ArrayList<C9Service1TkV1AuthController.Api7OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9Service1TkV1AuthController.Api7OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        val profileData = database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(
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
        return C9Service1TkV1AuthController.Api7OutputVo(
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
            myOAuth2List,
            member.accountPassword == null
        )
    }


    ////
    fun api7Dot1(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api7Dot1InputVo
    ): C9Service1TkV1AuthController.Api7Dot1OutputVo? {
        val snsOauth2: Database1_Service1_MemberOauth2LoginData?

        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            4 -> { // APPLE
                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2IdToken)

                val loginId: String
                if (appleInfo != null) {
                    loginId = appleInfo.snsId
                } else {
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                snsOauth2 =
                    database1Service1MemberOauth2LoginDataRepository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
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
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            snsOauth2.memberUid,
            true
        )

        if (member == null) { // 가입된 회원이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 멤버의 권한 리스트를 조회 후 반환
        val memberRoleList = database1Service1MemberRoleDataRepository.findAllByMemberUidAndRowActivate(
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

        val jwtAccessToken = JwtTokenUtilObject.generateAccessToken(
            memberUidString, roleList,
            SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
            SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
        )

        val accessTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(
                Calendar.MILLISECOND,
                SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt()
            )
        }.time)

        // 액세스 토큰의 리프레시 토큰 생성 및 DB 저장 = 액세스 토큰에 대한 리프레시 토큰은 1개 혹은 0개
        val jwtRefreshToken = JwtTokenUtilObject.generateRefreshToken(
            memberUidString,
            SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
            SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
        )

        val refreshTokenExpireWhen: String = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS"
        ).format(Calendar.getInstance().apply {
            this.time = Date()
            this.add(
                Calendar.MILLISECOND,
                SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt()
            )
        }.time)

        database1Service1LogInTokenInfoRepository.save(
            Database1_Service1_LogInTokenInfo(
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
            database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList =
            database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(snsOauth2.memberUid, true)
        val myOAuth2List = ArrayList<C9Service1TkV1AuthController.Api7Dot1OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9Service1TkV1AuthController.Api7Dot1OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        val profileData = database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(
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
        return C9Service1TkV1AuthController.Api7Dot1OutputVo(
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
            myOAuth2List,
            member.accountPassword == null
        )
    }


    ////
    // 주의점 : 클라이언트 입장에선 강제종료 등의 이유로 항상 로그인과 로그아웃이 쌍을 이루는 것은 아니기에 이점을 유의
    fun api8(authorization: String, httpServletResponse: HttpServletResponse) {
        // 해당 멤버의 토큰 발행 정보 삭제
        val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
        val tokenType = authorizationSplit[0].trim().lowercase() // (ex : "bearer")
        val token = authorizationSplit[1].trim() // (ex : "abcd1234")

        val tokenInfo = database1Service1LogInTokenInfoRepository.findByTokenTypeAndAccessTokenAndRowActivate(
            tokenType,
            token,
            true
        )

        if (tokenInfo != null) {
            tokenInfo.rowActivate = false
            database1Service1LogInTokenInfoRepository.save(tokenInfo)
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api9(
        authorization: String,
        inputVo: C9Service1TkV1AuthController.Api9InputVo,
        httpServletResponse: HttpServletResponse
    ): C9Service1TkV1AuthController.Api9OutputVo? {
        val accessTokenMemberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val memberInfo =
            database1Service1MemberDataRepository.findByUidAndRowActivate(accessTokenMemberUid.toLong(), true)

        if (memberInfo == null) {
            // 가입되지 않은 회원
            httpServletResponse.status = 500
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
                        !JwtTokenUtilObject.validateSignature(
                            jwtRefreshToken,
                            SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
                        ) || // 시크릿 검증이 유효하지 않을 때 = 시크릿 검증시 정보가 틀림 = 위변조된 토큰
                        JwtTokenUtilObject.getTokenUsage(
                            jwtRefreshToken,
                            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
                        )
                            .lowercase() != "refresh" || // 토큰 타입이 Refresh 토큰이 아닐 때
                        JwtTokenUtilObject.getIssuer(jwtRefreshToken) != SecurityConfig.AuthTokenFilterService1Tk.ISSUER || // 발행인이 다를 때
                        refreshTokenType.lowercase() != "jwt" || // 토큰 타입이 JWT 가 아닐 때
                        JwtTokenUtilObject.getRemainSeconds(jwtRefreshToken) * 1000 > SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS || // 최대 만료시간을 초과
                        JwtTokenUtilObject.getMemberUid(
                            jwtRefreshToken,
                            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
                        ) != accessTokenMemberUid // 리프레시 토큰의 멤버 고유번호와 액세스 토큰 멤버 고유번호가 다를시
                    ) {
                        httpServletResponse.status = 500
                        httpServletResponse.setHeader("api-result-code", "2")
                        return null
                    }

                    // 저장된 현재 인증된 멤버의 리프레시 토큰 가져오기
                    val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]
                    val tokenType1 = authorizationSplit[0].trim().lowercase() // (ex : "bearer")
                    val token = authorizationSplit[1].trim() // (ex : "abcd1234")

                    val tokenInfo =
                        database1Service1LogInTokenInfoRepository.findByTokenTypeAndAccessTokenAndRowActivate(
                            tokenType1,
                            token,
                            true
                        )

                    if (JwtTokenUtilObject.getRemainSeconds(jwtRefreshToken) == 0L || // 만료시간 지남
                        tokenInfo == null // jwtAccessToken 의 리프레시 토큰이 저장소에 없음
                    ) {
                        httpServletResponse.status = 500
                        httpServletResponse.setHeader("api-result-code", "3")
                        return null
                    }

                    if (jwtRefreshToken != tokenInfo.refreshToken) {
                        // 건내받은 토큰이 해당 액세스 토큰의 가용 토큰과 맞지 않음
                        httpServletResponse.status = 500
                        httpServletResponse.setHeader("api-result-code", "4")
                        return null
                    }

                    // 먼저 로그아웃 처리
                    tokenInfo.rowActivate = false
                    database1Service1LogInTokenInfoRepository.save(tokenInfo)

                    // 멤버의 권한 리스트를 조회 후 반환
                    val memberRoleList = database1Service1MemberRoleDataRepository.findAllByMemberUidAndRowActivate(
                        accessTokenMemberUid.toLong(),
                        true
                    )

                    val roleList: ArrayList<String> = arrayListOf()
                    for (userRole in memberRoleList) {
                        roleList.add(userRole.role)
                    }

                    // 새 토큰 생성 및 로그인 처리
                    val newJwtAccessToken = JwtTokenUtilObject.generateAccessToken(
                        accessTokenMemberUid, roleList,
                        SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS,
                        SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                        SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
                        SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
                        SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
                    )

                    val accessTokenExpireWhen: String = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                    ).format(Calendar.getInstance().apply {
                        this.time = Date()
                        this.add(
                            Calendar.MILLISECOND,
                            SecurityConfig.AuthTokenFilterService1Tk.ACCESS_TOKEN_EXPIRATION_TIME_MS.toInt()
                        )
                    }.time)

                    val newRefreshToken = JwtTokenUtilObject.generateRefreshToken(
                        accessTokenMemberUid,
                        SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS,
                        SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                        SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY,
                        SecurityConfig.AuthTokenFilterService1Tk.ISSUER,
                        SecurityConfig.AuthTokenFilterService1Tk.JWT_SECRET_KEY_STRING
                    )

                    val refreshTokenExpireWhen: String = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                    ).format(Calendar.getInstance().apply {
                        this.time = Date()
                        this.add(
                            Calendar.MILLISECOND,
                            SecurityConfig.AuthTokenFilterService1Tk.REFRESH_TOKEN_EXPIRATION_TIME_MS.toInt()
                        )
                    }.time)

                    database1Service1LogInTokenInfoRepository.save(
                        Database1_Service1_LogInTokenInfo(
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

                    val emailEntityList = database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(
                        accessTokenMemberUid.toLong(),
                        true
                    )
                    val emailList = ArrayList<String>()
                    for (emailEntity in emailEntityList) {
                        emailList.add(
                            emailEntity.emailAddress
                        )
                    }

                    val phoneEntityList = database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(
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
                        database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(
                            accessTokenMemberUid.toLong(),
                            true
                        )
                    val myOAuth2List = ArrayList<C9Service1TkV1AuthController.Api9OutputVo.OAuth2Info>()
                    for (oAuth2Entity in oAuth2EntityList) {
                        myOAuth2List.add(
                            C9Service1TkV1AuthController.Api9OutputVo.OAuth2Info(
                                oAuth2Entity.oauth2TypeCode.toInt(),
                                oAuth2Entity.oauth2Id
                            )
                        )
                    }

                    val profileData = database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(
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
                    return C9Service1TkV1AuthController.Api9OutputVo(
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
                        myOAuth2List,
                        memberInfo.accountPassword == null
                    )
                }

                else -> {
                    // 처리 가능한 토큰 타입이 아닐 때
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }
            }
        } else {
            // 타입을 전달 하지 않았을 때
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }
    }


    ////
    fun api10(authorization: String, httpServletResponse: HttpServletResponse) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        // loginAccessToken 의 Iterable 가져오기
        val tokenInfoList = database1Service1LogInTokenInfoRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        // 발행되었던 모든 액세스 토큰 무효화 (다른 디바이스에선 사용중 로그아웃된 것과 동일한 효과)
        for (tokenInfo in tokenInfoList) {
            tokenInfo.rowActivate = false

            database1Service1LogInTokenInfoRepository.save(tokenInfo)
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api11(httpServletResponse: HttpServletResponse, nickName: String): C9Service1TkV1AuthController.Api11OutputVo? {
        httpServletResponse.setHeader("api-result-code", "0")
        return C9Service1TkV1AuthController.Api11OutputVo(
            database1Service1MemberDataRepository.existsByNickNameAndRowActivate(nickName.trim(), true)
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api12(httpServletResponse: HttpServletResponse, authorization: String, nickName: String) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
        val userInfo = database1Service1MemberDataRepository.findById(memberUid.toLong()).get()

        if (database1Service1MemberDataRepository.existsByNickNameAndRowActivate(nickName, true)) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        userInfo.nickName = nickName
        database1Service1MemberDataRepository.save(
            userInfo
        )

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api13(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api13InputVo
    ): C9Service1TkV1AuthController.Api13OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                inputVo.email,
                true
            )

        if (isDatabase1MemberUserExists) { // 기존 회원 존재
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 이메일 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberRegisterEmailVerificationData =
            database1Service1JoinTheMembershipWithEmailVerificationDataRepository.save(
                Database1_Service1_JoinTheMembershipWithEmailVerificationData(
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
        return C9Service1TkV1AuthController.Api13OutputVo(
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
    ): C9Service1TkV1AuthController.Api14OutputVo? {
        val emailVerificationOpt =
            database1Service1JoinTheMembershipWithEmailVerificationDataRepository.findById(verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != email
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            emailVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1Service1JoinTheMembershipWithEmailVerificationDataRepository.save(
                emailVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9Service1TkV1AuthController.Api14OutputVo(
                emailVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api15(httpServletResponse: HttpServletResponse, inputVo: C9Service1TkV1AuthController.Api15InputVo) {
        val emailVerificationOpt =
            database1Service1JoinTheMembershipWithEmailVerificationDataRepository.findById(inputVo.verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != inputVo.email
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1Service1MemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            if (database1Service1MemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "5")
                return
            }

            val password: String? = passwordEncoder.encode(inputVo.password) // 비밀번호 암호화

            // 회원가입
            val database1MemberUser = database1Service1MemberDataRepository.save(
                Database1_Service1_MemberData(
                    inputVo.nickName,
                    password,
                    true
                )
            )

            // 이메일 저장
            database1Service1MemberEmailDataRepository.save(
                Database1_Service1_MemberEmailData(
                    database1MemberUser.uid!!,
                    inputVo.email,
                    true
                )
            )

            // 역할 저장
            val database1MemberUserRoleList = ArrayList<Database1_Service1_MemberRoleData>()
            // 기본 권한 추가
//        database1MemberUserRoleList.add(
//            Database1_Member_MemberRole(
//                database1MemberUser.uid!!,
//                2,
//                true
//            )
//        )
            database1Service1MemberRoleDataRepository.saveAll(database1MemberUserRoleList)

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

                savedProfileImageUrl = "${externalAccessAddress}/service1/tk/v1/auth/member-profile/$savedFileName"
                //----------------------------------------------------------------------------------------------------------

                database1Service1MemberProfileDataRepository.save(
                    Database1_Service1_MemberProfileData(
                        database1MemberUser.uid!!,
                        savedProfileImageUrl,
                        true,
                        rowActivate = true
                    )
                )
            }

            // 확인 완료된 검증 요청 정보 삭제
            emailVerification.rowActivate = false
            database1Service1JoinTheMembershipWithEmailVerificationDataRepository.save(emailVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api16(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api16InputVo
    ): C9Service1TkV1AuthController.Api16OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                inputVo.phoneNumber,
                true
            )

        if (isDatabase1MemberUserExists) { // 기존 회원 존재
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberRegisterPhoneNumberVerificationData =
            database1Service1JoinTheMembershipWithPhoneNumberVerificationDataRepository.save(
                Database1_Service1_JoinTheMembershipWithPhoneNumberVerificationData(
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
        return C9Service1TkV1AuthController.Api16OutputVo(
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
    ): C9Service1TkV1AuthController.Api17OutputVo? {
        val phoneNumberVerificationOpt =
            database1Service1JoinTheMembershipWithPhoneNumberVerificationDataRepository.findById(verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != phoneNumber
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            phoneNumberVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1Service1JoinTheMembershipWithPhoneNumberVerificationDataRepository.save(
                phoneNumberVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9Service1TkV1AuthController.Api17OutputVo(
                phoneNumberVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api18(httpServletResponse: HttpServletResponse, inputVo: C9Service1TkV1AuthController.Api18InputVo) {
        val phoneNumberVerificationOpt =
            database1Service1JoinTheMembershipWithPhoneNumberVerificationDataRepository.findById(inputVo.verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != inputVo.phoneNumber
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1Service1MemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            if (database1Service1MemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "5")
                return
            }

            val password: String? = passwordEncoder.encode(inputVo.password) // 비밀번호 암호화

            // 회원가입
            val database1MemberUser = database1Service1MemberDataRepository.save(
                Database1_Service1_MemberData(
                    inputVo.nickName,
                    password,
                    true
                )
            )

            // 전화번호 저장
            database1Service1MemberPhoneDataRepository.save(
                Database1_Service1_MemberPhoneData(
                    database1MemberUser.uid!!,
                    inputVo.phoneNumber,
                    true
                )
            )

            // 역할 저장
            val database1MemberUserRoleList = ArrayList<Database1_Service1_MemberRoleData>()
            // 기본 권한 추가
//        database1MemberUserRoleList.add(
//            Database1_Member_MemberRole(
//                database1MemberUser.uid!!,
//                2,
//                true
//            )
//        )
            database1Service1MemberRoleDataRepository.saveAll(database1MemberUserRoleList)

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

                savedProfileImageUrl = "${externalAccessAddress}/service1/tk/v1/auth/member-profile/$savedFileName"
                //----------------------------------------------------------------------------------------------------------

                database1Service1MemberProfileDataRepository.save(
                    Database1_Service1_MemberProfileData(
                        database1MemberUser.uid!!,
                        savedProfileImageUrl,
                        true,
                        rowActivate = true
                    )
                )
            }

            // 확인 완료된 검증 요청 정보 삭제
            phoneNumberVerification.rowActivate = false
            database1Service1JoinTheMembershipWithPhoneNumberVerificationDataRepository.save(phoneNumberVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api19(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api19InputVo
    ): C9Service1TkV1AuthController.Api19OutputVo? {
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
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                loginId = response.body()!!.id!!

                val isDatabase1MemberUserExists =
                    database1Service1MemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        1,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1Service1JoinTheMembershipWithOauth2VerificationDataRepository.save(
                        Database1_Service1_JoinTheMembershipWithOauth2VerificationData(
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
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                loginId = response.body()!!.response.id

                val isDatabase1MemberUserExists =
                    database1Service1MemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        2,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1Service1JoinTheMembershipWithOauth2VerificationDataRepository.save(
                        Database1_Service1_JoinTheMembershipWithOauth2VerificationData(
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
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                loginId = response.body()!!.id.toString()

                val isDatabase1MemberUserExists =
                    database1Service1MemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        3,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1Service1JoinTheMembershipWithOauth2VerificationDataRepository.save(
                        Database1_Service1_JoinTheMembershipWithOauth2VerificationData(
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
        return C9Service1TkV1AuthController.Api19OutputVo(
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
        inputVo: C9Service1TkV1AuthController.Api19Dot1InputVo
    ): C9Service1TkV1AuthController.Api19Dot1OutputVo? {
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
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "1")
                    return null
                }

                val isDatabase1MemberUserExists =
                    database1Service1MemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                        4,
                        loginId,
                        true
                    )

                if (isDatabase1MemberUserExists) { // 기존 회원 존재
                    httpServletResponse.status = 500
                    httpServletResponse.setHeader("api-result-code", "2")
                    return null
                }

                verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
                val database1MemberRegisterOauth2VerificationData =
                    database1Service1JoinTheMembershipWithOauth2VerificationDataRepository.save(
                        Database1_Service1_JoinTheMembershipWithOauth2VerificationData(
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
        return C9Service1TkV1AuthController.Api19Dot1OutputVo(
            verificationUid,
            verificationCode,
            loginId,
            expireWhen
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api20(httpServletResponse: HttpServletResponse, inputVo: C9Service1TkV1AuthController.Api20InputVo) {
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
            database1Service1JoinTheMembershipWithOauth2VerificationDataRepository.findById(inputVo.verificationUid)

        if (oauth2VerificationOpt.isEmpty) { // 해당 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val oauth2Verification = oauth2VerificationOpt.get()

        if (!oauth2Verification.rowActivate ||
            oauth2Verification.oauth2TypeCode != oauth2TypeCode.toByte() ||
            oauth2Verification.oauth2Id != inputVo.oauth2Id
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(oauth2Verification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = oauth2Verification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1Service1MemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                    inputVo.oauth2TypeCode.toByte(),
                    inputVo.oauth2Id,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            if (database1Service1MemberDataRepository.existsByNickNameAndRowActivate(inputVo.nickName.trim(), true)) {
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "5")
                return
            }

            // 회원가입
            val database1MemberUser = database1Service1MemberDataRepository.save(
                Database1_Service1_MemberData(
                    inputVo.nickName,
                    null,
                    true
                )
            )

            // SNS OAUth2 저장
            database1Service1MemberOauth2LoginDataRepository.save(
                Database1_Service1_MemberOauth2LoginData(
                    database1MemberUser.uid!!,
                    inputVo.oauth2TypeCode.toByte(),
                    inputVo.oauth2Id,
                    true
                )
            )

            // 역할 저장
            val database1MemberUserRoleList = ArrayList<Database1_Service1_MemberRoleData>()
            // 기본 권한 추가
//        database1MemberUserRoleList.add(
//            Database1_Member_MemberRole(
//                database1MemberUser.uid!!,
//                2,
//                true
//            )
//        )
            database1Service1MemberRoleDataRepository.saveAll(database1MemberUserRoleList)

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

                savedProfileImageUrl = "${externalAccessAddress}/service1/tk/v1/auth/member-profile/$savedFileName"
                //----------------------------------------------------------------------------------------------------------

                database1Service1MemberProfileDataRepository.save(
                    Database1_Service1_MemberProfileData(
                        database1MemberUser.uid!!,
                        savedProfileImageUrl,
                        true,
                        rowActivate = true
                    )
                )
            }

            // 확인 완료된 검증 요청 정보 삭제
            oauth2Verification.rowActivate = false
            database1Service1JoinTheMembershipWithOauth2VerificationDataRepository.save(oauth2Verification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api21(
        httpServletResponse: HttpServletResponse,
        authorization: String,
        inputVo: C9Service1TkV1AuthController.Api21InputVo
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(memberUid.toLong(), true)

        if (member == null) { // 멤버 정보가 없을 때
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (member.accountPassword == null) { // 기존 비번이 존재하지 않음
            if (inputVo.oldPassword != null) { // 비밀번호 불일치
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }
        } else { // 기존 비번 존재
            if (inputVo.oldPassword == null || !passwordEncoder.matches(
                    inputVo.oldPassword,
                    member.accountPassword
                )
            ) { // 비밀번호 불일치
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "2")
                return
            }
        }

        if (inputVo.newPassword == null) {
            val oAuth2EntityList =
                database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(
                    memberUid.toLong(),
                    true
                )

            if (oAuth2EntityList.isEmpty()) {
                // null 로 만들려고 할 때 account 외의 OAuth2 인증이 없다면 제거 불가
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "3")
                return
            }

            member.accountPassword = null
        } else {
            member.accountPassword = passwordEncoder.encode(inputVo.newPassword) // 비밀번호는 암호화
        }
        database1Service1MemberDataRepository.save(member)

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api22(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api22InputVo
    ): C9Service1TkV1AuthController.Api22OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                inputVo.email,
                true
            )
        if (!isDatabase1MemberUserExists) { // 회원 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 이메일 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberFindPasswordEmailVerificationData =
            database1Service1FindPasswordWithEmailVerificationDataRepository.save(
                Database1_Service1_FindPasswordWithEmailVerificationData(
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

        return C9Service1TkV1AuthController.Api22OutputVo(
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
    ): C9Service1TkV1AuthController.Api23OutputVo? {
        val emailVerificationOpt =
            database1Service1FindPasswordWithEmailVerificationDataRepository.findById(verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != email
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            emailVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1Service1FindPasswordWithEmailVerificationDataRepository.save(
                emailVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9Service1TkV1AuthController.Api23OutputVo(
                emailVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api24(httpServletResponse: HttpServletResponse, inputVo: C9Service1TkV1AuthController.Api24InputVo) {
        val emailVerificationOpt =
            database1Service1FindPasswordWithEmailVerificationDataRepository.findById(inputVo.verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.emailAddress != inputVo.email
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            // 입력 데이터 검증
            val memberEmail =
                database1Service1MemberEmailDataRepository.findByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )

            if (memberEmail == null) {
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
                memberEmail.memberUid,
                true
            )

            if (member == null) {
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            // 랜덤 비번 생성 후 세팅
            val newPassword = String.format("%09d", Random().nextInt(999999999)) // 랜덤 9자리 숫자
            member.accountPassword = passwordEncoder.encode(newPassword) // 비밀번호는 암호화
            database1Service1MemberDataRepository.save(member)

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
            database1Service1FindPasswordWithEmailVerificationDataRepository.save(emailVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api25(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api25InputVo
    ): C9Service1TkV1AuthController.Api25OutputVo? {
        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                inputVo.phoneNumber,
                true
            )
        if (!isDatabase1MemberUserExists) { // 회원 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberFindPasswordPhoneNumberVerificationData =
            database1Service1FindPasswordWithPhoneNumberVerificationDataRepository.save(
                Database1_Service1_FindPasswordWithPhoneNumberVerificationData(
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

        return C9Service1TkV1AuthController.Api25OutputVo(
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
    ): C9Service1TkV1AuthController.Api26OutputVo? {
        val phoneNumberVerificationOpt =
            database1Service1FindPasswordWithPhoneNumberVerificationDataRepository.findById(verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != phoneNumber
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            phoneNumberVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1Service1FindPasswordWithPhoneNumberVerificationDataRepository.save(
                phoneNumberVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9Service1TkV1AuthController.Api26OutputVo(
                phoneNumberVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api27(httpServletResponse: HttpServletResponse, inputVo: C9Service1TkV1AuthController.Api27InputVo) {
        val phoneNumberVerificationOpt =
            database1Service1FindPasswordWithPhoneNumberVerificationDataRepository.findById(inputVo.verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.phoneNumber != inputVo.phoneNumber
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            // 입력 데이터 검증
            val memberPhone =
                database1Service1MemberPhoneDataRepository.findByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )

            if (memberPhone == null) {
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
                memberPhone.memberUid,
                true
            )

            if (member == null) {
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            // 랜덤 비번 생성 후 세팅
            val newPassword = String.format("%09d", Random().nextInt(999999999)) // 랜덤 9자리 숫자
            member.accountPassword = passwordEncoder.encode(newPassword) // 비밀번호는 암호화
            database1Service1MemberDataRepository.save(member)

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
            database1Service1FindPasswordWithPhoneNumberVerificationDataRepository.save(phoneNumberVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    fun api28(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ): C9Service1TkV1AuthController.Api28OutputVo {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val emailEntityList =
            database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        val phoneEntityList =
            database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        val oAuth2EntityList =
            database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val myOAuth2List = ArrayList<C9Service1TkV1AuthController.Api28OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9Service1TkV1AuthController.Api28OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9Service1TkV1AuthController.Api28OutputVo(
            emailList,
            phoneNumberList,
            myOAuth2List
        )
    }


    ////
    fun api29(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ): C9Service1TkV1AuthController.Api29OutputVo {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val emailEntityList =
            database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val emailList = ArrayList<String>()
        for (emailEntity in emailEntityList) {
            emailList.add(
                emailEntity.emailAddress
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9Service1TkV1AuthController.Api29OutputVo(
            emailList
        )
    }


    ////
    fun api30(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ): C9Service1TkV1AuthController.Api30OutputVo {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val phoneEntityList =
            database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val phoneNumberList = ArrayList<String>()
        for (emailEntity in phoneEntityList) {
            phoneNumberList.add(
                emailEntity.phoneNumber
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9Service1TkV1AuthController.Api30OutputVo(
            phoneNumberList
        )
    }


    ////
    fun api31(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ): C9Service1TkV1AuthController.Api31OutputVo {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val oAuth2EntityList =
            database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUid.toLong(), true)
        val myOAuth2List = ArrayList<C9Service1TkV1AuthController.Api31OutputVo.OAuth2Info>()
        for (oAuth2Entity in oAuth2EntityList) {
            myOAuth2List.add(
                C9Service1TkV1AuthController.Api31OutputVo.OAuth2Info(
                    oAuth2Entity.oauth2TypeCode.toInt(),
                    oAuth2Entity.oauth2Id
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9Service1TkV1AuthController.Api31OutputVo(
            myOAuth2List
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api32(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api32InputVo,
        authorization: String
    ): C9Service1TkV1AuthController.Api32OutputVo? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                inputVo.email,
                true
            )

        if (isDatabase1MemberUserExists) { // 기존 회원 존재
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 이메일 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberRegisterEmailVerificationData = database1Service1AddEmailVerificationDataRepository.save(
            Database1_Service1_AddEmailVerificationData(
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
        return C9Service1TkV1AuthController.Api32OutputVo(
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
    ): C9Service1TkV1AuthController.Api33OutputVo? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val emailVerificationOpt = database1Service1AddEmailVerificationDataRepository.findById(verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.memberUid != memberUid.toLong() ||
            emailVerification.emailAddress != email
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            emailVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1Service1AddEmailVerificationDataRepository.save(
                emailVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9Service1TkV1AuthController.Api33OutputVo(
                emailVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api34(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api34InputVo,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val emailVerificationOpt =
            database1Service1AddEmailVerificationDataRepository.findById(inputVo.verificationUid)

        if (emailVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val emailVerification = emailVerificationOpt.get()

        if (!emailVerification.rowActivate ||
            emailVerification.memberUid != memberUid.toLong() ||
            emailVerification.emailAddress != inputVo.email
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(emailVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = emailVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1Service1MemberEmailDataRepository.existsByEmailAddressAndRowActivate(
                    inputVo.email,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            // 이메일 추가
            database1Service1MemberEmailDataRepository.save(
                Database1_Service1_MemberEmailData(
                    memberUid.toLong(),
                    inputVo.email,
                    true
                )
            )

            // 확인 완료된 검증 요청 정보 삭제
            emailVerification.rowActivate = false
            database1Service1AddEmailVerificationDataRepository.save(emailVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api35(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api35InputVo,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
        val memberUidLong = memberUid.toLong()

        // 내 계정에 등록된 모든 이메일 리스트 가져오기
        val myEmailList = database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(
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

        val isOauth2Exists = database1Service1MemberOauth2LoginDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        // 지우려는 이메일 외에 로그인 방법이 존재하는지 확인
        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        )!!

        val isMemberPhoneExists = database1Service1MemberPhoneDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        if (isOauth2Exists || (member.accountPassword != null && (isMemberPhoneExists || myEmailList.size > 1))) {
            // 사용 가능한 계정 로그인 정보가 존재
            val selectedEmailEntity = myEmailList[selectedEmailIdx]

            // 이메일 지우기
            selectedEmailEntity.rowActivate = false
            database1Service1MemberEmailDataRepository.save(
                selectedEmailEntity
            )

            httpServletResponse.setHeader("api-result-code", "0")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.status = 500
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api36(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api36InputVo,
        authorization: String
    ): C9Service1TkV1AuthController.Api36OutputVo? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        // 입력 데이터 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                inputVo.phoneNumber,
                true
            )

        if (isDatabase1MemberUserExists) { // 기존 회원 존재
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        // 정보 저장 후 이메일 발송
        val verificationTimeSec: Long = 60 * 10
        val verificationCode = String.format("%06d", Random().nextInt(999999)) // 랜덤 6자리 숫자
        val database1MemberAddPhoneNumberVerificationData =
            database1Service1AddPhoneNumberVerificationDataRepository.save(
                Database1_Service1_AddPhoneNumberVerificationData(
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
        return C9Service1TkV1AuthController.Api36OutputVo(
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
    ): C9Service1TkV1AuthController.Api37OutputVo? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val phoneNumberVerificationOpt =
            database1Service1AddPhoneNumberVerificationDataRepository.findById(verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.memberUid != memberUid.toLong() ||
            phoneNumberVerification.phoneNumber != phoneNumber
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return null
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return null
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == verificationCode

        return if (codeMatched) { // 코드 일치
            val verificationTimeSec: Long = 60 * 10
            phoneNumberVerification.verificationExpireWhen =
                LocalDateTime.now().plusSeconds(verificationTimeSec)
            database1Service1AddPhoneNumberVerificationDataRepository.save(
                phoneNumberVerification
            )

            httpServletResponse.setHeader("api-result-code", "0")
            C9Service1TkV1AuthController.Api37OutputVo(
                phoneNumberVerification.verificationExpireWhen.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            )
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            null
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api38(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api38InputVo,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val phoneNumberVerificationOpt =
            database1Service1AddPhoneNumberVerificationDataRepository.findById(inputVo.verificationUid)

        if (phoneNumberVerificationOpt.isEmpty) { // 해당 이메일 검증을 요청한적이 없음
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        val phoneNumberVerification = phoneNumberVerificationOpt.get()

        if (!phoneNumberVerification.rowActivate ||
            phoneNumberVerification.memberUid != memberUid.toLong() ||
            phoneNumberVerification.phoneNumber != inputVo.phoneNumber
        ) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (LocalDateTime.now().isAfter(phoneNumberVerification.verificationExpireWhen)) {
            // 만료됨
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 입력 코드와 발급된 코드와의 매칭
        val codeMatched = phoneNumberVerification.verificationSecret == inputVo.verificationCode

        if (codeMatched) { // 코드 일치
            val isUserExists =
                database1Service1MemberPhoneDataRepository.existsByPhoneNumberAndRowActivate(
                    inputVo.phoneNumber,
                    true
                )
            if (isUserExists) { // 기존 회원이 있을 때
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "4")
                return
            }

            // 이메일 추가
            database1Service1MemberPhoneDataRepository.save(
                Database1_Service1_MemberPhoneData(
                    memberUid.toLong(),
                    inputVo.phoneNumber,
                    true
                )
            )

            // 확인 완료된 검증 요청 정보 삭제
            phoneNumberVerification.rowActivate = false
            database1Service1AddPhoneNumberVerificationDataRepository.save(phoneNumberVerification)

            httpServletResponse.setHeader("api-result-code", "0")
            return
        } else { // 코드 불일치
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api39(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api39InputVo,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
        val memberUidLong = memberUid.toLong()

        // 내 계정에 등록된 모든 전화번호 리스트 가져오기
        val myPhoneList = database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(
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

        val isOauth2Exists = database1Service1MemberOauth2LoginDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        // 지우려는 전화번호 외에 로그인 방법이 존재하는지 확인
        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        )!!

        val isMemberEmailExists = database1Service1MemberEmailDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        if (isOauth2Exists || (member.accountPassword != null && (isMemberEmailExists || myPhoneList.size > 1))) {
            // 사용 가능한 계정 로그인 정보가 존재

            // 전화번호 지우기
            selectedPhoneEntity.rowActivate = false
            database1Service1MemberPhoneDataRepository.save(
                selectedPhoneEntity
            )

            httpServletResponse.setHeader("api-result-code", "0")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.status = 500
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api40(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1AuthController.Api40InputVo,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

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
                    httpServletResponse.status = 500
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
                    httpServletResponse.status = 500
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
                    httpServletResponse.status = 500
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
        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        if (member == null) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 사용중인지 아닌지 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                snsTypeCode.toByte(),
                snsId,
                true
            )

        if (isDatabase1MemberUserExists) { // 이미 사용중인 SNS 인증
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }

        // SNS 인증 추가
        database1Service1MemberOauth2LoginDataRepository.save(
            Database1_Service1_MemberOauth2LoginData(
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
        inputVo: C9Service1TkV1AuthController.Api40Dot1InputVo,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val snsTypeCode: Int
        val snsId: String

        // (정보 검증 로직 수행)
        when (inputVo.oauth2TypeCode) {
            4 -> { // Apple
                val appleInfo = AppleOAuthHelperUtilObject.getAppleMemberData(inputVo.oauth2IdToken)

                if (appleInfo != null) {
                    snsId = appleInfo.snsId
                } else {
                    httpServletResponse.status = 500
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
        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        if (member == null) {
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "2")
            return
        }

        // 사용중인지 아닌지 검증
        val isDatabase1MemberUserExists =
            database1Service1MemberOauth2LoginDataRepository.existsByOauth2TypeCodeAndOauth2IdAndRowActivate(
                snsTypeCode.toByte(),
                snsId,
                true
            )

        if (isDatabase1MemberUserExists) { // 이미 사용중인 SNS 인증
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "3")
            return
        }

        // SNS 인증 추가
        database1Service1MemberOauth2LoginDataRepository.save(
            Database1_Service1_MemberOauth2LoginData(
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
        inputVo: C9Service1TkV1AuthController.Api41InputVo,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
        val memberUidLong = memberUid.toLong()

        // 내 계정에 등록된 모든 인증 리스트 가져오기
        val myOAuth2List = database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(
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

        val isMemberEmailExists = database1Service1MemberEmailDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        val isMemberPhoneExists = database1Service1MemberPhoneDataRepository.existsByMemberUidAndRowActivate(
            memberUidLong,
            true
        )

        // 지우려는 정보 외에 로그인 방법이 존재하는지 확인
        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        )!!

        if (myOAuth2List.size > 1 || (member.accountPassword != null && (isMemberEmailExists || isMemberPhoneExists))) {
            // 사용 가능한 계정 로그인 정보가 존재

            // 로그인 정보 지우기
            selectedOAuth2Entity.rowActivate = false
            database1Service1MemberOauth2LoginDataRepository.save(
                selectedOAuth2Entity
            )

            httpServletResponse.setHeader("api-result-code", "0")
            return
        }

        // 이외에 사용 가능한 로그인 정보가 존재하지 않을 때
        httpServletResponse.status = 500
        httpServletResponse.setHeader("api-result-code", "1")
        return
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api42(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
        val memberUidLong = memberUid.toLong()

        val member = database1Service1MemberDataRepository.findByUidAndRowActivate(
            memberUidLong,
            true
        ) ?: return  // 가입된 회원이 없음 = 회원탈퇴한 것으로 처리

        // 회원탈퇴 처리
        member.rowActivate = false
        database1Service1MemberDataRepository.save(
            member
        )

        // member_phone, member_email, member_role, member_sns_oauth2, member_profile 비활성화
        val emailList = database1Service1MemberEmailDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (email in emailList) {
            email.rowActivate = false
            database1Service1MemberEmailDataRepository.save(email)
        }

        val memberRoleList =
            database1Service1MemberRoleDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (memberRole in memberRoleList) {
            memberRole.rowActivate = false
            database1Service1MemberRoleDataRepository.save(memberRole)
        }

        val memberSnsOauth2List =
            database1Service1MemberOauth2LoginDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (memberSnsOauth2 in memberSnsOauth2List) {
            memberSnsOauth2.rowActivate = false
            database1Service1MemberOauth2LoginDataRepository.save(memberSnsOauth2)
        }

        val memberPhoneList =
            database1Service1MemberPhoneDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (memberPhone in memberPhoneList) {
            memberPhone.rowActivate = false
            database1Service1MemberPhoneDataRepository.save(memberPhone)
        }

        val profileData =
            database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(memberUidLong, true)
        for (profile in profileData) {
            profile.rowActivate = false
            database1Service1MemberProfileDataRepository.save(profile)
        }


        // !!!회원과 관계된 처리!!

        // loginAccessToken 의 Iterable 가져오기
        val tokenInfoList = database1Service1LogInTokenInfoRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        // 발행되었던 모든 액세스 토큰 무효화 (다른 디바이스에선 사용중 로그아웃된 것과 동일한 효과)
        for (tokenInfo in tokenInfoList) {
            tokenInfo.rowActivate = false

            database1Service1LogInTokenInfoRepository.save(tokenInfo)
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    fun api43(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ): C9Service1TkV1AuthController.Api43OutputVo? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val profileData = database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        val myProfileList: ArrayList<C9Service1TkV1AuthController.Api43OutputVo.ProfileInfo> = ArrayList()
        for (profile in profileData) {
            myProfileList.add(
                C9Service1TkV1AuthController.Api43OutputVo.ProfileInfo(
                    profile.uid!!,
                    profile.imageFullUrl,
                    profile.isSelected
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9Service1TkV1AuthController.Api43OutputVo(
            myProfileList
        )
    }


    ////
    fun api44(
        httpServletResponse: HttpServletResponse,
        authorization: String
    ): C9Service1TkV1AuthController.Api44OutputVo? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        val profileData = database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        var myProfile: C9Service1TkV1AuthController.Api44OutputVo.ProfileInfo? = null
        for (profile in profileData) {
            if (profile.isSelected) {
                myProfile = C9Service1TkV1AuthController.Api44OutputVo.ProfileInfo(
                    profile.uid!!,
                    profile.imageFullUrl,
                    profile.isSelected
                )
                break
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
        return C9Service1TkV1AuthController.Api44OutputVo(
            myProfile
        )
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api45(httpServletResponse: HttpServletResponse, authorization: String, profileUid: Long?) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        // 내 프로필 리스트 가져오기
        val profileData = database1Service1MemberProfileDataRepository.findAllByMemberUidAndRowActivate(
            memberUid.toLong(),
            true
        )

        if (profileData.isEmpty()) {
            // 내 프로필이 하나도 없을 때
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (profileUid == null) {
            for (profile in profileData) {
                profile.isSelected = false
                database1Service1MemberProfileDataRepository.save(profile)
            }

            httpServletResponse.setHeader("api-result-code", "0")
            return
        }


        // 기존에 선택된 프로필
        var frontProfile: Database1_Service1_MemberProfileData? = null

        // 이번에 선택하려는 프로필
        var selectedProfile: Database1_Service1_MemberProfileData? = null
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
            httpServletResponse.status = 500
            httpServletResponse.setHeader("api-result-code", "1")
            return
        }

        if (frontProfile != null) {
            // 기존에 선택된 프로필 선택 해제
            frontProfile.isSelected = false
            database1Service1MemberProfileDataRepository.save(frontProfile)
        }

        // 이번에 선택하려는 프로필을 선택하기
        selectedProfile.isSelected = true
        database1Service1MemberProfileDataRepository.save(selectedProfile)

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api46(authorization: String, httpServletResponse: HttpServletResponse, profileUid: Long) {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

        // 프로필 가져오기
        val profileDataOpt = database1Service1MemberProfileDataRepository.findById(profileUid)

        if (profileDataOpt.isPresent) {
            // 프로필이 존재할 때
            val profileData = profileDataOpt.get()

            if (profileData.rowActivate &&
                profileData.memberUid == memberUid.toLong()
            ) {
                // 프로필이 활성 상태이고, 멤버 고유번호가 내 고유 번호와 같을 때
                // 프로필 비활성화
                profileData.rowActivate = false
                database1Service1MemberProfileDataRepository.save(profileData)
            }
        }

        httpServletResponse.setHeader("api-result-code", "0")
    }


    ////
    @CustomTransactional([Database1Config.TRANSACTION_NAME])
    fun api47(
        httpServletResponse: HttpServletResponse,
        authorization: String,
        inputVo: C9Service1TkV1AuthController.Api47InputVo
    ): C9Service1TkV1AuthController.Api47OutputVo? {
        val memberUid = JwtTokenUtilObject.getMemberUid(
            authorization.split(" ")[1].trim(),
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            SecurityConfig.AuthTokenFilterService1Tk.JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )

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

        savedProfileImageUrl = "${externalAccessAddress}/service1/tk/v1/auth/member-profile/$savedFileName"
        //----------------------------------------------------------------------------------------------------------

        val profileData = database1Service1MemberProfileDataRepository.save(
            Database1_Service1_MemberProfileData(
                memberUid.toLong(),
                savedProfileImageUrl,
                false,
                rowActivate = true
            )
        )

        httpServletResponse.setHeader("api-result-code", "0")

        return C9Service1TkV1AuthController.Api47OutputVo(
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
                httpServletResponse.status = 500
                httpServletResponse.setHeader("api-result-code", "1")
                return null
            }

            Files.notExists(serverFilePathObject) -> {
                // 파일이 없을 때
                httpServletResponse.status = 500
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