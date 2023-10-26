plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  api(libs.kotlin.compiler.embeddable)
  api(libs.kotlin.symbolProcessor)
  api(libs.kotlinPoet.ksp)
}
