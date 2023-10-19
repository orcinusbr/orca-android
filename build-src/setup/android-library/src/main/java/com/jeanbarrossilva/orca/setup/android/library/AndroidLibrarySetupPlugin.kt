package com.jeanbarrossilva.orca.setup.android.library

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibrarySetupPlugin : Plugin<Project> {
  private val Project.parentNamespaceCandidate
    get() = group.toString().removePrefix("${rootProject.name}.")

  override fun apply(target: Project) {
    val javaVersion = JavaVersion.toVersion(BuildConfig.JAVA_VERSION)
    target.subprojects { subProjects ->
      subProjects.afterEvaluate { evaluatedSubProject ->
        evaluatedSubProject.extensions.findByType(LibraryExtension::class.java)?.apply {
          compileSdk = BuildConfig.TARGET_SDK_VERSION.toInt()
          defaultConfig.minSdk = BuildConfig.MIN_SDK_VERSION.toInt()
          setDefaultNamespaceIfUnsetAndEligible(evaluatedSubProject)

          buildTypes { release { isMinifyEnabled = true } }

          compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
          }
        }
      }
    }
  }

  private fun LibraryExtension.setDefaultNamespaceIfUnsetAndEligible(project: Project) {
    if (
      namespace == null &&
        isEligibleForNamespace(project.name) &&
        isEligibleForNamespace(project.parentNamespaceCandidate)
    ) {
      namespace = createDefaultNamespace(project)
    }
  }

  private fun isEligibleForNamespace(coordinates: String): Boolean {
    return coordinates.all { it.isLetterOrDigit() || it == '.' }
  }

  private fun createDefaultNamespace(project: Project): String {
    val rootProjectNamespace = project.rootProject.property("project.namespace").toString()
    return "$rootProjectNamespace.${project.parentNamespaceCandidate}.${project.name}"
  }
}
