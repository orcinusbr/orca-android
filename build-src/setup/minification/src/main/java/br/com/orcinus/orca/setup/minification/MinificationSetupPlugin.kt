/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.setup.minification

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MinificationSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.subprojects { subProject ->
      subProject.afterEvaluate { evaluatedSubProject ->
        evaluatedSubProject.extensions
          .findByType(CommonExtension::class.java)
          ?.defaultConfig
          ?.proguardFile(evaluatedSubProject.rootDir.resolve("proguard-rules.pro"))
      }
    }
  }
}
