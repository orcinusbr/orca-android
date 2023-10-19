include(":setup:android-library", ":setup:formatting", ":setup:java", ":setup:kotlin")

dependencyResolutionManagement.versionCatalogs {
  register("libs") { from(files("../gradle/libs.versions.toml")) }
}
