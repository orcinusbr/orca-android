plugins {
    id("com.android.library")

    kotlin("android")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
}

dependencies {
    api(libs.android.compose.material)
    api(libs.android.compose.ui.tooling)

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(libs.accompanist.adapter)
    implementation(libs.android.material)
}
