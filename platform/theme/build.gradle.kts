plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
  androidTestImplementation(project(":platform:theme-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.assertk)

  api(libs.android.compose.material)
  api(libs.android.compose.ui.tooling)

  implementation(kotlin("stdlib"))
  implementation(libs.accompanist.adapter)
  implementation(libs.android.material)
  implementation(libs.kotlin.reflect)
  implementation(libs.loadable.placeholder)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
