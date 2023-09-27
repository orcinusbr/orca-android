import com.jeanbarrossilva.orca.namespaceFor

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("build-src")
}

android.namespace = namespaceFor("core.http.test")

dependencies {
    api(project(":core-test"))
    api(libs.ktor.client.core)
    api(libs.ktor.client.mock)

    implementation(project(":core:http"))
    implementation(project(":core:sample"))
}
