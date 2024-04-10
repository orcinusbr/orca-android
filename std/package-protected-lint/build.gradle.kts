plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.lint)

  `java-library`
}

dependencies {
  compileOnly(project(":std:package-protected"))
  compileOnly(libs.lint.api)

  testImplementation(project(":std:package-protected"))
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.lint.api)
  testImplementation(libs.lint.tests)
}
