import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  namespace = namespaceFor("feature.tootdetails")
}

dependencies {
  ksp(project(":std:injector-processor"))

  implementation(project(":core"))
  implementation(project(":core:sample"))
  implementation(project(":platform:theme"))
  implementation(project(":platform:ui"))
  implementation(project(":std:injector"))
  implementation(libs.android.lifecycle.viewmodel)
  implementation(libs.loadable.list)
  implementation(libs.loadable.placeholder)
}
