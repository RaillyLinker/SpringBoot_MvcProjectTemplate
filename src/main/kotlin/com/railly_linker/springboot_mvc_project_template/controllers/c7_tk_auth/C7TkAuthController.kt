package com.railly_linker.springboot_mvc_project_template.controllers.c7_tk_auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "/tk/auth APIs", description = "C7. 인증/인가 API 컨트롤러")
@RestController
@RequestMapping("/tk/auth")
class C7TkAuthController(
    private val service: C7TkAuthService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1. 비 로그인 접속 테스트",
        description = "비 로그인 접속 테스트용 API\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/for-no-signed-in")
    fun api1(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse
    ): Map<String, Any>? {
        return service.api1(httpServletResponse)
    }


    ////
    @Operation(
        summary = "N2. 로그인 진입 테스트 <>",
        description = "로그인 되어 있어야 진입 가능\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/for-signed-in")
    @PreAuthorize("isAuthenticated()")
    fun api2(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Map<String, Any>? {
        return service.api2(httpServletResponse, authorization!!)
    }


    ////
    @Operation(
        summary = "N3. ADMIN 권한 진입 테스트 <'ADMIN'>",
        description = "ADMIN 권한이 있어야 진입 가능\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/for-admin")
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    fun api3(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Map<String, Any>? {
        return service.api3(httpServletResponse, authorization!!)
    }


    ////
    @Operation(
        summary = "N4. Developer 권한 진입 테스트 <'ADMIN' or 'Developer'>",
        description = "Developer 권한이 있어야 진입 가능\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/for-developer")
    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_DEVELOPER') or hasRole('ROLE_ADMIN'))")
    fun api4(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Map<String, Any>? {
        return service.api4(httpServletResponse, authorization!!)
    }


    ////
    @Operation(
        summary = "N5. 계정 비밀번호 로그인",
        description = "계정 아이디 + 비밀번호를 사용하는 로그인 요청\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 가입 되지 않은 회원\n\n" +
                "2 : 패스워드 불일치\n\n" +
                "3 : 추가 로그인 금지됨(동시 로그인 제한시 추가 로그인을 금지한 상황일 때)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/sign-in-with-password")
    fun api5(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api5InputVo
    ): Api5OutputVo? {
        return service.api5(httpServletResponse, inputVo)
    }

    data class Api5InputVo(
        @Schema(
            description = "로그인 타입 (0 : 닉네임, 1 : 이메일, 2 : 전화번호)",
            required = true,
            example = "1"
        )
        @JsonProperty("signInTypeCode")
        val signInTypeCode: Int,

        @Schema(
            description = "아이디 (0 : 홍길동, 1 : test@gmail.com, 2 : 82)000-0000-0000)",
            required = true,
            example = "test@gmail.com"
        )
        @JsonProperty("id")
        val id: String,

        @Schema(
            description = "사용할 비밀번호",
            required = true,
            example = "kkdli!!"
        )
        @JsonProperty("password")
        val password: String
    )

    data class Api5OutputVo(
        @Schema(description = "멤버 고유값", required = true, example = "1")
        @JsonProperty("memberUid")
        val memberUid: String,

        @Schema(description = "닉네임", required = true, example = "홍길동")
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "권한 코드 리스트 (1 : 관리자(ROLE_ADMIN), 2 : 개발자(ROLE_DEVELOPER))",
            required = true,
            example = "[1, 2]"
        )
        @JsonProperty("roleCodeList")
        val roleCodeList: List<Int>,

        @Schema(description = "인증 토큰 타입", required = true, example = "Bearer")
        @JsonProperty("tokenType")
        val tokenType: String,

        @Schema(description = "엑세스 토큰", required = true, example = "kljlkjkfsdlwejoe")
        @JsonProperty("accessToken")
        val accessToken: String,

        @Schema(description = "리프레시 토큰", required = true, example = "cxfdsfpweiijewkrlerw")
        @JsonProperty("refreshToken")
        val refreshToken: String,

        @Schema(
            description = "엑세스 토큰 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("accessTokenExpireWhen")
        val accessTokenExpireWhen: String,

        @Schema(
            description = "리프레시 토큰 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("refreshTokenExpireWhen")
        val refreshTokenExpireWhen: String,

        @Schema(description = "내가 등록한 이메일 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<String>,

        @Schema(description = "내가 등록한 전화번호 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<String>,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2TypeCode")
            val oauth2TypeCode: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )
    }

    ////
    @Operation(
        summary = "N6. OAuth2 Code 로 OAuth2 AccessToken 발급",
        description = "OAuth2 Code 를 사용하여 얻은 OAuth2 AccessToken 발급\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 유효하지 않은 OAuth2 인증 정보",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/oauth2-access-token")
    fun api6(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(
            name = "oauth2TypeCode",
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            example = "1"
        )
        @RequestParam("oauth2TypeCode")
        oauth2TypeCode: Int,
        @Parameter(name = "oauth2Code", description = "OAuth2 인증으로 받은 OAuth2 Code", example = "asdfeqwer1234")
        @RequestParam("oauth2Code")
        oauth2Code: String
    ): Api6OutputVo? {
        return service.api6(httpServletResponse, oauth2TypeCode, oauth2Code)
    }

    data class Api6OutputVo(

        @Schema(
            description = "Code 로 발급받은 SNS AccessToken Type",
            required = true,
            example = "Bearer"
        )
        @JsonProperty("oAuth2AccessTokenType")
        val oAuth2AccessTokenType: String,

        @Schema(
            description = "Code 로 발급받은 SNS AccessToken",
            required = true,
            example = "abcd1234!@#$"
        )
        @JsonProperty("oAuth2AccessToken")
        val oAuth2AccessToken: String
    )

    ////
    @Operation(
        summary = "N7. OAuth2 로그인 (Access Token)",
        description = "OAuth2 Access Token 으로 로그인 요청\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 유효하지 않은 OAuth2 Access Token\n\n" +
                "2 : 가입 되지 않은 회원\n\n" +
                "3 : 추가 로그인 금지됨(동시 로그인 제한시 추가 로그인을 금지한 상황일 때)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/sign-in-with-oauth2-access-token")
    fun api7(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api7InputVo
    ): Api7OutputVo? {
        return service.api7(httpServletResponse, inputVo)
    }

    data class Api7InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 TokenType + AccessToken",
            required = true,
            example = "Bearer asdfeqwer1234"
        )
        @JsonProperty("oauth2AccessToken")
        val oauth2AccessToken: String
    )

    data class Api7OutputVo(
        @Schema(description = "멤버 고유값", required = true, example = "1")
        @JsonProperty("memberUid")
        val memberUid: String,

        @Schema(description = "닉네임", required = true, example = "홍길동")
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "권한 코드 리스트 (1 : 관리자(ROLE_ADMIN), 2 : 개발자(ROLE_DEVELOPER))",
            required = true,
            example = "[1, 2]"
        )
        @JsonProperty("roleCodeList")
        val roleCodeList: List<Int>,

        @Schema(description = "인증 토큰 타입", required = true, example = "Bearer")
        @JsonProperty("tokenType")
        val tokenType: String,

        @Schema(description = "엑세스 토큰", required = true, example = "kljlkjkfsdlwejoe")
        @JsonProperty("accessToken")
        val accessToken: String,

        @Schema(description = "리프레시 토큰", required = true, example = "cxfdsfpweiijewkrlerw")
        @JsonProperty("refreshToken")
        val refreshToken: String,

        @Schema(
            description = "엑세스 토큰 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("accessTokenExpireWhen")
        val accessTokenExpireWhen: String,

        @Schema(
            description = "리프레시 토큰 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("refreshTokenExpireWhen")
        val refreshTokenExpireWhen: String,

        @Schema(description = "내가 등록한 이메일 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<String>,

        @Schema(description = "내가 등록한 전화번호 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<String>,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2TypeCode")
            val oauth2TypeCode: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )
    }


    ////
    @Operation(
        summary = "N8. 로그아웃 처리 <>",
        description = "로그아웃 처리\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/sign-out")
    @PreAuthorize("isAuthenticated()")
    fun api8(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") authorization: String?
    ) {
        service.api8(authorization!!, httpServletResponse)
    }


    ////
    @Operation(
        summary = "N9. 토큰 재발급 <>",
        description = "엑세스 토큰 및 리프레시 토큰 재발행\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 가입되지 않은 회원\n\n" +
                "2 : 유효하지 않은 리프레시 토큰\n\n" +
                "3 : 리프레시 토큰 만료\n\n" +
                "4 : 리프레시 토큰이 액세스 토큰과 매칭되지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/reissue")
    @PreAuthorize("isAuthenticated()")
    fun api9(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api9InputVo
    ): Api9OutputVo? {
        return service.api9(authorization!!, inputVo, httpServletResponse)
    }

    data class Api9InputVo(
        @Schema(description = "리프레시 토큰 (토큰 타입을 앞에 붙이기)", required = true, example = "Bearer 1sdfsadfsdafsdafsdafd")
        @JsonProperty("refreshToken")
        val refreshToken: String
    )

    data class Api9OutputVo(
        @Schema(description = "멤버 고유값", required = true, example = "1")
        @JsonProperty("memberUid")
        val memberUid: String,

        @Schema(description = "닉네임", required = true, example = "홍길동")
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "권한 코드 리스트 (1 : 관리자(ROLE_ADMIN), 2 : 개발자(ROLE_DEVELOPER))",
            required = true,
            example = "[1, 2]"
        )
        @JsonProperty("roleCodeList")
        val roleCodeList: List<Int>,

        @Schema(description = "인증 토큰 타입", required = true, example = "Bearer")
        @JsonProperty("tokenType")
        val tokenType: String,

        @Schema(description = "엑세스 토큰", required = true, example = "kljlkjkfsdlwejoe")
        @JsonProperty("accessToken")
        val accessToken: String,

        @Schema(description = "리프레시 토큰", required = true, example = "cxfdsfpweiijewkrlerw")
        @JsonProperty("refreshToken")
        val refreshToken: String,

        @Schema(
            description = "엑세스 토큰 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("accessTokenExpireWhen")
        val accessTokenExpireWhen: String,

        @Schema(
            description = "리프레시 토큰 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("refreshTokenExpireWhen")
        val refreshTokenExpireWhen: String,

        @Schema(description = "내가 등록한 이메일 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<String>,

        @Schema(description = "내가 등록한 전화번호 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<String>,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2TypeCode")
            val oauth2TypeCode: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )
    }


    ////
    @Operation(
        summary = "N10. 멤버의 현재 발행된 모든 액세스 토큰, 리프레시 토큰 비활성화 (= 모든 기기에서 로그아웃) <>",
        description = "멤버의 현재 발행된 모든 액세스 토큰, 리프레시 토큰을 비활성화 (= 모든 기기에서 로그아웃) 하는 API\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @DeleteMapping("/all-authorization-token")
    @PreAuthorize("isAuthenticated()")
    fun api10(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ) {
        service.api10(authorization!!, httpServletResponse)
    }


    ////
    @Operation(
        summary = "N11. 닉네임 중복 검사",
        description = "닉네임 중복 여부 반환\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/nickname-duplicate-check")
    fun api11(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "nickName", description = "중복 검사 닉네임", example = "홍길동")
        @RequestParam("nickName")
        nickName: String
    ): Api11OutputVo? {
        return service.api11(
            httpServletResponse,
            nickName
        )
    }

    data class Api11OutputVo(
        @Schema(description = "중복여부", required = true, example = "false")
        @JsonProperty("duplicated")
        val duplicated: Boolean
    )


    ////
    @Operation(
        summary = "N12. 닉네임 수정하기 <>",
        description = "닉네임 수정하기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1. 중복된 닉네임 : 중복검사를 했음에도 그 사이에 동일 닉네임이 등록되었을 수 있음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PatchMapping("/my/profile/nickname")
    @PreAuthorize("isAuthenticated()")
    fun api12(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "nickName", description = "닉네임", example = "홍길동")
        @RequestParam(value = "nickName")
        nickName: String
    ) {
        service.api12(
            httpServletResponse,
            authorization!!,
            nickName
        )
    }

    ////
    @Operation(
        summary = "N13. 이메일 회원가입 본인 인증 이메일 발송",
        description = "이메일 회원가입시 본인 이메일 확인 메일 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 기존 회원 존재",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/register-with-email-verification")
    fun api13(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api13InputVo
    ): Api13OutputVo? {
        return service.api13(httpServletResponse, inputVo)
    }

    data class Api13InputVo(
        @Schema(description = "수신 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String
    )

    data class Api13OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,
        @Schema(
            description = "검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N14. 이메일 회원가입 본인 확인 이메일에서 받은 코드 검증하기",
        description = "이메일 회원가입시 본인 이메일에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "첫 인증 완료시 이메일 회원가입까지의 만료시간은 10분\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/register-with-email-verification-check")
    fun api14(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "email", description = "확인 이메일", example = "test@gmail.com")
        @RequestParam("email")
        email: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ): Api14OutputVo? {
        return service.api14(httpServletResponse, verificationUid, email, verificationCode)
    }

    data class Api14OutputVo(
        @Schema(
            description = "인증 완료시 새로 늘어난 검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String
    )


    ////
    @Operation(
        summary = "N15 : 이메일 회원가입",
        description = "이메일 회원가입 처리\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 가입된 회원이 있습니다.\n\n" +
                "5 : 이미 사용중인 닉네임",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/register-with-email")
    fun api15(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api15InputVo
    ) {
        service.api15(httpServletResponse, inputVo)
    }

    data class Api15InputVo(
        @Schema(
            description = "아이디 - 이메일",
            required = true,
            example = "test@gmail.com"
        )
        @JsonProperty("email")
        val email: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "사용할 비밀번호",
            required = true,
            example = "kkdli!!"
        )
        @JsonProperty("password")
        val password: String,

        @Schema(
            description = "닉네임",
            required = true,
            example = "홍길동"
        )
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "이메일 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )

    ////
    @Operation(
        summary = "N16. 전화번호 회원가입 본인 인증 문자 발송",
        description = "전화번호 회원가입시 본인 전화번호 확인 문자 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 기존 회원 존재",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/register-with-phone-number-verification")
    fun api16(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api16InputVo
    ): Api16OutputVo? {
        return service.api16(httpServletResponse, inputVo)
    }

    data class Api16InputVo(
        @Schema(description = "인증 문자 수신 전화번호(국가번호 + 전화번호)", required = true, example = "82)010-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String
    )

    data class Api16OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N17. 전화번호 회원가입 본인 확인 문자에서 받은 코드 검증하기",
        description = "전화번호 회원가입시 본인 전화번호에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "첫 인증 완료시 SMS 회원가입까지의 만료시간은 10분\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/register-with-phone-number-verification-check")
    fun api17(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "phoneNumber", description = "인증 문자 수신 전화번호(국가번호 + 전화번호)", example = "82)010-0000-0000")
        @RequestParam("phoneNumber")
        phoneNumber: String,
        @Parameter(name = "verificationCode", description = "확인 문자에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ): Api17OutputVo? {
        return service.api17(httpServletResponse, verificationUid, phoneNumber, verificationCode)
    }

    data class Api17OutputVo(
        @Schema(
            description = "isVerified true 일때 새로 늘어난 검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = false,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String?
    )


    ////
    @Operation(
        summary = "N18. 전화번호 회원가입",
        description = "전화번호 회원가입 처리\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 가입된 회원이 있습니다.\n\n" +
                "5 : 이미 사용중인 닉네임",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/register-with-phone-number")
    fun api18(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api18InputVo
    ) {
        service.api18(httpServletResponse, inputVo)
    }

    data class Api18InputVo(
        @Schema(
            description = "아이디 - 전화번호(국가번호 + 전화번호)",
            required = true,
            example = "82)010-0000-0000"
        )
        @JsonProperty("phoneNumber")
        val phoneNumber: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "사용할 비밀번호",
            required = true,
            example = "kkdli!!"
        )
        @JsonProperty("password")
        val password: String,

        @Schema(
            description = "닉네임",
            required = true,
            example = "홍길동"
        )
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "문자 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N19. OAuth2 AccessToken 으로 회원가입 검증",
        description = "OAuth2 AccessToken 으로 회원가입 검증\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 잘못된 OAuth2 AccessToken\n\n" +
                "2 : 기존 회원 존재",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/register-with-oauth2-access-token-verification")
    fun api19(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api19InputVo
    ): Api19OutputVo? {
        return service.api19(httpServletResponse, inputVo)
    }

    data class Api19InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 OAuth2 TokenType + AccessToken",
            required = true,
            example = "Bearer asdfeqwer1234"
        )
        @JsonProperty("oauth2AccessToken")
        val oauth2AccessToken: String
    )

    data class Api19OutputVo(
        @Schema(
            description = "OAuth2 가입시 검증에 사용할 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("oauth2VerificationCode")
        val oauth2VerificationCode: String,

        @Schema(
            description = "가입에 사용할 OAuth2 고유 아이디",
            required = true,
            example = "abcd1234"
        )
        @JsonProperty("oauth2Id")
        val oauth2Id: String,

        @Schema(
            description = "검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String
    )


    ////
    @Operation(
        summary = "N20. OAuth2 회원가입",
        description = "OAuth2 회원가입 처리\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 기존 회원 존재\n\n" +
                "2 : OAuth2 검증 요청을 보낸 적 없음 혹은 만료된 요청\n\n" +
                "3 : 입력한 verificationCode 와 검증된 code 가 일치하지 않거나 만료된 요청\n\n" +
                "4 : 닉네임 중복",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/register-with-oauth2")
    fun api20(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api20InputVo
    ) {
        service.api20(httpServletResponse, inputVo)
    }

    data class Api20InputVo(
        @Schema(
            description = "가입에 사용할 OAuth2 고유 아이디",
            required = true,
            example = "abcd1234"
        )
        @JsonProperty("oauth2Id")
        val oauth2Id: String,

        @Schema(
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "닉네임",
            required = true,
            example = "홍길동"
        )
        @JsonProperty("nickName")
        val nickName: String,

        @Schema(
            description = "oauth2Id 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N21. 계정 비밀번호 변경 <>",
        description = "계정 비밀번호 변경\n\n" +
                "변경 완료된 후, 기존 모든 인증/인가 토큰을 비활성화 시키고 싶다면 별도의 API 사용하기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 탈퇴된 회원\n\n" +
                "2 : 기존 비밀번호가 일치하지 않음\n\n" +
                "3 : 비번을 null 로 만들려고 할 때 account 외의 OAuth2 인증이 없기에 비번 제거 불가\n\n",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PutMapping("/change-account-password")
    @PreAuthorize("isAuthenticated()")
    fun api21(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api21InputVo
    ) {
        service.api21(httpServletResponse, authorization!!, inputVo)
    }

    data class Api21InputVo(
        @Schema(description = "기존 계정 로그인용 비밀번호(기존 비밀번호가 없다면 null)", required = false, example = "kkdli!!")
        @JsonProperty("oldPassword")
        val oldPassword: String?,

        @Schema(description = "새 계정 로그인용 비밀번호(비밀번호를 없애려면 null)", required = false, example = "fddsd##")
        @JsonProperty("newPassword")
        val newPassword: String?
    )


    ////
    @Operation(
        summary = "N22. 이메일 비밀번호 찾기 본인 인증 이메일 발송",
        description = "이메일 비밀번호 찾기 본인 이메일 확인 메일 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 가입되지 않은 회원",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/find-password-with-email-verification")
    fun api22(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api22InputVo
    ): Api22OutputVo? {
        return service.api22(httpServletResponse, inputVo)
    }

    data class Api22InputVo(
        @Schema(description = "수신 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String
    )

    data class Api22OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,
        @Schema(
            description = "검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N23. 이메일 비밀번호 찾기 본인 확인 이메일에서 받은 코드 검증하기",
        description = "이메일 비밀번호 찾기 시 본인 이메일에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "첫 인증 완료시 비밀번호 찾기 까지의 만료시간은 10분\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/find-password-with-email-verification-check")
    fun api23(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "email", description = "확인 이메일", example = "test@gmail.com")
        @RequestParam("email")
        email: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ): Api23OutputVo? {
        return service.api23(httpServletResponse, verificationUid, email, verificationCode)
    }

    data class Api23OutputVo(
        @Schema(
            description = "isVerified true 일때 새로 늘어난 검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = false,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String?
    )


    ////
    @Operation(
        summary = "N24. 이메일 비밀번호 찾기 완료",
        description = "계정 비밀번호를 랜덤 값으로 변경 후 인증한 이메일로 발송\n\n" +
                "변경 완료된 후, 기존 모든 인증/인가 토큰을 비활성화 시키고 싶다면 별도의 API 사용하기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 탈퇴한 회원입니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/find-password-with-email")
    fun api24(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api24InputVo
    ) {
        service.api24(httpServletResponse, inputVo)
    }

    data class Api24InputVo(
        @Schema(description = "비밀번호를 찾을 계정 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "이메일 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N25. 전화번호 비밀번호 찾기 본인 인증 문자 발송",
        description = "전화번호 비밀번호 찾기 본인 전화번호 확인 문자 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 가입되지 않은 회원",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/find-password-with-phone-number-verification")
    fun api25(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api25InputVo
    ): Api25OutputVo? {
        return service.api25(httpServletResponse, inputVo)
    }

    data class Api25InputVo(
        @Schema(description = "수신 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String
    )

    data class Api25OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N26. 전화번호 비밀번호 찾기 본인 확인 문자에서 받은 코드 검증하기",
        description = "전화번호 비밀번호 찾기 시 본인 전와번호에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "첫 인증 완료시 비밀번호 찾기 까지의 만료시간은 10분\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/find-password-with-phone-number-verification-check")
    fun api26(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "phoneNumber", description = "수신 전화번호", example = "82)000-0000-0000")
        @RequestParam("phoneNumber")
        phoneNumber: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ): Api26OutputVo? {
        return service.api26(httpServletResponse, verificationUid, phoneNumber, verificationCode)
    }

    data class Api26OutputVo(
        @Schema(
            description = "isVerified true 일때 새로 늘어난 검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = false,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String?
    )


    ////
    @Operation(
        summary = "N27. 전화번호 비밀번호 찾기 완료",
        description = "계정 비밀번호를 랜덤 값으로 변경 후 인증한 전화번호로 발송\n\n" +
                "변경 완료된 후, 기존 모든 인증/인가 토큰을 비활성화 시키고 싶다면 별도의 API 사용하기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 탈퇴된 회원",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/find-password-with-phone-number")
    fun api27(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @RequestBody
        inputVo: Api27InputVo
    ) {
        service.api27(httpServletResponse, inputVo)
    }

    data class Api27InputVo(
        @Schema(description = "비밀번호를 찾을 계정 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N28. 내 로그인 관련 정보 리스트 가져오기 <>",
        description = "내 계정에 연결된 로그인 수단 리스트 가져오기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/my-auth-list")
    @PreAuthorize("isAuthenticated()")
    fun api26(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api28OutputVo? {
        return service.api28(httpServletResponse, authorization!!)
    }

    data class Api28OutputVo(
        @Schema(description = "내가 등록한 이메일 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<String>,

        @Schema(description = "내가 등록한 전화번호 리스트", required = true)
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<String>,

        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true)
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2Type")
            val oauth2Type: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )
    }


    ////
    @Operation(
        summary = "N29. 내 이메일 리스트 가져오기 <>",
        description = "내 이메일 리스트 가져오기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/my-email-addresses")
    @PreAuthorize("isAuthenticated()")
    fun api29(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api29OutputVo? {
        return service.api29(httpServletResponse, authorization!!)
    }

    data class Api29OutputVo(
        @Schema(description = "내가 등록한 이메일 리스트", required = true)
        @JsonProperty("myEmailList")
        val myEmailList: List<String>
    )


    ////
    @Operation(
        summary = "N30. 내 전화번호 리스트 가져오기 <>",
        description = "내 전화번호 리스트 가져오기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/my-phone-numbers")
    @PreAuthorize("isAuthenticated()")
    fun api30(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api30OutputVo? {
        return service.api30(httpServletResponse, authorization!!)
    }

    data class Api30OutputVo(
        @Schema(description = "내가 등록한 전화번호 리스트", required = true, example = "true")
        @JsonProperty("myPhoneNumberList")
        val myPhoneNumberList: List<String>
    )


    ////
    @Operation(
        summary = "N31. 내 OAuth2 로그인 리스트 가져오기 <>",
        description = "내 OAuth2 로그인 리스트 가져오기\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/my-oauth2-list")
    @PreAuthorize("isAuthenticated()")
    fun api31(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ): Api31OutputVo? {
        return service.api31(httpServletResponse, authorization!!)
    }

    data class Api31OutputVo(
        @Schema(description = "내가 등록한 OAuth2 정보 리스트", required = true, example = "true")
        @JsonProperty("myOAuth2List")
        val myOAuth2List: List<OAuth2Info>
    ) {
        @Schema(description = "OAuth2 정보")
        data class OAuth2Info(
            @Schema(
                description = "OAuth2 (1 : Google, 2 : Naver, 3 : Kakao)",
                required = true,
                example = "1"
            )
            @JsonProperty("oauth2Type")
            val oauth2Type: Int,
            @Schema(description = "oAuth2 고유값 아이디", required = true, example = "asdf1234")
            @JsonProperty("oauth2Id")
            val oauth2Id: String
        )
    }


    ////
    @Operation(
        summary = "N32. 이메일 추가하기 본인 인증 이메일 발송 <>",
        description = "이메일 추가하기 본인 이메일 확인 메일 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이미 사용중인 이메일",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/add-email-verification")
    @PreAuthorize("isAuthenticated()")
    fun api32(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api32InputVo
    ): Api32OutputVo? {
        return service.api32(httpServletResponse, inputVo, authorization!!)
    }

    data class Api32InputVo(
        @Schema(description = "수신 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String
    )

    data class Api32OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N33. 이메일 추가하기 본인 확인 이메일에서 받은 코드 검증하기 <>",
        description = "이메일 추가하기 본인 이메일에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "첫 인증 완료시 추가하기 까지의 만료시간은 10분\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/add-email-verification-check")
    @PreAuthorize("isAuthenticated()")
    fun api33(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "email", description = "확인 이메일", example = "test@gmail.com")
        @RequestParam("email")
        email: String,
        @Parameter(name = "verificationCode", description = "확인 이메일에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ): Api33OutputVo? {
        return service.api33(httpServletResponse, verificationUid, email, verificationCode, authorization!!)
    }

    data class Api33OutputVo(
        @Schema(
            description = "isVerified true 일때 새로 늘어난 검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = false,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String?
    )


    ////
    @Operation(
        summary = "N34. 이메일 추가하기 <>",
        description = "내 계정에 이메일 추가\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 사용중인 이메일",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/my-new-email")
    @PreAuthorize("isAuthenticated()")
    fun api34(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api34InputVo
    ) {
        service.api34(httpServletResponse, inputVo, authorization!!)
    }

    data class Api34InputVo(
        @Schema(description = "추가할 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "이메일 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N35. 내 이메일 제거하기 <>",
        description = "내 계정에서 이메일 제거\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 제거할 수 없습니다. (이외에 로그인 할 방법이 없음)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @DeleteMapping("/my-email")
    @PreAuthorize("isAuthenticated()")
    fun api35(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api35InputVo
    ) {
        service.api35(httpServletResponse, inputVo, authorization!!)
    }

    data class Api35InputVo(
        @Schema(description = "제거할 이메일", required = true, example = "test@gmail.com")
        @JsonProperty("email")
        val email: String
    )


    ////
    @Operation(
        summary = "N36. 전화번호 추가하기 본인 인증 문자 발송 <>",
        description = "전화번호 추가하기 본인 전화번호 확인 문자 발송\n\n" +
                "발송 후 10분 후 만료됨\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이미 사용중인 전화번호",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/add-phone-number-verification")
    @PreAuthorize("isAuthenticated()")
    fun api36(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api36InputVo
    ): Api36OutputVo? {
        return service.api36(httpServletResponse, inputVo, authorization!!)
    }

    data class Api36InputVo(
        @Schema(description = "수신 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String
    )

    data class Api36OutputVo(
        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = true,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("verificationExpireWhen")
        val verificationExpireWhen: String
    )


    ////
    @Operation(
        summary = "N37. 전화번호 추가하기 본인 확인 문자에서 받은 코드 검증하기 <>",
        description = "전화번호 추가하기 본인 전화번호에 보내진 코드를 입력하여 일치 결과 확인\n\n" +
                "첫 인증 완료시 추가하기 까지의 만료시간은 10분\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 전화번호 검증 요청을 보낸 적 없음\n\n" +
                "2 : 전화번호 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @GetMapping("/add-phone-number-verification-check")
    @PreAuthorize("isAuthenticated()")
    fun api37(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @Parameter(name = "verificationUid", description = "검증 고유값", example = "1")
        @RequestParam("verificationUid")
        verificationUid: Long,
        @Parameter(name = "phoneNumber", description = "수신 전화번호", example = "82)000-0000-0000")
        @RequestParam("phoneNumber")
        phoneNumber: String,
        @Parameter(name = "verificationCode", description = "확인 문자에 전송된 코드", example = "123456")
        @RequestParam("verificationCode")
        verificationCode: String
    ): Api37OutputVo? {
        return service.api37(httpServletResponse, verificationUid, phoneNumber, verificationCode, authorization!!)
    }

    data class Api37OutputVo(
        @Schema(
            description = "isVerified true 일때 새로 늘어난 검증 만료 시간 (yyyy-MM-dd HH:mm:ss.SSS)",
            required = false,
            example = "2023-01-02 11:11:11.111"
        )
        @JsonProperty("expireWhen")
        val expireWhen: String?
    )

    ////
    @Operation(
        summary = "N38. 전화번호 추가하기 <>",
        description = "내 계정에 전화번호 추가\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 이메일 검증 요청을 보낸 적 없음\n\n" +
                "2 : 이메일 검증 요청이 만료됨\n\n" +
                "3 : verificationCode 가 일치하지 않음\n\n" +
                "4 : 이미 사용중인 전화번호",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/my-new-phone-number")
    @PreAuthorize("isAuthenticated()")
    fun api38(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api38InputVo
    ) {
        service.api38(httpServletResponse, inputVo, authorization!!)
    }

    data class Api38InputVo(
        @Schema(description = "추가할 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String,

        @Schema(
            description = "검증 고유값",
            required = true,
            example = "1"
        )
        @JsonProperty("verificationUid")
        val verificationUid: Long,

        @Schema(
            description = "문자 검증에 사용한 코드",
            required = true,
            example = "123456"
        )
        @JsonProperty("verificationCode")
        val verificationCode: String
    )


    ////
    @Operation(
        summary = "N39. 내 전화번호 제거하기 <>",
        description = "내 계정에서 전화번호 제거\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 제거할 수 없습니다. (이외에 로그인 할 방법이 없음)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @DeleteMapping("/my-phone-number")
    @PreAuthorize("isAuthenticated()")
    fun api39(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api39InputVo
    ) {
        service.api39(httpServletResponse, inputVo, authorization!!)
    }

    data class Api39InputVo(
        @Schema(description = "제거할 전화번호", required = true, example = "82)000-0000-0000")
        @JsonProperty("phoneNumber")
        val phoneNumber: String
    )


    ////
    @Operation(
        summary = "N40. OAuth2 추가하기 (Access Token) <>",
        description = "내 계정에 OAuth2 Access Token 으로 인증 추가\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : oAuth2 Access Token 정보 검증 불일치\n\n" +
                "2 : 탈퇴된 회원\n\n" +
                "3 : 이미 사용중인 인증",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @PostMapping("/my-new-oauth2-token")
    @PreAuthorize("isAuthenticated()")
    fun api40(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api40InputVo
    ) {
        service.api40(httpServletResponse, inputVo, authorization!!)
    }

    data class Api40InputVo(
        @Schema(
            description = "OAuth2 종류 코드 (1 : GOOGLE, 2 : NAVER, 3 : KAKAO)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2TypeCode")
        val oauth2TypeCode: Int,

        @Schema(
            description = "OAuth2 인증으로 받은 oauth2 TokenType + AccessToken",
            required = true,
            example = "Bearer asdfeqwer1234"
        )
        @JsonProperty("oauth2AccessToken")
        val oauth2AccessToken: String
    )


    ////
    @Operation(
        summary = "N41. 내 OAuth2 제거하기 <>",
        description = "내 계정에서 OAuth2 제거\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 제거할 수 없습니다. (이외에 로그인 할 방법이 없음)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @DeleteMapping("/my-oauth2")
    @PreAuthorize("isAuthenticated()")
    fun api41(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?,
        @RequestBody
        inputVo: Api41InputVo
    ) {
        service.api41(httpServletResponse, inputVo, authorization!!)
    }

    data class Api41InputVo(
        @Schema(
            description = "제거할 oAuth2 종류 (1 : Google, 2 : Naver, 3 : Kakao)",
            required = true,
            example = "1"
        )
        @JsonProperty("oauth2Type")
        val oauth2Type: Int,
        @Schema(description = "제거할 oAuth2 고유값", required = true, example = "asdf1234")
        @JsonProperty("oauth2Id")
        val oauth2Id: String
    )


    ////
    @Operation(
        summary = "N42. 회원탈퇴 <>",
        description = "회원탈퇴 요청\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK"
            )
        ]
    )
    @DeleteMapping("/withdrawal")
    @PreAuthorize("isAuthenticated()")
    fun api42(
        @Parameter(hidden = true)
        httpServletResponse: HttpServletResponse,
        @Parameter(hidden = true)
        @RequestHeader("Authorization")
        authorization: String?
    ) {
        service.api42(httpServletResponse, authorization!!)
    }
}