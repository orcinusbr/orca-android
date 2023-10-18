import com.android.build.gradle.LibraryExtension
import com.jeanbarrossilva.orca.chrynan
import com.jeanbarrossilva.orca.loadable
import com.jeanbarrossilva.orca.namespace
import com.jeanbarrossilva.orca.parentNamespaceCandidate
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

  afterEvaluate {
    with(JavaVersion.toVersion(libs.versions.java.get())) java@{
      extensions.findByType<LibraryExtension>()?.apply {
        compileSdk = libs.versions.android.sdk.target.get().toInt()
        defaultConfig.minSdk = libs.versions.android.sdk.min.get().toInt()
        setDefaultNamespaceIfUnsetAndEligible(this@subproject)

        buildTypes { release { isMinifyEnabled = true } }

        compileOptions {
          sourceCompatibility = this@java
          targetCompatibility = this@java
        }
      }
    }
  }
}

tasks.named("clean") { delete(rootProject.buildDir) }

/**
 * Sets a default [namespace] to the [project] if it doesn't have one and the [project]'s coordinate
 * candidates are eligible to be part of the one to be set.
 *
 * @param project [Project] whose [namespace] will be set if unset.
 * @see isEligibleForNamespace
 */
fun LibraryExtension.setDefaultNamespaceIfUnsetAndEligible(project: Project) {
  if (
    namespace == null &&
      isEligibleForNamespace(project.name) &&
      isEligibleForNamespace(project.parentNamespaceCandidate)
  ) {
    namespace = createDefaultNamespace(project)
  }
}

/**
 * Whether the [coordinates] are valid namespace coordinates.
 *
 * @param coordinates Dot-separated coordinates whose eligibility will be checked.
 */
fun isEligibleForNamespace(coordinates: String): Boolean {
  return coordinates.all { it.isLetterOrDigit() || it == '.' }
}

/**
 * Creates a default [namespace] for the [project].
 *
 * @param project [Project] for which the default [namespace] will be created.
 * @return Created [namespace].
 */
fun createDefaultNamespace(project: Project): String {
  return "${rootProject.namespace}." + project.parentNamespaceCandidate + ".${project.name}"
}
