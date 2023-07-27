plugins {
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.android.library")
    id("org.jetbrains.kotlin.android")

    @Suppress("SpellCheckingInspection")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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

    @Suppress("UnstableApiUsage")
    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    kotlinOptions {
        jvmTarget = Versions.java.toString()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(Dependencies.BROWSER)
    implementation(Dependencies.COMPOSE_MATERIAL_ICONS_EXTENDED)
    implementation(Dependencies.KOIN_ANDROID)
    implementation(Dependencies.KTOR_CIO)
    implementation(Dependencies.KTOR_CONTENT_NEGOTIATION)
    implementation(Dependencies.KTOR_CORE)
    implementation(Dependencies.KTOR_SERIALIZATION_KOTLINX_JSON)
    implementation(Dependencies.PAGINATE)
    implementation(Dependencies.VIEWMODEL)
}
