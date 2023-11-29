rootProject.name = "Orca"

pluginManagement.repositories {
  google()
  gradlePluginPortal()
  mavenCentral()
}

includeBuild("build-src")

include(
  ":app",
  ":core",
  ":core:mastodon",
  ":core:sample",
  ":core:sample-test",
  ":core:shared-preferences",
  ":core-module",
  ":core-test",
  ":ext:processing",
  ":feature:composer",
  ":feature:feed",
  ":feature:post-details",
  ":feature:profile-details",
  ":feature:search",
  ":feature:settings",
  ":feature:settings:term-muting",
  ":platform:autos",
  ":platform:cache",
  ":platform:autos-test",
  ":platform:ui",
  ":platform:ui-test",
  ":std:buildable",
  ":std:buildable-processor",
  ":std:image-loader",
  ":std:image-loader:compose",
  ":std:image-loader:local",
  ":std:image-loader-test",
  ":std:injector",
  ":std:injector-processor",
  ":std:injector-test",
  ":std:styled-string"
)
