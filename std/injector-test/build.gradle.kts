plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  api(libs.junit)

  implementation(project(":std:injector"))

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
