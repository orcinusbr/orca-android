plugins {
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

buildConfig {
  packageName("com.jeanbarrossilva.orca.setup.kotlin")
  buildConfigField("String", "JAVA_VERSION", "\"${libs.versions.java.get()}\"")
}

dependencies { implementation(libs.kotlin.gradlePlugin) }

gradlePlugin.plugins.register("setup-kotlin") {
  id = libs.plugins.orca.setup.kotlin.get().pluginId
  implementationClass = "com.jeanbarrossilva.orca.setup.kotlin.KotlinSetupPlugin"
}
