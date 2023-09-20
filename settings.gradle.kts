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
    ":core-http",
    ":core-http:android",
    ":core-test",
    ":feature:auth",
    ":feature:composer",
    ":feature:feed",
    ":feature:profile-details",
    ":feature:search",
    ":feature:toot-details",
    ":platform:cache",
    ":platform:launchable",
    ":platform:theme",
    ":platform:ui",
    ":platform:ui-test",
    ":std:image-loader",
    ":std:image-loader:compose",
    ":std:image-loader-test",
    ":std:styled-string"
)
