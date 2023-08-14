plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("feature.composer")
    compileSdk = Versions.Orca.SDK_COMPILE

    defaultConfig {
        minSdk = Versions.Orca.SDK_MIN
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
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

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(Dependencies.COMPOSE_MATERIAL_ICONS_EXTENDED)
    implementation(Dependencies.KOIN_ANDROID)

    testImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    testImplementation(Dependencies.KOIN_TEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}