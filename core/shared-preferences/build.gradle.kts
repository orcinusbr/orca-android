import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)

  kotlin("plugin.serialization")
}

android {
  namespace = namespaceFor("core.sharedpreferences")
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
  androidTestImplementation(project(":core-test"))
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.kotlin.coroutines.test)
  androidTestImplementation(libs.turbine)

  implementation(project(":core"))
  implementation(libs.android.core)
  implementation(libs.kotlin.coroutines.android)
  implementation(libs.kotlin.serialization.json)
}
