plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
