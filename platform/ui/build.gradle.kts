plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  packagingOptions.resources.excludes +=
    arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")

  defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
}

dependencies {
  androidTestImplementation(kotlin("reflect"))
  androidTestImplementation(project(":core:sample-test"))
  androidTestImplementation(project(":platform:ui-test"))
  androidTestImplementation(project(":std:image-loader-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.mockk)

  api(project(":std:image-loader:compose"))
  api(libs.android.compose.foundation)
  api(libs.android.fragment.ktx)
  api(libs.loadable)

  implementation(project(":core"))
  implementation(project(":core:sample"))
  implementation(project(":platform:theme"))
  implementation(project(":std:image-loader:local"))
  implementation(libs.android.activity.compose)
  implementation(libs.android.material)
  implementation(libs.jsoup)
  implementation(libs.kotlin.reflect)
  implementation(libs.loadable.list)
  implementation(libs.loadable.placeholder)
  implementation(libs.loadable.placeholder.test)
  implementation(libs.time4j)

  testImplementation(libs.assertk)
  testImplementation(libs.junit)
}
