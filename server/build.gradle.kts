plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.caelum.wowo"
version = "1.0.0"
application {
    mainClass.set("com.caelum.wowo.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.kotlinx.coroutines.core)

    //MongoDB
    implementation(libs.mongodb.driver.kotlin.coroutine)
    implementation(libs.bson.kotlinx)

    //Koin
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
}