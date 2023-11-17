plugins {
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

dependencies { implementation(libs.kotlin.gradlePlugin) }

gradlePlugin.plugins.register("setup-hooks") {
  id = libs.plugins.orca.setup.hooks.get().pluginId
  implementationClass = "com.jeanbarrossilva.orca.setup.hooks.HooksSetupPlugin"
}
