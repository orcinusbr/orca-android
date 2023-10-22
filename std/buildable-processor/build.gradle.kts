plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  implementation(project(":std:buildable"))
  implementation(libs.kotlin.symbolProcessor)
  implementation(libs.kotlinPoet.ksp)

  testImplementation(libs.assertk)
  testImplementation(libs.kctfork.ksp)
  testImplementation(libs.kotlin.reflect)
  testImplementation(libs.kotlin.test)
}
