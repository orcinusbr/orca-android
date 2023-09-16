plugins {
    id("com.android.library")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp")

    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()

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
    api(project(":platform:cache"))
    api(libs.paginate)
    api(libs.android.room.ktx)

    implementation(project(":core"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(libs.android.browser)
    implementation(libs.android.lifecycle.viewmodel)
    implementation(libs.koin.android)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.serialization.json)
    implementation(libs.loadable.list)

    ksp(libs.android.room.plugin)

    releaseImplementation(libs.slf4j) {
        because("Ktor references \"StaticLoggerBinder\" and it is missing on minification.")
    }

    testImplementation(project(":core:sample"))
    testImplementation(libs.junit)
}
