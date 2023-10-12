import com.jeanbarrossilva.orca.Dimensions
import com.jeanbarrossilva.orca.namespace
import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)

  id("build-src")
}

android {
  compileSdk = libs.versions.android.sdk.target.get().toInt()
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  flavorDimensions += Dimensions.VERSION
  namespace = namespaceFor("app")

  buildFeatures {
    compose = true
    viewBinding = true
  }

  buildTypes { release { isMinifyEnabled = true } }

  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
  }

  defaultConfig {
    applicationId = project.namespace
    minSdk = libs.versions.android.sdk.min.get().toInt()
    targetSdk = libs.versions.android.sdk.target.get().toInt()
    versionCode = 1
    versionName = "1.0.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  productFlavors {
    create("default")

    create("demo") {
      applicationIdSuffix = ".demo"
      versionNameSuffix = "-demo"
    }
  }
}

dependencies {
  "androidTestDemoImplementation"(project(":platform:ui-test"))
  "androidTestDemoImplementation"(libs.android.activity.ktx)
  "androidTestDemoImplementation"(libs.android.compose.ui.test.junit)
  "androidTestDemoImplementation"(libs.android.test.core)
  "androidTestDemoImplementation"(libs.android.test.espresso.intents)
  "androidTestDemoImplementation"(libs.android.test.runner)

  ksp(project(":std:injector-processor"))

  "demoImplementation"(project(":core-test"))

  implementation(project(":core:http"))
  implementation(project(":core:sample"))
  implementation(project(":core:shared-preferences"))
  implementation(project(":feature:composer"))
  implementation(project(":feature:feed"))
  implementation(project(":feature:profile-details"))
  implementation(project(":feature:search"))
  implementation(project(":feature:settings"))
  implementation(project(":feature:settings:term-muting"))
  implementation(project(":feature:toot-details"))
  implementation(project(":platform:launchable"))
  implementation(project(":platform:theme"))
  implementation(project(":platform:ui"))
  implementation(project(":std:injector"))
  implementation(libs.android.appcompat)
  implementation(libs.android.constraintlayout)
  implementation(libs.android.fragment.ktx)
  implementation(libs.android.material)
  implementation(libs.time4j)
}
