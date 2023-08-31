import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.9.10"
	kotlin("plugin.spring") version "1.9.10"

	// 추가
	kotlin("kapt") version "1.9.10"
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

	// (Swagger)
	// : API 자동 문서화
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	// (GSON)
	// : Json - Object 라이브러리
	implementation("com.google.code.gson:gson:2.10.1")
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
