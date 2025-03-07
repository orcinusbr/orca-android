plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  testImplementation(project(":std:func-test"))
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
