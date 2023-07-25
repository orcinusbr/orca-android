plugins {
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("core.mastodon")
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
    implementation(Dependencies.KTOR_CIO)
    implementation(Dependencies.KTOR_CONTENT_NEGOTIATION)
    implementation(Dependencies.KTOR_CORE)
    implementation(Dependencies.KTOR_SERIALIZATION_KOTLINX_JSON)
    implementation(Dependencies.PAGINATE)
}
