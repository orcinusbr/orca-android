/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
