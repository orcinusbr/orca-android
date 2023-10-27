import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  namespace = namespaceFor("feature.settings.termmuting")
}

dependencies {
  androidTestImplementation(project(":platform:theme-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.android.test.runner)

  ksp(project(":std:injector-processor"))

  implementation(project(":core"))
  implementation(project(":platform:theme"))
  implementation(project(":platform:ui"))
  implementation(project(":std:injector"))
}
