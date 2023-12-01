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
