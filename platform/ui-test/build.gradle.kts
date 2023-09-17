import com.jeanbarrossilva.orca.namespaceFor

plugins {
    id("com.android.library")

    kotlin("android")
}

android {
    namespace = namespaceFor("platform.ui.test")
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    androidTestImplementation(libs.android.test.core)

    api(libs.android.navigation.fragment)

    implementation(project(":platform:ui"))
    implementation(libs.android.compose.ui.test.junit)
    implementation(libs.time4j)
}
