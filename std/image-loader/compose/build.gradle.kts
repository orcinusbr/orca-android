plugins {
    id("com.android.library")

    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    namespace = namespaceFor("std.imageloader.compose")
}

dependencies {
    api(project(":std:image-loader"))
    api(libs.android.compose.ui.tooling)

    implementation(project(":platform:theme"))
    implementation(libs.android.core)
    implementation(libs.coil)
    implementation(libs.loadable.placeholder)
}
