plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.maps.secrets)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.symbolProcessor)
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
    api(project(":core-http"))
    api(project(":platform:cache"))
    api(libs.paginate)
    api(libs.android.room.ktx)

    implementation(project(":platform:theme"))
    implementation(libs.android.lifecycle.viewmodel)
    implementation(libs.koin.android)
    implementation(libs.loadable.list)

    ksp(libs.android.room.compiler)

    releaseImplementation(libs.slf4j) {
        because("Ktor references \"StaticLoggerBinder\" and it is missing on minification.")
    }

    testImplementation(project(":core:sample"))
    testImplementation(libs.junit)
}
