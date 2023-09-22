import com.jeanbarrossilva.orca.namespaceFor

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("build-src")
}

android {
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    namespace = namespaceFor("core.http.mastodon.social")
}

dependencies {
    api(project(":core:http"))
}
