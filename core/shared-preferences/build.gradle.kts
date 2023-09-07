plugins {
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("core.sharedpreferences")
    compileSdk = Versions.Orca.SDK_TARGET

    defaultConfig {
        minSdk = Versions.Orca.SDK_MIN
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }
}

dependencies {
    androidTestImplementation(project(":core-test"))
    androidTestImplementation(Dependencies.COROUTINES_TEST)
    androidTestImplementation(Dependencies.JUNIT)
    androidTestImplementation(Dependencies.TEST_RUNNER)

    implementation(project(":core"))
    implementation(Dependencies.CORE)
    implementation(Dependencies.SERIALIZATION_JSON)
}
