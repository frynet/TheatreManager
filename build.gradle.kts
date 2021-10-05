import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
    testCompile("junit", "junit", "4.12")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.5.5")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.5")

    implementation("org.postgresql:postgresql:42.2.24.jre7")

    implementation("io.swagger.core.v3:swagger-annotations:2.1.11")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
