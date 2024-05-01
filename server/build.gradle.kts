import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.cloud.tools.appengine") version "2.4.2"
}

group = "com.caelum.wowo"
version = "1.0.0"
application {
    mainClass.set("com.caelum.wowo.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

kotlin {
    jvmToolchain(11)
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/server-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}

dependencies {
    implementation(libs.logback)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    //Ktor server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.rate.limit)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    //Ktor client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.apache5)
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    //MongoDB
    implementation(libs.mongodb.driver.kotlin.coroutine)
    implementation(libs.bson.kotlinx)

    //Koin
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
}