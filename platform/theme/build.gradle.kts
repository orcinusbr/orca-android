plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
}

dependencies {
    api(libs.android.compose.material)
    api(libs.android.compose.ui.tooling)

    implementation(kotlin("stdlib"))
    implementation(libs.accompanist.adapter)
    implementation(libs.android.material)
    implementation(libs.kotlin.reflect)
}
