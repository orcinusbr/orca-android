plugins {
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("std.imageloader.coil")
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

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {
    api(project(":std:image-loader"))
    api(Dependencies.COMPOSE_UI_TOOLING)

    implementation(project(":platform:theme"))
    implementation(Dependencies.COIL)
    implementation(Dependencies.COMPOSE_MATERIAL_ICONS_EXTENDED)
    implementation(Dependencies.CORE)
    implementation(Dependencies.LOADABLE_PLACEHOLDER)
}
