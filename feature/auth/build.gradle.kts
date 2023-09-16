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
    androidTestImplementation(project(":core-test"))
    androidTestImplementation(project(":platform:ui-test"))
    androidTestImplementation(libs.android.compose.ui.test.junit)
    androidTestImplementation(libs.android.compose.ui.test.manifest)
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.koin.test)

    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(libs.koin.android)
}
