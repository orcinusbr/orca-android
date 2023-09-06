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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        create("default") {
            dimension = Dimensions.VERSION
        }

        create("demo") {
            dimension = Dimensions.VERSION
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
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
    "androidTestDemoImplementation"(project(":platform:ui-test"))
    "androidTestDemoImplementation"(Dependencies.ACTIVITY)
    "androidTestDemoImplementation"(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    "androidTestDemoImplementation"(Dependencies.TEST_CORE)
    "androidTestDemoImplementation"(Dependencies.TEST_ESPRESSO_INTENTS)
    "androidTestDemoImplementation"(Dependencies.TEST_RUNNER)

    "demoImplementation"(project(":core-test"))

    implementation(project(":core:mastodon"))
    implementation(project(":core:sample"))
    implementation(project(":core:shared-preferences"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:composer"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:profile-details"))
    implementation(project(":feature:search"))
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
}
