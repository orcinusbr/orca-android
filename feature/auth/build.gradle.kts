plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("feature.auth")
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

    kotlinOptions {
        jvmTarget = Versions.java.toString()
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
    implementation(Dependencies.KOIN_ANDROID)

    testImplementation(project(":core-test"))
    testImplementation(project(":platform:ui-test"))
    testImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    testImplementation(Dependencies.COMPOSE_UI_TEST_MANIFEST)
    testImplementation(Dependencies.KOIN_TEST)
    testImplementation(Dependencies.ROBOLECTRIC)
    testImplementation(Dependencies.TEST_CORE)
}
