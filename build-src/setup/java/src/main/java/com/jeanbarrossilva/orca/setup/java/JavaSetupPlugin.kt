package com.jeanbarrossilva.orca.setup.java

import com.jeanbarrossilva.orca.build.setup.java.BuildConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

class JavaSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val javaVersion = JavaVersion.toVersion(BuildConfig.JAVA_VERSION)
    target.subprojects { subProject ->
      subProject.afterEvaluate { evaluatedSubProject ->
        evaluatedSubProject.extensions.findByType(JavaPluginExtension::class.java)?.apply {
          sourceCompatibility = javaVersion
          targetCompatibility = javaVersion
        }
      }
    }
  }
}
