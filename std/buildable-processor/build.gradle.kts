plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.symbolProcessor)

  `java-library`
}

dependencies {
  implementation(project(":ext:processing"))
  implementation(project(":std:buildable"))
  implementation(libs.kotlin.symbolProcessor)

  kspTest(project(":std:buildable-processor"))

  testImplementation(libs.assertk)
  testImplementation(libs.kctfork.ksp)
  testImplementation(libs.kotlin.reflect)
  testImplementation(libs.kotlin.test)
}
