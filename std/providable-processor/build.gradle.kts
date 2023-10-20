plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  implementation(project(":std:providable"))
  implementation(libs.kotlin.symbolProcessor)
  implementation(libs.kotlinPoet)
  implementation(libs.kotlinPoet.ksp)

  testImplementation(libs.assertk)
  testImplementation(libs.kctfork.ksp)
  testImplementation(libs.kotlin.test)
}
