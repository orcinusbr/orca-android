plugins {
    id("com.android.library")

    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    namespace = namespaceFor("core.sharedpreferences")
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
    androidTestImplementation(project(":core-test"))
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.kotlin.coroutines.test)

    implementation(project(":core"))
    implementation(libs.android.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.serialization.json)
}
