plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
}

dependencies {
  ksp(project(":std:injector-processor"))

  implementation(project(":core"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:ui"))
  implementation(project(":std:injector"))
}
