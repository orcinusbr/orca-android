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
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    kotlinOptions {
        jvmTarget = Versions.java.toString()
    }
}

dependencies {
    implementation(project(":core"))
    implementation(Dependencies.CORE)
    implementation(Dependencies.SERIALIZATION_JSON)

    testImplementation(project(":core-test"))
    testImplementation(Dependencies.COROUTINES_TEST)
    testImplementation(Dependencies.JUNIT)
    testImplementation(Dependencies.ROBOLECTRIC)
}
