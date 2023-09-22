import com.jeanbarrossilva.orca.namespaceFor

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.maps.secrets)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.symbolProcessor)

    id("build-src")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    namespace = namespaceFor("core.http")

    buildFeatures {
        buildConfig = true
        compose = true
    }

    secrets {
        defaultPropertiesFileName = "public.properties"
        ignoreList += "^(?!mastodon\\.clientSecret).*$"
    }
}

dependencies {
    api(project(":core"))
    api(project(":platform:cache"))
    api(libs.android.room.ktx)
    api(libs.ktor.client.core)
    api(libs.ktor.serialization.json)
    api(libs.paginate)

    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(libs.android.browser)
    implementation(libs.koin.android)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)

    ksp(libs.android.room.compiler)

    releaseImplementation(libs.slf4j) {
        because("Ktor references \"StaticLoggerBinder\" and it is missing on minification.")
    }

    testImplementation(project(":core:sample"))
    testImplementation(project(":core-test"))
    testImplementation(libs.assertk)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockito)
}
