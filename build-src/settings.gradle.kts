include(":setup:formatting", ":setup:java")

dependencyResolutionManagement.versionCatalogs {
  register("libs") { from(files("../gradle/libs.versions.toml")) }
}
