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

import com.jeanbarrossilva.orca.autos
import com.jeanbarrossilva.orca.chrynan
import com.jeanbarrossilva.orca.loadable

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

  id(libs.plugins.orca.setup.android.library.get().pluginId)
  id(libs.plugins.orca.setup.formatting.get().pluginId)
  id(libs.plugins.orca.setup.hooks.get().pluginId)
  id(libs.plugins.orca.setup.java.get().pluginId)
  id(libs.plugins.orca.setup.kotlin.get().pluginId)
  id("build-src")
}

allprojects { repositories.mavenCentral() }

subprojects {
  repositories {
    autos(rootProject)
    chrynan()
    google()
    gradlePluginPortal()
    loadable(rootProject)
  }
}

tasks.named("clean") { delete(rootProject.buildDir) }
