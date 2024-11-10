import br.com.orcinus.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = namespaceFor("core.mastodon.test")
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  packagingOptions.resources.excludes +=
    arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(libs.kotlin.test)
  androidTestImplementation(libs.mockk)

  implementation(project(":core:mastodon"))
  implementation(project(":platform:core"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:starter:lifecycle"))
  implementation(libs.android.test.core)
  implementation(libs.unifiedPush.connector)

  testImplementation(project(":platform:testing"))
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.robolectric)
}
