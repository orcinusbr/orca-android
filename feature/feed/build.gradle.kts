plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.symbolProcessor)
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
    androidTestImplementation(project(":platform:ui-test"))
    androidTestImplementation(project(":std:injector-test"))
    androidTestImplementation(libs.android.compose.ui.test.junit)
    androidTestImplementation(libs.android.compose.ui.test.manifest)
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.android.test.runner)

    ksp(project(":std:injector-processor"))

    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(project(":std:injector"))
    implementation(libs.loadable.list)
}
