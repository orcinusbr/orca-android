plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  implementation(project(":ext:processing"))
  implementation(project(":std:injector"))

  testImplementation(libs.assertk)
  testImplementation(libs.kctfork.ksp)
  testImplementation(libs.kotlin.test)
}
