plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("platform.ui")
    compileSdk = Versions.Mastodonte.SDK_COMPILE

    defaultConfig {
        minSdk = Versions.Mastodonte.SDK_MIN
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            @Suppress("UnstableApiUsage")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    @Suppress("UnstableApiUsage")
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
    api(Dependencies.FRAGMENT)
    api(Dependencies.LOADABLE)

    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(Dependencies.ACTIVITY_COMPOSE)
    implementation(Dependencies.COIL_COMPOSE)
    implementation(Dependencies.COMPOSE_MATERIAL_ICONS_EXTENDED)
    implementation(Dependencies.LOADABLE_LIST)
    implementation(Dependencies.LOADABLE_PLACEHOLDER)
    implementation(Dependencies.LOADABLE_PLACEHOLDER_TEST)
    implementation(Dependencies.LOTTIE_COMPOSE)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.TIME4J)

    testImplementation(project(":platform:ui-test"))
    testImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    testImplementation(Dependencies.COMPOSE_UI_TEST_MANIFEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}
