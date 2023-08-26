plugins {
    id("com.android.library")
    id("com.google.devtools.ksp") version Versions.KSP
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("platform.cache")
    compileSdk = Versions.Orca.SDK_COMPILE

    defaultConfig {
        minSdk = Versions.Orca.SDK_MIN
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
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
}

dependencies {
    ksp(Plugins.ROOM)

    implementation(Dependencies.COROUTINES_CORE)
    implementation(Dependencies.ROOM)

    testImplementation(kotlin("test"))
    testImplementation(Dependencies.COROUTINES_TEST)
    testImplementation(Dependencies.MOCKITO)
    testImplementation(Dependencies.ROBOLECTRIC)
}
