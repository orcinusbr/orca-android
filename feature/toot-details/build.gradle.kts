import com.jeanbarrossilva.orca.namespaceFor

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    namespace = namespaceFor("feature.tootdetails")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(libs.android.lifecycle.viewmodel)
    implementation(libs.koin.android)
    implementation(libs.loadable.list)
    implementation(libs.loadable.placeholder)
}
