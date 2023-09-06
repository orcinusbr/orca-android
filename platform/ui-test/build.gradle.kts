plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("platform.ui.test")
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

    buildFeatures {
        compose = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    kotlinOptions {
        @Suppress("SpellCheckingInspection")
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {
    androidTestImplementation(Dependencies.TEST_CORE)

    api(Dependencies.NAVIGATION_FRAGMENT)

    implementation(project(":platform:ui"))
    implementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    implementation(Dependencies.TIME4J)
}
