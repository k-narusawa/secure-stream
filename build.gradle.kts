import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.cloud.tools.jib") version "3.4.0"
    id("org.flywaydb.flyway") version "9.22.3"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    kotlin("plugin.jpa") version "1.9.21"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

allprojects {
    group = "com.knarusawa"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("com.google.cloud.tools.jib")
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.jetbrains.kotlin:kotlin-maven-noarg")
        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.security:spring-security-config")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.webauthn4j:webauthn4j-core:0.22.1.RELEASE")

        runtimeOnly("org.postgresql:postgresql:42.7.1")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
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
}

project(":auth") {
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("com.auth0:java-jwt:4.4.0")
        implementation("sh.ory.hydra:hydra-client:2.2.0")
        implementation("org.flywaydb:flyway-core:9.22.3")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation(project(":common"))

        runtimeOnly("org.postgresql:postgresql:42.7.1")
    }

    jib {
        container {
            ports = listOf("8080")
            jvmFlags = listOf(
                "-server",
                "-Djava.awt.headless=true",
                "-XX:InitialRAMFraction=2",
                "-XX:MinRAMFraction=2",
                "-XX:MaxRAMFraction=2",
                "-XX:+UseG1GC",
                "-XX:MaxGCPauseMillis=100",
                "-XX:+UseStringDeduplication"
            )
        }
        to {
            image = "registry.hub.docker.com/19992240/secure-stream-auth"
            tags = setOf("${project.version}", "latest")
        }
    }
}

project(":api") {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("com.auth0:java-jwt:4.4.0")
        implementation("org.springframework.security:spring-security-oauth2-resource-server")
        implementation("org.springframework.security:spring-security-oauth2-jose")
        implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
        implementation("org.springframework.graphql:spring-graphql:1.2.4")
        implementation(project(":common"))
        implementation(project(":secure-stream-openapi:kotlin-spring"))

        testImplementation("org.springframework.graphql:spring-graphql-test:1.2.4")
    }
}

project(":secure-stream-openapi:kotlin-spring") {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
        implementation("io.swagger.core.v3:swagger-core:2.2.20")
        implementation("io.swagger.core.v3:swagger-models:2.2.20")
    }
}