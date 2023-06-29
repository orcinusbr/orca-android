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
    implementation(project(":core:sample"))
    implementation(project(":feature:profile-details"))
    implementation(project(":feature:toot-details"))
    implementation(project(":platform:launchable"))
    implementation(project(":platform:theme"))
    implementation(project(":platform:ui"))
    implementation(Dependencies.ACCOMPANIST_NAVIGATION_MATERIAL)
    implementation(Dependencies.COMPOSE_DESTINATIONS_ANIMATIONS)
    implementation(Dependencies.COMPOSE_DESTINATIONS_CORE)
    implementation(Dependencies.KOIN_COMPOSE)
    implementation(Dependencies.TIME4J)

    ksp(Plugins.COMPOSE_DESTINATIONS)
}
