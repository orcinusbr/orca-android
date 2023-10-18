import com.jeanbarrossilva.orca.chrynan
import com.jeanbarrossilva.orca.loadable
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
  id("build-src")
}

allprojects { repositories.mavenCentral() }

subprojects subproject@{
  tasks.withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
      freeCompilerArgs.addAll("-Xstring-concat=inline")
    }
  }

  repositories {
    chrynan()
    google()
    gradlePluginPortal()
    loadable(project)
  }
}

tasks.named("clean") { delete(rootProject.buildDir) }
