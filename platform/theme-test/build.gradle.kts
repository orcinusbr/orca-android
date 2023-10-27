import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android.namespace = namespaceFor("platform.theme.test")

dependencies {
  implementation(project(":platform:theme"))
  implementation(libs.android.compose.ui.test.junit)
}
