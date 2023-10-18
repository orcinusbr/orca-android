plugins {
  alias(libs.plugins.kotlin.jvm)

  id(libs.plugins.orca.build.setup.java.get().pluginId)

  `java-library`
}

dependencies {
  implementation(project(":std:injector"))
  implementation(libs.kotlin.compiler.embeddable)
  implementation(libs.kotlin.symbolProcessor)
  implementation(libs.kotlinPoet)
  implementation(libs.kotlinPoet.ksp)

  testImplementation(libs.assertk)
  testImplementation(libs.kctfork.ksp)
  testImplementation(libs.kotlin.test)
}
