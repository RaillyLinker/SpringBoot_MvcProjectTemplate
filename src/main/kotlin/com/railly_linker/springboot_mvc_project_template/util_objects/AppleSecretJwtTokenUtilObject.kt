package com.railly_linker.springboot_mvc_project_template.util_objects

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Reader
import java.io.StringReader
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


// [Apple Client Secret 생성용 JWT 토큰 유틸]
object AppleSecretJwtTokenUtilObject {
    // <static 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)

    // https://developer.apple.com/account/resources/identifiers/list
    // !!!Keys - KeyID!!
    private const val keyId = "TODO"

    // !!!Identifiers - App IDs - Team ID!!
    private const val teamId = "TODO"

    // !!!Identifiers - Services - Identifier!!
    private const val clientId = "TODO"

    // !!!p8 파일 안 내용!!
    private const val secretFileContext = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgpCwRnXuXGVqlLCsd\n" +
            "1k5WEcCP2YId7g7YHaB/EzctDpWgCgYIKoZIzj0DAQehRANCAAQFofDg0/6BEEfd\n" +
            "duTtOlDRozisCZ9gjn3Bfj4Rvx81yP4nA6wOsL5lpQapPw7CmB88//KZMZlPnW3d\n" +
            "MMQ6TODO\n" +
            "-----END PRIVATE KEY-----"


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (ClientSecret 발행)
    fun makeClientSecret(): String {
        val expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
        return Jwts.builder()
            .setHeaderParam("kid", keyId)
            .setHeaderParam("alg", "ES256")
            .setIssuer(teamId)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expirationDate)
            .setAudience("https://appleid.apple.com")
            .setSubject(clientId)
            .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
            .compact()
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>
    private fun getPrivateKey(): PrivateKey {
        val privateKey = secretFileContext
        val pemReader: Reader = StringReader(privateKey)
        val pemParser = PEMParser(pemReader)
        val converter = JcaPEMKeyConverter()
        val `object` = pemParser.readObject() as PrivateKeyInfo
        return converter.getPrivateKey(`object`)
    }


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>


}