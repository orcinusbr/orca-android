import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)

  kotlin("plugin.serialization")
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  namespace = namespaceFor("std.imageloader.compose")
}

dependencies {
  api(project(":std:image-loader"))
  api(libs.android.compose.ui.tooling)

  implementation(project(":platform:theme"))
  implementation(project(":std:image-loader:local"))
  implementation(libs.android.core)
  implementation(libs.coil)
  implementation(libs.loadable.placeholder)
}
