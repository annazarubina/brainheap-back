import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    val kotlinVersion = "1.3.30"
    id("org.springframework.boot") version "2.1.8.RELEASE"
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

    testLogging {
        lifecycle {
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }
        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }

    val failedTests = mutableListOf<TestDescriptor>()
    val skippedTests = mutableListOf<TestDescriptor>()

    // See https://github.com/gradle/kotlin-dsl/issues/836
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            when (result.resultType) {
                TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
                TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                else -> Unit
            }
        }

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) { // root suite
                logger.lifecycle("----")
                logger.lifecycle("Test result: ${result.resultType}")
                logger.lifecycle(
                        "Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped")
                failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
                skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
            }
        }

        private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
            logger.lifecycle(subject)
            forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
        }

        private fun TestDescriptor.displayName() = parent?.let { "${it.name} - $name" } ?: "$name"
    })
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
    compile("com.github.ulisesbocchio:jasypt-spring-boot-starter:2.1.1")
    compile("com.netflix.feign:feign-core:8.18.0")
    compile("com.netflix.feign:feign-gson:8.18.0")
    compile("com.google.cloud:google-cloud-translate:1.62.0")

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
