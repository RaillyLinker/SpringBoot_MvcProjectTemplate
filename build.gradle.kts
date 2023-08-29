import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0-M2"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.9.10"
	kotlin("plugin.spring") version "1.9.10"

	// 추가
	kotlin("kapt") version "1.8.0"
	kotlin("plugin.allopen") version "1.8.0" // allOpen 에 지정한 어노테이션으로 만든 클래스에 open 키워드를 적용
	kotlin("plugin.noarg") version "1.8.0" // noArg 에 지정한 어노테이션으로 만든 클래스에 자동으로 no-arg 생성자를 생성
	kotlin("plugin.jpa") version "1.8.0" // JPA 사용을 위한 플러그인 추가 옵션
}

group = "com.railly_linker"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_20
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	// (기본)
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// (Spring Starter Web)
	// : 스프링 부트 웹 개발
	implementation("org.springframework.boot:spring-boot-starter-web")

	// (ThymeLeaf)
	// : 웹 뷰 라이브러리
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

	// (Swagger)
	// : API 자동 문서화
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "20"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
