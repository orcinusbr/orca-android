plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  api(project(":std:image-loader"))
  api(project(":std:styled-string"))
  api(libs.kotlin.coroutines.core)

  testImplementation(project(":core:sample"))
  testImplementation(project(":core-test"))
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.turbine)
}
