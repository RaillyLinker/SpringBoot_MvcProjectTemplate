import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"

    // 추가
    kotlin("kapt") version "1.9.10" // kotlin 어노테이션
    kotlin("plugin.allopen") version "1.9.10" // allOpen 에 지정한 어노테이션으로 만든 클래스에 open 키워드를 적용
    kotlin("plugin.noarg") version "1.9.10" // noArg 에 지정한 어노테이션으로 만든 클래스에 자동으로 no-arg 생성자를 생성
    kotlin("plugin.jpa") version "1.9.10" // JPA 사용을 위한 플러그인 추가 옵션
}

group = "com.railly_linker"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    // (기본)
    implementation("org.springframework.boot:spring-boot-starter:3.1.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")

    // (snakeyaml)
    // : Gradle 종속성 에러 해결
    implementation("org.yaml:snakeyaml:2.2")

    // (Spring Starter Web)
    // : 스프링 부트 웹 개발
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")

    // (Swagger)
    // : API 자동 문서화
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // (GSON)
    // : Json - Object 라이브러리
    implementation("com.google.code.gson:gson:2.10.1")

    // (Jackson)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.1")

    // (retrofit2 네트워크 요청)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // (ThymeLeaf)
    // : 웹 뷰 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.0.4")

    // (Spring email)
    // : 스프링 이메일 발송
    implementation("org.springframework.boot:spring-boot-starter-mail:3.0.4")

    // (JPA)
    // : DB ORM
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.14.2")
    implementation("org.hibernate:hibernate-validator:8.0.0.Final")

    // (MySQL)
    // : MySQL 접근 사용 라이브러리
    implementation("com.mysql:mysql-connector-j:8.0.32")

    // (SpringBoot AOP)
    implementation("org.springframework.boot:spring-boot-starter-aop:3.0.4")

    // (Redis)
    // : 메모리 키 값 데이터 구조 스토어
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.0.4")

    // (JWT)
    // : JWT 인증 토큰 라이브러리
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // (Spring Security)
    // : 스프링 부트 보안
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")
    testImplementation("org.springframework.security:spring-security-test:6.0.2")

    // (Apache Common Codec)
    implementation("commons-codec:commons-codec:1.16.0")

    // (Excel File Read Write)
    // : 액셀 파일 입출력 라이브러리
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("sax:sax:2.0.1")

    // (HTML 2 PDF)
    // : HTML -> PDF 변환 라이브러리
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.2.2")

    // (정적 이미지 리사이징)
    implementation("net.coobird:thumbnailator:0.4.20")

    // (WebSocket)
    // : 웹소켓
    implementation("org.springframework.boot:spring-boot-starter-websocket:3.0.4")

    // (Spring Admin Client)
    // : Spring Actuator 포함
    implementation("de.codecentric:spring-boot-admin-starter-client:3.0.2")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// kotlin jpa : 아래의 어노테이션 클래스에 no-arg 생성자를 생성
noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

// kotlin jpa : 아래의 어노테이션 클래스를 open class 로 자동 설정
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
