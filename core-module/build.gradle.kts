plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.symbolProcessor)

  id(libs.plugins.orca.build.setup.java.get().pluginId)

  `java-library`
}

dependencies {
  api(project(":core"))
  api(project(":std:injector"))

  ksp(project(":std:injector-processor"))
}
