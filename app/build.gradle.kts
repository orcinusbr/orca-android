plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version Versions.KSP
    id("kotlin-android")
}

@Suppress("UnstableApiUsage")
android {
    namespace = Metadata.namespace("app")
    compileSdk = Versions.Mastodonte.SDK_COMPILE

    defaultConfig {
        applicationId = Metadata.GROUP
        minSdk = Versions.Mastodonte.SDK_MIN
        targetSdk = Versions.Mastodonte.SDK_TARGET
        versionCode = Versions.Mastodonte.CODE
        versionName = Versions.Mastodonte.NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(Variants.RELEASE) {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        compose = true
        viewBinding = true
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
    androidTestImplementation(project(":platform:ui-test"))
    androidTestImplementation(Dependencies.ACTIVITY)
    androidTestImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    androidTestImplementation(Dependencies.TEST_CORE)
    androidTestImplementation(Dependencies.TEST_ESPRESSO_CORE)

    implementation(project(":core:sample"))
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
}
