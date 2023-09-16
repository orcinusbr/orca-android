plugins {
    id("com.android.library")

    kotlin("android")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    namespace = namespaceFor("feature.profiledetails")
}

dependencies {
    androidTestImplementation(project(":core:sample-test"))
    androidTestImplementation(project(":platform:ui-test"))
    androidTestImplementation(libs.android.compose.ui.test.junit)
    androidTestImplementation(libs.android.fragment.testing)
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.koin.test)

    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(libs.android.lifecycle.viewmodel)
    implementation(libs.loadable.list)
    implementation(libs.loadable.placeholder)
    implementation(libs.koin.android)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit)
}
