import br.com.orcinus.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android.namespace = namespaceFor("feature.profiledetails.test")

dependencies {
  api(project(":std:injector"))

  implementation(project(":feature:profile-details"))
  implementation(project(":platform:core"))

  ksp(project(":std:injector-processor"))
}
