plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.symbolProcessor)

  `java-library`
}

dependencies {
  api(project(":core"))
  api(project(":std:injector"))

  ksp(project(":std:injector-processor"))
}
