plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  api(libs.assertk)

  implementation(project(":std:func"))
  implementation(libs.openTest4J)

  testImplementation(libs.kotlin.test)
}
