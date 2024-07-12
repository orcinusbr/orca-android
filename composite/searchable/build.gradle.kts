plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android {
  buildFeatures.compose = true
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  implementation(project(":platform:autos"))
  implementation(project(":platform:focus"))
  implementation(libs.android.constraintLayout.compose)

  testImplementation(libs.android.compose.ui.test.junit)
  testImplementation(libs.android.compose.ui.test.manifest)
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.robolectric)
}
