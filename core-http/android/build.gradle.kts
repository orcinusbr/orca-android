import com.jeanbarrossilva.orca.namespaceFor

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("build-src")
}

android.namespace = namespaceFor("core.http.android")

dependencies {
    api(project(":core-http"))

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
}
