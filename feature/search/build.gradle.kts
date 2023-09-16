plugins {
    id("com.android.library")

    kotlin("android")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(libs.koin.android)
    implementation(libs.loadable.list)
    implementation(libs.loadable.placeholder)
}
