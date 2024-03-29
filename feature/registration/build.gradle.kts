plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  packagingOptions.resources.excludes +=
    arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
}

dependencies {
  androidTestImplementation(project(":platform:autos-test"))
  androidTestImplementation(project(":platform:navigation"))
  androidTestImplementation(project(":platform:navigation-test"))
  androidTestImplementation(project(":std:injector-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.fragment.testing)
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.kotlin.test)
  androidTestImplementation(libs.mockk)

  api(project(":platform:navigation"))
  api(project(":std:injector"))

  implementation(project(":composite:composable"))
  implementation(project(":core:sample"))
  implementation(project(":platform:animator"))
  implementation(project(":platform:autos"))
  implementation(libs.android.core)

  ksp(project(":std:injector-processor"))

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
