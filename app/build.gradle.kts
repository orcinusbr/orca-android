plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version Versions.KSP
    id("kotlin-android")
}

android {
    namespace = Metadata.namespace("app")
    compileSdk = Versions.Mastodonte.SDK_COMPILE

    defaultConfig {
        applicationId = Metadata.GROUP
        minSdk = Versions.Mastodonte.SDK_MIN
        targetSdk = Versions.Mastodonte.SDK_TARGET
        versionCode = Versions.Mastodonte.CODE
        versionName = Versions.Mastodonte.NAME
    }

    buildTypes {
        getByName(Variants.RELEASE) {
            isMinifyEnabled = true

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

    testImplementation(project(":platform:ui-test"))
    testImplementation(Dependencies.ACTIVITY)
    testImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    testImplementation(Dependencies.ROBOLECTRIC)
    testImplementation(Dependencies.TEST_CORE)
    testImplementation(Dependencies.TEST_ESPRESSO_CORE)
}
