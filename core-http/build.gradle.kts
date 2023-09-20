plugins {
    alias(libs.plugins.kotlin.jvm)

    `java-library`
}

dependencies {
    api(project(":core"))
    api(libs.ktor.client.core)
    api(libs.ktor.serialization.json)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)

    testImplementation(project(":core-test"))
    testImplementation(libs.assertk)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.ktor.client.mock)
}
