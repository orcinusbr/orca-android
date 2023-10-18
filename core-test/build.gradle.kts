plugins {
  alias(libs.plugins.kotlin.jvm)

  id(libs.plugins.orca.build.setup.java.get().pluginId)

  `java-library`
}

dependencies { implementation(project(":core")) }
