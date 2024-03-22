plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.kotlin.test)

  implementation(project(":core:sample"))
  implementation(project(":platform:animator"))
  implementation(project(":platform:autos"))
  implementation(libs.android.core)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
