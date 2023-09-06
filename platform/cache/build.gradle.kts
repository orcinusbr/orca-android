plugins {
    id("com.android.library")
    id("com.google.devtools.ksp") version Versions.KSP
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("platform.cache")
    compileSdk = Versions.Orca.SDK_COMPILE

    defaultConfig {
        minSdk = Versions.Orca.SDK_MIN
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    packagingOptions.resources.excludes +=
        arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
}

dependencies {
    androidTestImplementation(kotlin("test"))
    androidTestImplementation(Dependencies.COROUTINES_TEST)
    androidTestImplementation(Dependencies.MOCKK)
    androidTestImplementation(Dependencies.TEST_CORE)
    androidTestImplementation(Dependencies.TEST_RUNNER)

    ksp(Plugins.ROOM)

    implementation(Dependencies.COROUTINES_CORE)
    implementation(Dependencies.ROOM)
}
