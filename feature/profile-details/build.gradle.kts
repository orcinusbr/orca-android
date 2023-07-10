plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("feature.profile")
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
    androidTestImplementation(project(":core:sample-test"))
    androidTestImplementation(project(":platform:ui-test"))
    androidTestImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    androidTestImplementation(Dependencies.FRAGMENT_TESTING)
    androidTestImplementation(Dependencies.KOIN_TEST)
    androidTestImplementation(Dependencies.TURBINE)

    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(Dependencies.COMPOSE_MATERIAL_ICONS_EXTENDED)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL)
    implementation(Dependencies.LOADABLE_LIST)
    implementation(Dependencies.LOADABLE_PLACEHOLDER)
    implementation(Dependencies.KOIN_ANDROID)

    testImplementation(Dependencies.JUNIT)
}
