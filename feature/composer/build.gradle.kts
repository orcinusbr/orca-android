plugins {
    id("com.android.library")

    kotlin("android")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
    androidTestImplementation(libs.android.compose.ui.test.junit)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.koin.test)

    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(libs.android.compose.material.icons)
    implementation(libs.koin.android)

    testImplementation(libs.junit)
}
