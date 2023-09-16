plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")

    kotlin("android")
}

android {
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    packagingOptions.resources.excludes +=
        arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
}

dependencies {
    androidTestImplementation(kotlin("test"))
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.mockk)

    ksp(libs.android.room.plugin)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.android.room.ktx)
}
