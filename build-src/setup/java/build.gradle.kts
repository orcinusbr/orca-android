plugins {
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

buildConfig {
  packageName("com.jeanbarrossilva.orca.setup.java")
  buildConfigField("String", "JAVA_VERSION", "\"${libs.versions.java.get()}\"")
}

gradlePlugin.plugins.register("setup-java") {
  id = libs.plugins.orca.setup.java.get().pluginId
  implementationClass = "com.jeanbarrossilva.orca.setup.java.JavaSetupPlugin"
}
