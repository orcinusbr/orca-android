plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  implementation(project(":core:sample"))
  implementation(kotlin("test-junit"))
}
