plugins {
    java
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.jpa") version "1.4.32"
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version "1.4.32"
}

group = "com.frynet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.5.5")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.5")

    runtimeOnly("org.flywaydb:flyway-core:7.15.0")
    runtimeOnly("org.postgresql:postgresql:42.2.24.jre7")

    runtimeOnly("org.springdoc:springdoc-openapi-ui:1.5.11")
    implementation("io.swagger.core.v3:swagger-annotations:2.1.11")

    testImplementation("junit", "junit", "4.13.1")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.5")
    testImplementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.0.4")
}

tasks {
    test {
        useJUnitPlatform()
    }
}