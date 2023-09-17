import java.util.Properties

dependencyResolutionManagement.versionCatalogs.create("libs") {
    library(
        "accompanist-adapter",
        "com.google.accompanist:accompanist-themeadapter-material3:0.31.3-beta"
    )
    library("android-activity-ktx", "androidx.activity", "activity-ktx").versionRef(
        "android-activity"
    )
    library("android-activity-compose", "androidx.activity", "activity-compose").versionRef(
        "android-activity"
    )
    library("android-appcompat", "androidx.appcompat:appcompat:1.6.1")
    library("android-browser", "androidx.browser:browser:1.5.0")
    library("android-compose-material", "androidx.compose.material3:material3:1.1.0")
    library(
        "android-compose-material-icons",
        "androidx.compose.material:material-icons-extended:1.5.1"
    )
    library("android-compose-ui-test-junit", "androidx.compose.ui", "ui-test-junit4").versionRef(
        "android-compose"
    )
    library("android-compose-ui-test-manifest", "androidx.compose.ui", "ui-test-manifest")
        .versionRef("android-compose")
    library("android-compose-ui-tooling", "androidx.compose.ui", "ui-tooling").versionRef(
        "android-compose"
    )
    library("android-constraintlayout", "androidx.constraintlayout:constraintlayout:2.1.4")
    library("android-core", "androidx.core:core-ktx:1.10.1")
    library("android-fragment-ktx", "androidx.fragment", "fragment-ktx").versionRef(
        "android-fragment"
    )
    library("android-fragment-testing", "androidx.fragment", "fragment-testing").versionRef(
        "android-fragment"
    )
    library("android-lifecycle-viewmodel", "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    library(
        "android-maps-secrets-plugin",
        "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:" +
            "2.0.1"
    )
    library("android-material", "com.google.android.material:material:1.9.0")
    library("android-navigation-fragment", "androidx.navigation:navigation-fragment:2.6.0")
    library("android-plugin", "com.android.tools.build:gradle:8.1.1")
    library("android-room-ktx", "androidx.room", "room-ktx").versionRef("android-room")
    library("android-room-plugin", "androidx.room", "room-compiler").versionRef("android-room")
    library("android-test-core", "androidx.test:core-ktx:1.5.0")
    library("android-test-espresso-intents", "androidx.test.espresso:espresso-intents:3.5.0")
    library("android-test-runner", "androidx.test:runner:1.5.0")
    library("coil", "io.coil-kt:coil:2.4.0")
    library("junit", "junit:junit:4.13.2")
    library("koin-android", "io.insert-koin:koin-android:3.4.2")
    library("koin-test", "io.insert-koin:koin-test-junit4:3.4.1")
    library("kotlin-coroutines-android", "org.jetbrains.kotlinx", "kotlinx-coroutines-android")
        .versionRef("kotlin-coroutines")
    library("kotlin-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core")
        .versionRef("kotlin-coroutines")
    library("kotlin-coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test")
        .versionRef("kotlin-coroutines")
    library("kotlin-plugin", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
    library(
        "kotlin-symbolProcessor-plugin",
        "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.8.21-1.0.11"
    )
    library("kotlin-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    library("kotlin-serialization-plugin", "org.jetbrains.kotlin", "kotlin-serialization")
        .versionRef("kotlin")
    library("ktor-client-contentNegotiation", "io.ktor", "ktor-client-content-negotiation")
        .versionRef("ktor")
    library("ktor-serialization-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef(
        "ktor"
    )
    library("ktor-client-cio", "io.ktor", "ktor-client-cio").versionRef("ktor")
    library("ktor-client-core", "io.ktor", "ktor-client-core").versionRef("ktor")
    library("loadable", "com.jeanbarrossilva.loadable", "loadable").versionRef("loadable")
    library("loadable-list", "com.jeanbarrossilva.loadable", "loadable-list").versionRef("loadable")
    library("loadable-placeholder", "com.jeanbarrossilva.loadable", "loadable-placeholder")
        .versionRef("loadable")
    library(
        "loadable-placeholder-test",
        "com.jeanbarrossilva.loadable",
        "loadable-placeholder-test"
    )
        .versionRef("loadable")
    library("mockk", "io.mockk:mockk-android:1.13.7")
    library("paginate", "com.chrynan.paginate:paginate-core:0.3.0")
    library("slf4j", "org.slf4j:slf4j-api:2.0.7")
    library("time4j", "net.time4j:time4j-android:4.8-2021a")

    version("android-activity", "1.7.2")
    version("android-compose", "1.5.1")
    version("android-compose-compiler", "1.4.7")
    version("android-fragment", "1.6.0")
    version("android-room", "2.5.2")
    version("android-sdk-min", "28")
    version("android-sdk-target", "34")
    version("java", rootProject.projectDir.properties("gradle").getProperty("project.java"))
    version("kotlin", "1.8.21")
    version("kotlin-coroutines", "1.7.1")
    version("ktor", "2.3.2")
    version("loadable", "1.6.9")
}

rootProject.name = "Orca"
includeBuild("build-src")
include(
    ":app",
    ":core",
    ":core:mastodon",
    ":core:sample",
    ":core:sample-test",
    ":core:shared-preferences",
    ":core-test",
    ":feature:auth",
    ":feature:composer",
    ":feature:feed",
    ":feature:profile-details",
    ":feature:search",
    ":feature:toot-details",
    ":platform:cache",
    ":platform:launchable",
    ":platform:theme",
    ":platform:ui",
    ":platform:ui-test",
    ":std:image-loader",
    ":std:image-loader:compose",
    ":std:image-loader-test",
    ":std:styled-string"
)

/**
 * Creates [Properties] according to the `.properties` file named [name] within this directory.
 *
 * @param name Name of the file.
 **/
fun File.properties(name: String): Properties {
    val file = File(this, "$name.properties")
    return Properties().apply { tryToLoad(file) }
}

/**
 * Loads the given [file] into these [Properties].
 *
 * @param file [File] to be loaded.
 **/
fun Properties.load(file: File) {
    file.inputStream().reader().use {
        load(it)
    }
}

/**
 * Loads the given [file] into these [Properties] if it's a normal file.
 *
 * @param file [File] to be loaded.
 **/
fun Properties.tryToLoad(file: File) {
    if (file.isFile) {
        load(file)
    }
}
