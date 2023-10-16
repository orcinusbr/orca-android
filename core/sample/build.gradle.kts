plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.symbolProcessor)

  `java-library`
}

dependencies {
  api(project(":core-module"))

  ksp(project(":std:injector-processor"))

  testImplementation(project(":core:sample-test"))
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
}
