plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.lint)

  `java-library`
}

dependencies {
  compileOnly(project(":std:package-internal"))
  compileOnly(libs.lint.api)

  testImplementation(project(":std:package-internal"))
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.lint.api)
  testImplementation(libs.lint.tests)
}
