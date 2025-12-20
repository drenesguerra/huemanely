plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.huemanely"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {

    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.openapi)
    implementation(libs.swagger.codegen)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.h2)
    implementation(libs.postgresql)

    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("com.sun.activation:javax.activation:1.2.0")   

    implementation("io.insert-koin:koin-ktor:4.1.1")
    implementation("io.insert-koin:koin-logger-slf4j:4.1.1")

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

}

tasks.test {
    systemProperty("huemanely.h2.console", "false")
}
