import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.3.21"
    id("org.springframework.boot") version "2.1.2.RELEASE"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    kotlin("kapt") version "1.3.31"
}

version = "1.0.0-SNAPSHOT"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-data-mongodb")
    compile("org.springframework.security:spring-security-oauth2-client:5.1.5.RELEASE")
    compile("org.springframework.security.oauth:spring-security-oauth2:2.3.5.RELEASE")
    compile("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.1.4.RELEASE")
    compile("org.springframework.boot:spring-boot-starter-security")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("io.springfox:springfox-swagger-ui:2.9.2")
    compile("io.springfox:springfox-swagger2:2.9.2")
    compile("org.mongodb:mongodb-driver")
    compile("com.github.ulisesbocchio:jasypt-spring-boot-starter:2.1.1")


    testCompile("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testCompile("de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.1.2")
    testCompile("org.springframework.security:spring-security-test")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
        compileOnly ("org.springframework.boot:spring-boot-configuration-processor")
}
