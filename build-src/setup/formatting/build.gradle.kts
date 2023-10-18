plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

dependencies { implementation(libs.spotless) }

gradlePlugin.plugins.register("setup-formatting") {
  id = libs.plugins.orca.build.setup.formatting.get().pluginId
  implementationClass = "com.jeanbarrossilva.orca.build.setup.formatting.FormattingSetupPlugin"
}
