import br.com.orcinus.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = namespaceFor("platform.ime.test")
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(libs.kotlin.test)
  androidTestImplementation(libs.android.test.runner)

  api(libs.assertk)
  api(libs.kotlin.coroutines.test)

  implementation(project(":ext:coroutines"))
  implementation(project(":platform:ime"))
  implementation(project(":platform:testing"))
  implementation(libs.android.activity.ktx)
  implementation(libs.android.test.core)

  testImplementation(libs.kotlin.test)
  testImplementation(libs.openTest4J)
  testImplementation(libs.robolectric)
}
