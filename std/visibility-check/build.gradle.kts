plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.lint)

  `java-library`
}

dependencies {
  compileOnly(project(":std:visibility"))
  compileOnly(libs.lint.api)
  testImplementation(project(":std:visibility"))

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.lint.api)
  testImplementation(libs.lint.tests)
}
