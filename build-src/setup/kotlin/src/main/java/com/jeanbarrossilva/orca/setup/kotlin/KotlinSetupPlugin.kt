package com.jeanbarrossilva.orca.setup.kotlin

import com.jeanbarrossilva.orca.build.setup.kotlin.BuildConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val javaVersion = JvmTarget.fromTarget(BuildConfig.JAVA_VERSION)
    target.subprojects { subProject ->
      subProject.tasks.withType(KotlinCompile::class.java) { kotlinCompile ->
        kotlinCompile.compilerOptions {
          jvmTarget.set(javaVersion)
          freeCompilerArgs.addAll("-Xstring-concat=inline")
        }
      }
    }
  }
}
