import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = namespaceFor("platform.ui.test")
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

  buildFeatures {
    compose = true
    viewBinding = true
  }
}

dependencies {
  androidTestImplementation(project(":platform:theme"))
  androidTestImplementation(libs.android.test.core)

  api(libs.android.navigation.fragment)

  implementation(project(":platform:ui"))
  implementation(libs.android.compose.ui.test.junit)
  implementation(libs.time4j)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
