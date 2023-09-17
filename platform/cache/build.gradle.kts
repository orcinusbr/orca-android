plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.symbolProcessor)
}

android {
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    packagingOptions.resources.excludes +=
        arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
}

dependencies {
    androidTestImplementation(libs.android.test.core)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.mockk)

    ksp(libs.android.room.compiler)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.android.room.ktx)
}
