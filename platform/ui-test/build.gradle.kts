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

import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = namespaceFor("platform.ui.test")
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

  buildFeatures {
    compose = true
    viewBinding = true
  }
}

dependencies {
  androidTestImplementation(project(":platform:autos"))
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.assertk)

  api(libs.android.navigation.fragment)

  implementation(project(":platform:ui"))
  implementation(libs.android.compose.ui.test.junit)
  implementation(libs.time4j)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
