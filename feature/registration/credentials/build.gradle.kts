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
  androidTestImplementation(project(":platform:navigation-test"))
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.kotlin.test)

  api(project(":composite:composable"))
  api(project(":platform:navigation"))

  implementation(project(":platform:autos"))
}
