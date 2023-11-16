plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  implementation(libs.kotlin.coroutines.core)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
}
