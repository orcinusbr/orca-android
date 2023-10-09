import com.jeanbarrossilva.orca.namespaceFor

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.symbolProcessor)

    id("build-src")
}

android {
    namespace = namespaceFor("feature.settings.termmuting")
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
}

dependencies {
    ksp(project(":std:injector-processor"))

    implementation(project(":core"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(project(":std:injector"))
}
