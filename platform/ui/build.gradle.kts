plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = Metadata.namespace("platform.ui")
    compileSdk = Versions.Orca.SDK_COMPILE

    defaultConfig {
        minSdk = Versions.Orca.SDK_MIN
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }

    packagingOptions.resources.excludes +=
        arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
}

dependencies {
    androidTestImplementation(kotlin("reflect"))
    androidTestImplementation(project(":platform:ui-test"))
    androidTestImplementation(project(":std:image-loader-test"))
    androidTestImplementation(Dependencies.COMPOSE_UI_TEST_JUNIT_4)
    androidTestImplementation(Dependencies.COMPOSE_UI_TEST_MANIFEST)
    androidTestImplementation(Dependencies.MOCKK)
    androidTestImplementation(Dependencies.TEST_CORE)
    androidTestImplementation(Dependencies.TEST_RUNNER)

    api(project(":std:image-loader:compose"))
    api(Dependencies.FRAGMENT)
    api(Dependencies.LOADABLE)

    implementation(project(":core"))
    implementation(project(":core:sample"))
    implementation(project(":platform:theme"))
    implementation(Dependencies.ACTIVITY_COMPOSE)
    implementation(Dependencies.COMPOSE_MATERIAL_ICONS_EXTENDED)
    implementation(Dependencies.LOADABLE_LIST)
    implementation(Dependencies.LOADABLE_PLACEHOLDER)
    implementation(Dependencies.LOADABLE_PLACEHOLDER_TEST)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.TIME4J)

    testImplementation(Dependencies.JUNIT)
}
