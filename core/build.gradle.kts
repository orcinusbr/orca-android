plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.symbolProcessor)

  `java-library`
}

dependencies {
  api(project(":std:image-loader"))
  api(project(":std:styled-string"))
  api(libs.kotlin.coroutines.core)

  implementation(project(":std:buildable"))

  ksp(project(":std:buildable-processor"))

  testImplementation(project(":core:sample"))
  testImplementation(project(":core:sample-test"))
  testImplementation(project(":core-test"))
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.turbine)
}
