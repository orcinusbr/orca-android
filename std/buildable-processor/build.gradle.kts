plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  implementation(project(":ext:processing"))
  implementation(project(":std:buildable"))
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.symbolProcessor)

  testImplementation(libs.assertk)
  testImplementation(libs.kctfork.ksp)
  testImplementation(libs.kotlin.reflect)
  testImplementation(libs.kotlin.test)
}
