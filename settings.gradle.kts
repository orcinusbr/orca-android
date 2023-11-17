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
  ":feature:profile-details",
  ":feature:search",
  ":feature:settings",
  ":feature:settings:term-muting",
  ":feature:toot-details",
  ":platform:cache",
  ":platform:theme",
  ":platform:theme-test",
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
