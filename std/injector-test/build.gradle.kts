plugins {
  alias(libs.plugins.kotlin.jvm)

  id(libs.plugins.orca.build.setup.java.get().pluginId)

  `java-library`
}

dependencies {
  api(libs.junit)

  implementation(project(":std:injector"))

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
