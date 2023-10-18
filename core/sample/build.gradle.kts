plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.symbolProcessor)

  id(libs.plugins.orca.build.setup.java.get().pluginId)

  `java-library`
}

dependencies {
  api(project(":core-module"))

  ksp(project(":std:injector-processor"))

  testImplementation(project(":core:sample-test"))
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
}
