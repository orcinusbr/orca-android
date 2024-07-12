plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android.buildFeatures.compose = true

dependencies {
  implementation(project(":platform:autos"))
  implementation(project(":platform:focus"))
  implementation(libs.android.constraintLayout.compose)
}
