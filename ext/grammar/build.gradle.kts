plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
