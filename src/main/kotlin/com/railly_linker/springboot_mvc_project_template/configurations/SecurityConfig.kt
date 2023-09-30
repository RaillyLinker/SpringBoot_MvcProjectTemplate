package com.railly_linker.springboot_mvc_project_template.configurations

import com.railly_linker.springboot_mvc_project_template.ApplicationConstants
import com.railly_linker.springboot_mvc_project_template.util_objects.JwtTokenUtilObject
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

// (서비스 보안 시큐리티 설정)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    private val authTokenFilterService1Tk: AuthTokenFilterService1Tk,
    // todo
//    private val customDefaultOauth2MemberService: CustomDefaultOauth2MemberService
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (비밀번호 인코딩, 매칭시 사용할 객체)
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // [/service1/tk 로 시작되는 리퀘스트의 시큐리티 설정 = Token 인증 사용]
    @Bean
    @Order(1)
    fun securityFilterChainService1Tk(http: HttpSecurity): SecurityFilterChain {
        // 본 시큐리티 필터가 관리할 주소 체계
        val securityUrl = "/service1/tk/**" // /service1/tk/** 의 모든 경로에 적용

        // (사이즈간 위조 요청(Cross site Request forgery) 방지 설정)
        // csrf 설정시 POST, PUT, DELETE 요청으로부터 보호하며 csrf 토큰이 포함되어야 요청을 받아들이게 됨
        // Rest API 에선 Token 이 요청의 위조 방지 역할을 하기에 비활성화
        http.securityMatcher(securityUrl)
            .csrf { csrfCustomizer ->
                csrfCustomizer.disable()
            }

        http.securityMatcher(securityUrl)
            .httpBasic { httpBasicCustomizer ->
                httpBasicCustomizer.disable()
            }

        // Token 인증을 위한 세션 비활성화
        http.securityMatcher(securityUrl)
            .sessionManagement { sessionManagementCustomizer ->
                sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        // (Token 인증 검증 필터 연결)
        // API 요청마다 헤더로 들어오는 인증 토큰 유효성을 검증
        http.securityMatcher(securityUrl)
            .addFilterBefore(authTokenFilterService1Tk, UsernamePasswordAuthenticationFilter::class.java)

        // 스프링 시큐리티 기본 로그인 화면 비활성화
        http.securityMatcher(securityUrl)
            .formLogin { formLoginCustomizer ->
                formLoginCustomizer.disable()
            }

        // 스프링 시큐리티 기본 로그아웃 비활성화
        http.securityMatcher(securityUrl)
            .logout { logoutCustomizer ->
                logoutCustomizer.disable()
            }

        // 예외처리
        http.securityMatcher(securityUrl)
            .exceptionHandling { exceptionHandlingCustomizer ->
                // 비인증(Security Context 에 멤버 정보가 없음) 처리
                exceptionHandlingCustomizer.authenticationEntryPoint { _, response, _ -> // Http Status 401
                    response.setHeader("api-result-code", "a")
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: UnAuthorized")
                }
                // 비인가(멤버 권한이 충족되지 않음) 처리
                exceptionHandlingCustomizer.accessDeniedHandler { _, response, _ -> // Http Status 403
                    response.setHeader("api-result-code", "b")
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Forbidden")
                }
            }

        // (API 요청 제한)
        // 기본적으로 모두 Open
        http.securityMatcher(securityUrl)
            .authorizeHttpRequests { authorizeHttpRequestsCustomizer ->
                authorizeHttpRequestsCustomizer.anyRequest().permitAll()
                // !!!접근 보안 블랙 리스트 방식
                // @PreAuthorize("isAuthenticated() and (hasRole('ROLE_DEVELOPER') or hasRole('ROLE_ADMIN'))")
                // 위와 같은 어노테이션을 접근 통제하고자 하는 API 위에 작성!!
            }

        return http.build()
    }

    // (인증 토큰 검증 필터)
    // API 요청마다 검증 실행
    // 주의 :
    // 본 프로젝트의 JWT 인증 / 인가 시스템은 세션 인증과 대비되는 토큰 인증의 장점을 최대한 살리기 위하여,
    // 검증 시점에 데이터베이스 접근을 하지 않으며, 검증 정보를 메모리에 저장하지 않습니다.
    // 즉, 토큰 검증은 발행 된 시점에 토큰 안에 넣어준 바로 그 정보만을 기준으로 하여 판단됩니다.
    // 예를들어 토큰 발행 시점에 Admin 권한이 있었는데, 서버측에서 이 권한을 취소하여도 토큰만 정상적이라면 여전히 Admin 권한을 가집니다.
    // 이에 대해 서버측에서 할 수 있는 것은, 액세스 토큰이 만료된 이후에 재발급 시점에 이를 판단하여 처리하는 방법,
    // 혹은 SSE 등으로 클라이언트에 신호를 보내어 해당 위치에서 처리를 하도록 하는 방법 밖에는 없습니다.
    // 되도록 액세스 토큰 만료시간을 짧게 잡고(15분 ~ 1시간), 클라이언트 측에서 판단하여 처리할 수 있는 부분은 클라이언트에서 처리하도록 합니다.
    @Component
    class AuthTokenFilterService1Tk : OncePerRequestFilter() {
        // <멤버 변수 공간>
        companion object {
            // (JWT signature 비밀키)
            // !!!비밀키 변경!!
            const val JWT_SECRET_KEY_STRING = "123456789abcdefghijklmnopqrstuvw"

            // (액세스 토큰 유효시간)
            // !!!유효시간 변경!!
            const val ACCESS_TOKEN_EXPIRATION_TIME_MS = 1000L * 60L * 30L // 30분

            // (리프레시 토큰 유효시간)
            // !!!유효시간 변경!!
            const val REFRESH_TOKEN_EXPIRATION_TIME_MS = 1000L * 60L * 60L * 24L * 7L // 7일

            // (JWT Claims 암호화 AES 키)
            // !!!암호키 변경!!
            const val JWT_CLAIMS_AES256_INITIALIZATION_VECTOR: String = "odkejduc726dj48d" // 16자
            const val JWT_CLAIMS_AES256_ENCRYPTION_KEY: String = "8fu3jd0ciiu3384hfucy36dye9sjv7b3" // 32자

            // (JWT 발행자)
            const val ISSUER = "${ApplicationConstants.PACKAGE_NAME}.service1"
        }

        // ---------------------------------------------------------------------------------------------
        // <공개 메소드 공간>
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
        ) {
            // (리퀘스트에서 가져온 AccessToken 검증)
            // 헤더의 Authorization 의 값 가져오기
            // 정상적인 토큰값은 "Bearer {Token String}" 형식으로 온다고 가정.
            val authorization = request.getHeader("Authorization") // ex : "Bearer aqwer1234"

            if (authorization != null) {
                // 타입과 토큰을 분리
                val authorizationSplit = authorization.split(" ") // ex : ["Bearer", "qwer1234"]

                if (authorizationSplit.size >= 2) { // 타입으로 추정되는 문장이 존재할 때
                    // 타입 분리
                    val accessTokenType = authorizationSplit[0].trim() // 첫번째 단어는 토큰 타입

                    when (accessTokenType.lowercase()) { // 타입 검증
                        "bearer" -> { // Bearer JWT 토큰 검증
                            val jwtAccessToken = authorizationSplit[1].trim() // 앞의 타입을 자르고 남은 액세스 토큰

                            // 토큰 문자열 해석 가능여부 확인
                            val tokenType: String? = try {
                                JwtTokenUtilObject.getTokenType(jwtAccessToken)
                            } catch (_: Exception) {
                                null
                            }

                            if (tokenType != null && // 해석 가능한 JWT 토큰이라는 뜻
                                jwtAccessToken != "" && // 액세스 토큰이 비어있지 않음
                                tokenType.lowercase() == "jwt" && // 토큰 타입 JWT
                                JwtTokenUtilObject.getTokenUsage(
                                    jwtAccessToken,
                                    JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                                    JWT_CLAIMS_AES256_ENCRYPTION_KEY
                                ).lowercase() == "access" && // 토큰 용도 확인
                                JwtTokenUtilObject.getIssuer(jwtAccessToken) == ISSUER && // 발행인 동일
                                JwtTokenUtilObject.validateSignature(
                                    jwtAccessToken,
                                    JWT_SECRET_KEY_STRING
                                ) // 시크릿 검증이 유효 = 위변조 되지 않은 토큰
                            ) {
                                // 토큰 만료 검증
                                val jwtRemainSeconds = JwtTokenUtilObject.getRemainSeconds(jwtAccessToken)

                                // 특정 request 에는 만료 필터링을 적용시키지 않음 (토큰 유효성 검증은 통과된 상황)
                                if (jwtRemainSeconds > 0L) { // 만료 검증 통과
                                    val memberRoleList = JwtTokenUtilObject.getMemberRoleList(
                                        jwtAccessToken,
                                        JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
                                        JWT_CLAIMS_AES256_ENCRYPTION_KEY
                                    )

                                    // 회원 권한 형식 변경
                                    val authorities: ArrayList<GrantedAuthority> = ArrayList()
                                    for (role in memberRoleList) {
                                        authorities.add(
                                            SimpleGrantedAuthority(role)
                                        )
                                    }

                                    // (검증된 멤버 정보와 권한 정보를 Security Context 에 입력)
                                    // authentication 정보가 context 에 존재하는지 여부로 로그인 여부를 확인
                                    SecurityContextHolder.getContext().authentication =
                                        UsernamePasswordAuthenticationToken(
                                            null, // 세션을 유지하지 않으니 굳이 입력할 필요가 없음
                                            null, // 세션을 유지하지 않으니 굳이 입력할 필요가 없음
                                            authorities // 멤버 권한 리스트만 입력해주어 권한 확인에 사용
                                        ).apply {
                                            this.details =
                                                WebAuthenticationDetailsSource().buildDetails(request)
                                        }
                                } else { // 액세스 토큰 만료
                                    response.setHeader("api-result-code", "d")
                                }
                            } else {
                                // 올바르지 않은 Authorization Token
                                response.setHeader("api-result-code", "c")
                            }
                        }

                        else -> {
                            // 올바르지 않은 Authorization Token
                            response.setHeader("api-result-code", "c")
                        }
                    }
                } else {
                    // 올바르지 않은 Authorization Token
                    response.setHeader("api-result-code", "c")
                }
            }

            // 필터 체인 실행
            //  : 정상 로그인시 Security Context 에 정보가 있고, 아니라면 없으므로 unAuthorized 상태
            filterChain.doFilter(request, response)
        }


        // ---------------------------------------------------------------------------------------------
        // <비공개 메소드 공간>


        // ---------------------------------------------------------------------------------------------
        // <중첩 클래스 공간>

    }

    // todo
    // todo : Session 사용시의 Security 동작 확인 필요
    // [그외 모든 리퀘스트의 시큐리티 설정 = Session Cookie 사용]
