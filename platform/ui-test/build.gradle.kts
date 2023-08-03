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

    @Suppress("UnstableApiUsage")
    buildFeatures {
        compose = true
        viewBinding = true
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

        @Suppress("SpellCheckingInspection")
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {
    api(Dependencies.NAVIGATION_FRAGMENT)

    implementation(project(":platform:ui"))
    implementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    implementation(Dependencies.TIME4J)

    testImplementation(Dependencies.ROBOLECTRIC)
    testImplementation(Dependencies.TEST_CORE)
}
