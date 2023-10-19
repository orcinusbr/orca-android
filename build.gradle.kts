import com.jeanbarrossilva.orca.chrynan
import com.jeanbarrossilva.orca.loadable

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.maps.secrets) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.symbolProcessor) apply false
  alias(libs.plugins.moduleDependencyGraph)
  alias(libs.plugins.spotless)

  id(libs.plugins.orca.build.setup.android.library.get().pluginId)
  id(libs.plugins.orca.build.setup.formatting.get().pluginId)
  id(libs.plugins.orca.build.setup.java.get().pluginId)
  id(libs.plugins.orca.build.setup.kotlin.get().pluginId)
  id("build-src")
}

allprojects { repositories.mavenCentral() }

subprojects subproject@{
  repositories {
    chrynan()
    google()
    gradlePluginPortal()
    loadable(project)
  }
}

tasks.named("clean") { delete(rootProject.buildDir) }