//    @Bean
//    @Order(2)
//    fun securityFilterChainAll(http: HttpSecurity): SecurityFilterChain {
//        // (사이즈간 위조 요청(Cross site Request forgery) 방지 설정)
//        // : true 시 POST, PUT, DELETE 요청으로부터 보호하며 csrf 토큰이 포함되어야 요청을 받아들이게 됨
//        //     CSRF 토큰이 없다면 권한 없음 에러(403)가 발생
//        //     서버사이드 타임리프 웹페이지는 자동으로 토큰이 생성되서 신경쓸 필요가 없음.
//        //     다시 말하지만 타임리프같은 서버사이드 랜더링을 제외한 RestAPI 를 사용하는 요청은
//        //     무조건 tk/** 주소를 사용하여 Token 인증을 사용할것
//
//        // AJAX 비동기 요청시 CSRF 토큰 헤더 설정 예시
//        // $.ajaxPrefilter(function (options) {
//        //  var headerName = '${_csrf.headerName}';
//        //  var token = '${_csrf.token}';
//        //  if (options.method === 'POST') {
//        //      options.headers = options.headers || {};
//        //      options.headers[headerName] = token;
//        //  }
//        // });
//
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .csrf()
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            .ignoringRequestMatchers( // CSRF 필터링 무시 목록 적용
//                object : RequestMatcher {
//                    override fun matches(request: HttpServletRequest?): Boolean {
//
//                        // CSRF 에 취약하지 않은 단순 조회 메소드는 허용
//                        val allowMethod = arrayOf("GET", "OPTIONS")
//                        val method = request!!.method
//                        for (allowedMethod in allowMethod) {
//                            if (allowedMethod.equals(method, ignoreCase = true)) {
//                                return true
//                            }
//                        }
//
//                        // 특정 조건에서 CSRF 필터링을 하지 않으려면 조건문을 달아 true 를 리턴하면 됨
//
//                        return false
//                    }
//                }
//            )
//
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .cors() // cors 설정 : authorizeRequests, corsConfigurationSource Bean과 연관
//
//        // (email form 로그인)
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .formLogin()
//            // !!!Session Login Path 설정!!
//            .loginPage("/sc/wp/auth/login") // 인증이 필요한 URL 에 접근시 이동할 위치
//            .usernameParameter("loginId") // 로그인 시 form 에서 가져올 아이디 name (= email)
//            .passwordParameter("loginPw") // 로그인 시 form 에서 가져올 비밀번호 name
//            .loginProcessingUrl("/sc/wp/auth/login-process") // 로그인 처리 위치의 path
//            .successHandler { request, response, _ ->
//                // !!!로그인 성공 후 핸들러!!
//                // 로그인 페이지 진입 이전 페이지로 이동
//                // 인증 이전에 위치했던 페이지는 인증 후에 체류해도 위험하지 않다고 판단.
//                val prevPageString = request.session.getAttribute("prevPage")?.toString() ?: "/"
//                response.sendRedirect(prevPageString)
//            }
//            .failureHandler { _, response, exception ->
//                // !!!로그인 실패 후 핸들러!!
//                when (exception) {
//                    is SessionAuthenticationException -> { // 동시 접속 세션 초과
//                        // 로그인 화면에 표시
//                        response.sendRedirect("/sc/wp/auth/login?maximumSessionOver=true")
//                    }
//
//                    is BadCredentialsException -> { // 아이디 혹은 비번이 다름
//                        // 로그인 화면에 표시
//                        response.sendRedirect("/sc/wp/auth/login?loginNotMatched=true")
//                    }
//
//                    else -> { // 그외 에러
//                        exception.printStackTrace()
//                    }
//                }
//            }
//
//        // (OAuth2 로그인)
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .oauth2Login()
//            // todo : 위에서 설정했으니 여기선 삭제해보기
//            // !!!Session Login Path 설정!!
//            .loginPage("/sc/wp/auth/login") // 인증이 필요한 URL 에 접근시 이동할 위치
//            .successHandler { request, response, _ ->
//                // !!!로그인 성공 후 핸들러!!
//                // 로그인 페이지 진입 이전 페이지로 이동
//                // 인증 이전에 위치했던 페이지는 인증 후에 체류해도 위험하지 않다고 판단.
//                val prevPageString = request.session.getAttribute("prevPage")?.toString() ?: "/"
//                response.sendRedirect(prevPageString)
//            }
//            .failureHandler { _, response, exception ->
//                // !!!로그인 실패 후 핸들러!!
//                when (exception) {
//                    is SessionAuthenticationException -> { // 동시 접속 세션 초과
//                        // 로그인 화면에 표시
//                        response.sendRedirect("/sc/wp/auth/login?maximumSessionOver=true")
//                    }
//
//                    is BadCredentialsException -> { // 아이디 혹은 비번이 다름
//                        // 로그인 화면에 표시
//                        response.sendRedirect("/sc/wp/auth/login?loginNotMatched=true")
//                    }
//
//                    else -> { // 그외 에러
//                        exception.printStackTrace()
//                    }
//                }
//            }
//            .userInfoEndpoint()
//            .userService(customDefaultOauth2MemberService)
//
//        // (로그아웃)
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .logout()
//            // !!!Session Logout Path 설정!!
//            .logoutUrl("/sc/wp/auth/logout-process") // 로그아웃 처리 위치
//            .logoutSuccessHandler { _, response, _ ->
//                // !!!로그아웃 성공후 핸들러!!
//                // 루트 경로로 돌아가기
//                // 인증 이후에 위치했던 페이지가 인증 해제 이후에 체류했을 때 위험할 수 있다고 판단.
//                response.sendRedirect("/")
//            }
//
//        // 예외처리
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .exceptionHandling()
//            .authenticationEntryPoint { _, response, _ ->
//                // !!!Http Status 401 일때의 처리!!
//                response.sendRedirect("/sc/wp/auth/login")
//            }
//            .accessDeniedHandler { _, response, exception ->
//                if (exception is MissingCsrfTokenException || exception is InvalidCsrfTokenException) {
//                    // !!!csrf 토큰 없음 or csrf 토큰이 다름!!
//                    response.sendRedirect("/sc/wp/error?type=ERR_CSRF")
//                } else { // !!!Http Status 403!!
//                    response.sendRedirect("/sc/wp/error?type=ERR_ACCESS_DENIED")
//                }
//            }
//
//        // (세션 최대 접속수 제한 설정)
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .sessionManagement()
//            .maximumSessions(SAME_MEMBER_SIGN_IN_COUNT) // 최대 접속수를 1개로 제한한다.
//            .maxSessionsPreventsLogin(SAME_MEMBER_SIGN_IN_OVER_POLICY == 0) // true : 새로운 로그인 금지, false : 기존 로그인 만료시키기
//            // !!!기존 세션이 만료 되었을 경우 리다이렉트 할 URL 설정!!
//            .expiredUrl("/sc/wp/auth/login?expire=true")
//            .sessionRegistry(sessionRegistry())
//
//        // (동일 origin 내에서는 내 웹페이지가 iframe 내에 표시 가능하도록 처리)
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .headers().frameOptions().sameOrigin()
//
//        // (API 요청 제한)
//        // 기본적으로 모두 Open
//        http.securityMatcher("/**") // /service1/tk/** 외 모든 경로에 적용
//            .authorizeHttpRequests { authorizeHttpRequestsCustomizer ->
//                // 인증 및 인가 조건 설정
//                authorizeHttpRequestsCustomizer.anyRequest().permitAll()
//                // !!!접근 보안 블랙 리스트 방식
//                // @PreAuthorize("isAuthenticated() and (hasRole('ROLE_DEVELOPER') or hasRole('ROLE_ADMIN'))")
//                // 위와 같은 어노테이션을 접근 통제하고자 하는 API 위에 작성!!
//            }
//
//        return http.build()
//    }
//
//    @Bean
//    fun sessionRegistry(): SessionRegistry? {
//        return SessionRegistryImpl()
//    }
//
//    @Bean
//    fun httpSessionEventPublisher(): ServletListenerRegistrationBean<*>? {
//        return ServletListenerRegistrationBean(HttpSessionEventPublisher())
//    }
//
//    @Bean
//    fun httpSessionListener(): HttpSessionListener? {
//        return object : HttpSessionListener {
//            override fun sessionCreated(httpSessionEvent: HttpSessionEvent) {
//                // 동작이 없을 때의 세션 유효시간 (초 단위, 기본값 1800초 = 30분)
//                httpSessionEvent.session.maxInactiveInterval = 1800
//            }
//
//            override fun sessionDestroyed(se: HttpSessionEvent?) {}
//        }
//    }
//
//    // (세션 입력용 멤버 정보 클래스)
//    // 세션 로그인시 클래스를 인스턴스화 하여 MemberService 로 반환하면 해당 멤버에 대한 세션이 생성됩니다.
//    class CustomMemberDetails : UserDetails, OAuth2User {
//        // <static 공간>
//        companion object {
//            // Email Login 에 사용
//            fun buildObject(
//                memberUid: String, // DB 저장 고유값
//                password: String?,
//                memberRoles: List<GrantedAuthority>
//            ): CustomMemberDetails {
//                val customMemberDetails = CustomMemberDetails()
//                customMemberDetails.memberId = memberUid
//                customMemberDetails.accountPassword = password
//                customMemberDetails.grantedAuthorityList.addAll(memberRoles)
//
//                return customMemberDetails
//            }
//
//            // SNS Login 에 사용
//            fun buildObject(
//                memberUid: String, // DB 저장 고유값
//                memberRoles: List<GrantedAuthority>,
//                oAuth2Attributes: MutableMap<String, Any>
//            ): CustomMemberDetails {
//                val customMemberDetails = CustomMemberDetails()
//                customMemberDetails.memberId = memberUid
//                customMemberDetails.oAuth2Attributes = oAuth2Attributes
//                customMemberDetails.grantedAuthorityList.addAll(memberRoles)
//
//                return customMemberDetails
//            }
//        }
//
//        lateinit var memberId: String
//        var accountPassword: String? = null
//        var oAuth2Attributes: MutableMap<String, Any> = hashMapOf()
//        val grantedAuthorityList: ArrayList<GrantedAuthority> = ArrayList()
//
//        override fun getUsername(): String { // 여기서 반환하는 것은 멤버 고유번호
//            return memberId
//        }
//
//        override fun getPassword(): String {
//            return accountPassword.toString()
//        }
//
//        override fun getName(): String { // 여기서 반환하는 것은 멤버 고유번호
//            return memberId
//        }
//
//        override fun getAttributes(): MutableMap<String, Any> {
//            return oAuth2Attributes
//        }
//
//        @JsonIgnore
//        override fun getAuthorities(): Collection<GrantedAuthority> {
//            return grantedAuthorityList
//        }
//
//        // [아래 정보들은 시큐리티에서 사용하지 않음 아래 개념을 사용하려면 서비스의 로그인 검증 로직에서 다룰것]
//        // (계정 만료 여부)
//        @JsonIgnore
//        override fun isAccountNonExpired(): Boolean {
//            return true
//        }
//
//        // (계정 잠김 여부)
//        @JsonIgnore
//        override fun isAccountNonLocked(): Boolean {
//            return true
//        }
//
//        // (비번 만료 여부)
//        @JsonIgnore
//        override fun isCredentialsNonExpired(): Boolean {
//            return true
//        }
//
//        // (계정 사용 가능 여부)
//        @JsonIgnore
//        override fun isEnabled(): Boolean {
//            return true
//        }
//
//        // 동시 세션 제한을 하려면 아래의 equals 와 hashCode 를 구현해야함
//        override fun equals(other: Any?): Boolean {
//            if (other is UserDetails) {
//                return this.username == other.username
//            }
//            return false
//        }
//
//        override fun hashCode(): Int {
//            return this.username.hashCode()
//        }
//    }
//
//    // (form sign in 의 이메일 로그인 정보가 입력된 경우 실행됨)
//    // memberId 가 전달되어 DB 에 저장된 실제 정보로 만들어지 UserDetail 이 반환되고, 이 정보를 기반으로 내부적으로 검증이 수행됨
//    @Service
//    class CustomMemberDetailService(
//        private val database1MemberRepository: Database1_Member_MemberRepository,
//        private val database1MemberEmailRepository: Database1_Member_MemberEmailRepository,
//        private val database1MemberRoleRepository: Database1_Member_MemberRoleRepository
//    ) : UserDetailsService {
//        // Session 인증시 formLogin 에 설정한 loginProcessingUrl 이 실행되면,
//        // html form 으로 가져온 userName 으로 DB 에서 검색하여 가져온 password 등의 정보로 UserDetails 를 만들어 반환하는 콜백 함수
//        // 여기서 반환된 정보를 loginProcessingUrl 에서 html form 으로 받아온 password 와 비교 및 userRole 을 비교하여 인증/인가
//        @Throws(UsernameNotFoundException::class)
//        override fun loadUserByUsername(loginId: String): UserDetails {
//            val memberEmail: Database1_Member_MemberEmail? =
//                database1MemberEmailRepository.findByEmailAddressAndRowActivate(
//                    loginId,
//                    true
//                )
//
//            return if (memberEmail == null) {
//                // 등록된 이메일이 없음 == 회원가입 안됨
//                // todo 에러 핸들러에서 핸들링이 가능한지 확인하여 처리
//                throw UsernameNotFoundException("UsernameNotFound")
//            } else {
//                val member = database1MemberRepository.findByUidAndRowActivate(
//                    memberEmail.memberUid, true
//                )
//
//                if (member == null) {
//                    // 등록된 회원이 없음 == 회원가입 안됨
//                    // todo 에러 핸들러에서 핸들링이 가능한지 확인하여 처리
//                    throw UsernameNotFoundException("UsernameNotFound")
//                } else {
//                    // 회원 권한 가져오기
//                    val memberRole =
//                        database1MemberRoleRepository.findAllByMemberUidAndRowActivate(
//                            memberEmail.memberUid, true
//                        )
//                    // 회원 권한 형식 변경
//                    val authorities: ArrayList<GrantedAuthority> = ArrayList()
//                    for (roleInfo in memberRole) {
//                        authorities.add(
//                            SimpleGrantedAuthority(
//                                when (roleInfo.roleCode) {
//                                    1 -> "ROLE_ADMIN"
//                                    2 -> "ROLE_DEVELOPER"
//                                    else -> throw Exception()
//                                }
//                            )
//                        )
//                    }
//
//                    CustomMemberDetails.buildObject(
//                        memberEmail.memberUid.toString(),
//                        member.accountPassword,
//                        authorities
//                    )
//                }
//            }
//        }
//    }
//
//    // (Oauth2 의 SNS 로그인 정보가 입력된 경우 실행됨)
//    // OAuth2 정보가 전달되어 DB 에 저장된 실제 정보로 만들어지 UserDetail 이 반환되고, 이 정보를 기반으로 내부적으로 검증이 수행됨
//    // Spring Security 에서 지원하는 모든 OAuth2 인증 인가 설정은 application.yml 의 spring:security:oauth2:client: 부분과,
//    // SecurityConfig.kt 의 oauth2Login 부분, CustomDefaultOauth2UserService 부분 내에만 집약됨
//
//    // (Oauth2 구글 로그인 서비스 설정)
//    //     https://console.cloud.google.com/welcome?project=prowdtemplate
//    //     위 링크에서 프로젝트 생성 및 리디렉션 URL 을 http://127.0.0.1:8080/login/oauth2/code/google 로 입력
//    //     /login/oauth2/code/google 는 spring security 기본 리디렉션 경로이므로 그대로 입력
//    //     외부 접속 경로의 경우는 각 서버별 경로를 추가시킬것.
//    //     클라이언트 ID와 클라이언트 보안 비밀번호를 기록해두고, application.yml 에
//    //
//    //spring:
//    //  security:
//    //    oauth2:
//    //      client:
//    //        registration:
//    //          google: # spring security oauth2 설정 정보
//    //            clientId: 1234.apps.googleusercontent.com
//    //            clientSecret: 12345678
//    //            scope:
//    //              - email
//    //              - profile
//    //
//    //     위와 같이 입력(clientId, clientSecret 는 위에서 기록했던 것을 입력하고, scope 는 설정한 스코프를 입력)
//    //     th:onclick="|location.href='@{/oauth2/authorization/google}'|" 와 같이 /oauth2/authorization/google 경로로 이동하면,
//    //     구글 로그인 화면으로 이동했다가 로그인이 완료되면 로그인 정보를 가지고 CustomDefaultOauth2UserService 의 loadUser 가 실행됨
//
//    // (Oauth2 네이버 로그인 서비스 설정)
//    //     https://developers.naver.com/apps/#/list
//    //     위 링크에서 프로젝트 생성
//    //     서비스 URL 은 http://127.0.0.1, Callback URL 을 http://127.0.0.1:8080/login/oauth2/code/naver 로 입력
//    //     /login/oauth2/code/naver 는 spring security 기본 리디렉션 경로이므로 그대로 입력
//    //     외부 접속 경로의 경우는 각 서버별 경로를 추가시킬것.
//    //     클라이언트 ID와 클라이언트 보안 비밀번호를 기록해두고, application.yml 에
//    //
//    //spring:
//    //  security:
//    //    oauth2:
//    //      client:
//    //        registration:
//    //          naver: # spring security oauth2 설정 정보
//    //            client-id: GM1MR5AmXmEWkia_Yumw
//    //            client-secret: wpEaHRJ1zY
//    //            scope:
//    //              - email
//    //            client-name: Naver
//    //            authorization-grant-type: authorization_code # oauth2의 동작 방식 중 코드를 응답받는 방식으로 진행한다는 의미
//    //            redirect-uri: http://127.0.0.1:8080/login/oauth2/code/naver # 배포 프로필별 따로 작성
//    //
//    //        provider:
//    //          naver:
//    //            authorization-uri: https://nid.naver.com/oauth2.0/authorize
//    //            token-uri: https://nid.naver.com/oauth2.0/token
//    //            user-info-uri: https://openapi.naver.com/v1/nid/me
//    //            user-name-attribute: response # 회원정보를 json 으로 받는데 response 라는 키 값으로 리턴
//    //
//    //     위와 같이 입력(client-id, client-secret 는 위에서 기록했던 것을 입력하고, scope 는 설정한 스코프를 입력)
//    //     th:onclick="|location.href='@{/oauth2/authorization/naver}'|" 와 같이 /oauth2/authorization/naver 경로로 이동하면,
//    //     네이버 로그인 화면으로 이동했다가 로그인이 완료되면 로그인 정보를 가지고 CustomDefaultOauth2UserService 의 loadUser 가 실행됨
//
//    // (Oauth2 카카오 로그인 서비스 설정)
//    //     https://developers.kakao.com/
//    //     위 링크에서 프로젝트 생성
//    //     플랫폼 - Web 사이트 도메인은 http://127.0.0.1, Redirect URI 설정은 http://127.0.0.1:8080/login/oauth2/code/kakao 로 입력
//    //     /login/oauth2/code/kakao 는 spring security 기본 리디렉션 경로이므로 그대로 입력
//    //     외부 접속 경로의 경우는 각 서버별 경로를 추가시킬것.
//    //     요약정보에서 REST API 키 가 클라이언트 ID 이며, 카카오는 클라이언트 시크릿 설정시 에러가 나므로 놔두고, application.yml 에
//    //
//    //spring:
//    //  security:
//    //    oauth2:
//    //      client:
//    //        registration:
//    //          kakao: # spring security oauth2 설정 정보
//    //            client-id: 1234
//    //            scope:
//    //              - account_email
//    //            client-name: Kakao
//    //            authorization-grant-type: authorization_code # oauth2의 동작 방식 중 코드를 응답받는 방식으로 진행한다는 의미
//    //            redirect-uri: http://127.0.0.1:8080/login/oauth2/code/kakao # 배포 프로필별 따로 작성
//    //            client-authentication-method: POST
//    //
//    //        provider:
//    //          kakao:
//    //            authorization-uri: https://kauth.kakao.com/oauth/authorize
//    //            token-uri: https://kauth.kakao.com/oauth/token
//    //            user-info-uri: https://kapi.kakao.com/v2/user/me
//    //            user-name-attribute: id # 회원정보를 json 으로 받는데 id 라는 키 값으로 리턴
//    //
//    //     위와 같이 입력(client-id는 위에서 기록했던 것을 입력하고, scope 는 설정한 스코프를 입력)
//    //     th:onclick="|location.href='@{/oauth2/authorization/kakao}'|" 와 같이 /oauth2/authorization/kakao 경로로 이동하면,
//    //     네이버 로그인 화면으로 이동했다가 로그인이 완료되면 로그인 정보를 가지고 CustomDefaultOauth2UserService 의 loadUser 가 실행됨
//
//    @Service
//    class CustomDefaultOauth2MemberService(
//        private val database1MemberRepository: Database1_Member_MemberRepository,
//        private val database1MemberRoleRepository: Database1_Member_MemberRoleRepository,
//        private val database1MemberMemberSnsOauth2Repository: Database1_Member_MemberOauth2Repository
//    ) : DefaultOAuth2UserService() {
//        @Throws(OAuth2AuthenticationException::class)
//        override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User? {
//            // OAuth2 로그인 정보
////            println("clientRegistration: " + userRequest.clientRegistration)
////            println("tokenValue: " + userRequest.accessToken.tokenValue)
////            println("clientRegistration: " + userRequest.clientRegistration)
////            println("attributes: " + super.loadUser(userRequest).attributes)
////            println("registrationId: " + userRequest.clientRegistration.registrationId)
//
//            val oAuth2Member = super.loadUser(userRequest)
//
//            // 로그인 타입별 정보 가져오기
//            // 로그인 고유값인 loginId 외의 모든 정보는 nullable 이 될 가능성이 있다는 것을 주의
//            val memberId: String
//            val oAuth2TypeCode: Int
//
//            when (userRequest.clientRegistration.registrationId.trim().lowercase()) {
//                "google" -> {
//                    memberId = oAuth2Member.getAttribute<String>("sub")!!
//                    oAuth2TypeCode = 1
//                }
//
//                // todo apple
//
//                "naver" -> {
//                    memberId = (super.loadUser(userRequest).attributes["response"] as Map<*, *>)["id"].toString()
//                    oAuth2TypeCode = 3
//                }
//
//                "kakao" -> {
//                    memberId = (super.loadUser(userRequest).attributes["id"]).toString()
//                    oAuth2TypeCode = 4
//                }
//
//                else -> {
//                    throw Exception("registrationId not supported")
//                }
//            }
//
//            val snsOauth2 = database1MemberMemberSnsOauth2Repository.findByOauth2TypeCodeAndOauth2IdAndRowActivate(
//                oAuth2TypeCode,
//                memberId,
//                true
//            )
//
//            return if (snsOauth2 == null) {
//                // 등록된 회원이 없음 == 회원가입 안됨
//                // todo 에러 핸들러에서 핸들링이 가능한지 확인하여 처리
//                throw UsernameNotFoundException("UsernameNotFound")
//            } else { // 기존 회원이 있을 때
//                val member = database1MemberRepository.findByUidAndRowActivate(
//                    snsOauth2.memberUid, true
//                )
//
//                if (member == null) {
//                    // 등록된 회원이 없음 == 회원가입 안됨
//                    // todo 에러 핸들러에서 핸들링이 가능한지 확인하여 처리
//                    throw UsernameNotFoundException("UsernameNotFound")
//                } else {
//                    // 회원 권한 가져오기
//                    val memberRole =
//                        database1MemberRoleRepository.findAllByMemberUidAndRowActivate(
//                            snsOauth2.memberUid, true
//                        )
//                    // 회원 권한 형식 변경
//                    val authorities: ArrayList<GrantedAuthority> = ArrayList()
//                    for (roleInfo in memberRole) {
//                        authorities.add(
//                            SimpleGrantedAuthority(
//                                when (roleInfo.roleCode) {
//                                    1 -> "ROLE_ADMIN"
//                                    2 -> "ROLE_DEVELOPER"
//                                    else -> throw Exception()
//                                }
//                            )
//                        )
//                    }
//
//                    CustomMemberDetails.buildObject(member.uid.toString(), authorities, oAuth2Member.attributes)
//                }
//            }
//        }
//    }
}