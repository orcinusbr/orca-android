plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("platform.ui.test")
    compileSdk = Versions.Mastodonte.SDK_COMPILE

    defaultConfig {
        minSdk = Versions.Mastodonte.SDK_MIN
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
}

tasks.getByName("testReleaseUnitTest") {
    enabled = false
}
