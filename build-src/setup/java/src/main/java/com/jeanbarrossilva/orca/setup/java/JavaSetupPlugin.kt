/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.setup.java

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
