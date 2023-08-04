plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("feature.search")
    compileSdk = Versions.Orca.SDK_COMPILE

    defaultConfig {
        minSdk = Versions.Orca.SDK_MIN
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(Dependencies.LOADABLE_LIST)
    implementation(Dependencies.LOADABLE_PLACEHOLDER)

    testImplementation(project(":platform:ui-test"))
    testImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    testImplementation(Dependencies.COMPOSE_UI_TEST_MANIFEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}
