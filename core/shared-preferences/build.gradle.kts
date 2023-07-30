import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Versions.java.toString()
    }
}

android {
    namespace = Metadata.namespace("core.sharedpreferences")
    compileSdk = Versions.Mastodonte.SDK_TARGET

    defaultConfig {
        minSdk = Versions.Mastodonte.SDK_MIN
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true

            @Suppress("UnstableApiUsage")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
