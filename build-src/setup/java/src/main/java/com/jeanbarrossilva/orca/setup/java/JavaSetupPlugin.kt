package com.jeanbarrossilva.orca.setup.java

import com.jeanbarrossilva.orca.build.setup.java.BuildConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

class JavaSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.subprojects { subProject ->
      subProject.afterEvaluate { evaluatedSubProject ->
        evaluatedSubProject.extensions.findByType(JavaPluginExtension::class.java)?.apply {
          val javaVersionAsString = BuildConfig.JAVA_VERSION
          val javaVersion = JavaVersion.toVersion(javaVersionAsString)
          sourceCompatibility = javaVersion
          targetCompatibility = javaVersion
        }
      }
    }
  }
}
