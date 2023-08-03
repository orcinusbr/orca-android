plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version Versions.KSP
    id("kotlin-android")
}

android {
    namespace = Metadata.namespace("app")
    compileSdk = Versions.Orca.SDK_COMPILE
    flavorDimensions += Dimensions.VERSION

    defaultConfig {
        applicationId = Metadata.GROUP
        minSdk = Versions.Orca.SDK_MIN
        targetSdk = Versions.Orca.SDK_TARGET
        versionCode = Versions.Orca.CODE
        versionName = Versions.Orca.NAME
    }

    buildTypes {
        getByName(Variants.RELEASE) {
            isMinifyEnabled = true
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    productFlavors {
        create("demo") {
            dimension = Dimensions.VERSION
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
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

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {
    "demoImplementation"(project(":core-test"))

    implementation(project(":core:mastodon"))
    implementation(project(":core:sample"))
    implementation(project(":core:shared-preferences"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:profile-details"))
    implementation(project(":feature:toot-details"))
    implementation(project(":platform:launchable"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.CONSTRAINTLAYOUT)
    implementation(Dependencies.FRAGMENT)
    implementation(Dependencies.KOIN_ANDROID)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.TIME4J)

    "testDemoImplementation"(project(":platform:ui-test"))
    "testDemoImplementation"(Dependencies.ACTIVITY)
    "testDemoImplementation"(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    "testDemoImplementation"(Dependencies.ROBOLECTRIC)
    "testDemoImplementation"(Dependencies.TEST_CORE)
    "testDemoImplementation"(Dependencies.TEST_ESPRESSO_CORE)
}
