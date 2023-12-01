/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.setup.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val jvmTarget = JvmTarget.fromTarget(BuildConfig.JAVA_VERSION)
    target.subprojects { subProject ->
      subProject.tasks.withType(KotlinCompile::class.java) { kotlinCompile ->
        kotlinCompile.compilerOptions {
          this.jvmTarget.set(jvmTarget)
          freeCompilerArgs.addAll("-Xstring-concat=inline")
        }
      }
    }
  }
}
