import com.jeanbarrossilva.orca.namespaceFor

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("build-src")
}

android.namespace = namespaceFor("core.http")

dependencies {
    api(project(":core"))
    api(libs.ktor.client.core)
    api(libs.ktor.serialization.json)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)

    testImplementation(project(":core-test"))
    testImplementation(libs.assertk)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockito)
}
