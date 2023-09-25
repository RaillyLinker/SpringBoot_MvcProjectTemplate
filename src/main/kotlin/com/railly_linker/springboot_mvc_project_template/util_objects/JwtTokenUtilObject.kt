package com.railly_linker.springboot_mvc_project_template.util_objects

import com.railly_linker.springboot_mvc_project_template.ApplicationConstants
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.json.BasicJsonParser
import java.nio.charset.StandardCharsets
import java.util.*

// [JWT 토큰 유틸]
object JwtTokenUtilObject {
    // <static 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // (JWT signature 비밀키)
    // !!!비밀키 변경!!
    private const val JWT_SECRET_KEY_STRING = "123456789abcdefghijklmnopqrstuvw"

    // (액세스 토큰 유효시간)
    // !!!유효시간 변경!!
    const val ACCESS_TOKEN_EXPIRATION_TIME_MS = 1000L * 60L * 30L // 30분

    // (리프레시 토큰 유효시간)
    // !!!유효시간 변경!!
    const val REFRESH_TOKEN_EXPIRATION_TIME_MS = 1000L * 60L * 60L * 24L * 7L // 7일

    // (JWT Claims 암호화 AES 키)
    // !!!암호키 변경!!
    private const val JWT_CLAIMS_AES256_INITIALIZATION_VECTOR: String = "odkejduc726dj48d" // 16자
    private const val JWT_CLAIMS_AES256_ENCRYPTION_KEY: String = "8fu3jd0ciiu3384hfucy36dye9sjv7b3" // 32자

    // (JWT 발행자)
    const val ISSUER = ApplicationConstants.PACKAGE_NAME


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (액세스 토큰 발행)
    fun generateAccessToken(username: String): String {
        return doGenerateToken(username, "access", ACCESS_TOKEN_EXPIRATION_TIME_MS)
    }

    // (리프레시 토큰 발행)
    fun generateRefreshToken(username: String): String {
        return doGenerateToken(username, "refresh", REFRESH_TOKEN_EXPIRATION_TIME_MS)
    }

    // (JWT Secret 확인)
    // : 토큰 유효성 검증. 유효시 true, 위변조시 false
    fun validateSignature(token: String): Boolean {
        val tokenSplit = token.split(".")
        val header = tokenSplit[0]
        val payload = tokenSplit[1]
        val signature = tokenSplit[2]

        // base64 로 인코딩된 header 와 payload 를 . 로 묶은 후 이를 시크릿으로 HmacSha256 해싱을 적용하여 signature 를 생성
        val newSig = CryptoUtilObject.hmacSha256("$header.$payload", JWT_SECRET_KEY_STRING)

        // 위 방식으로 생성된 signature 가 token 으로 전달된 signature 와 동일하다면 위/변조되지 않은 토큰으로 판단 가능
        // = 발행시 사용한 시크릿과 검증시 사용된 시크릿이 동일
        return signature == newSig
    }

    // (JWT 정보 반환)
    // Member Uid
    fun getMemberUid(token: String): String {
        return CryptoUtilObject.decryptAES256(
            parseJwtForPayload(token)["mu"].toString(),
            "AES/CBC/PKCS5Padding",
            JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
    }

    // Token 용도 (access or refresh)
    fun getTokenUsage(token: String): String {
        return CryptoUtilObject.decryptAES256(
            parseJwtForPayload(token)["tu"].toString(),
            "AES/CBC/PKCS5Padding",
            JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            JWT_CLAIMS_AES256_ENCRYPTION_KEY
        )
    }

    // 발행자
    fun getIssuer(token: String): String {
        return parseJwtForPayload(token)["iss"].toString()
    }

    // 토큰 남은 유효 시간(초) 반환 (만료된 토큰이라면 0)
    fun getRemainSeconds(token: String): Long {
        val exp = parseJwtForPayload(token)["exp"] as Long

        val diff = exp - (System.currentTimeMillis() / 1000)

        val remain = if (diff <= 0) {
            0
        } else {
            diff
        }

        return remain
    }

    // 토큰 타입
    fun getTokenType(token: String): String {
        return parseJwtForHeader(token)["typ"].toString()
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    // (JWT 토큰 생성)
    private fun doGenerateToken(userUid: String, tokenUsage: String, expireTimeMs: Long): String {
        val claims: Claims = Jwts.claims()

        // JWT 내부에 정보 입력
        claims["mu"] = CryptoUtilObject.encryptAES256(
            userUid,
            "AES/CBC/PKCS5Padding",
            JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            JWT_CLAIMS_AES256_ENCRYPTION_KEY
        ) // member uid
        claims["tu"] = CryptoUtilObject.encryptAES256(
            tokenUsage,
            "AES/CBC/PKCS5Padding",
            JWT_CLAIMS_AES256_INITIALIZATION_VECTOR,
            JWT_CLAIMS_AES256_ENCRYPTION_KEY
        ) // token usage
        return Jwts.builder()
            .setHeader(
                mapOf(
                    "typ" to "JWT"
                )
            )
            .setClaims(claims)
            .setIssuer(ISSUER) // 발행자
            .setIssuedAt(Date(System.currentTimeMillis())) // 토큰 생성일
            .setExpiration(Date(System.currentTimeMillis() + expireTimeMs)) // 토큰 만료일
            .signWith(
                Keys.hmacShaKeyFor(JWT_SECRET_KEY_STRING.toByteArray(StandardCharsets.UTF_8)),
                SignatureAlgorithm.HS256
            )
            .compact()
    }

    // (base64 로 인코딩된 Header, Payload 를 base64 로 디코딩)
    private fun parseJwtForHeader(jwt: String): Map<String, Any> {
        val header = CryptoUtilObject.base64Decode(jwt.split(".")[0])
        return BasicJsonParser().parseMap(header)
    }

    private fun parseJwtForPayload(jwt: String): Map<String, Any> {
        val payload = CryptoUtilObject.base64Decode(jwt.split(".")[1])
        return BasicJsonParser().parseMap(payload)
    }


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>


}