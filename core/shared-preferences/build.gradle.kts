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

  kotlin("plugin.serialization")
}

android {
  namespace = namespaceFor("core.sharedpreferences")
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
  androidTestImplementation(project(":core-test"))
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.kotlin.coroutines.test)
  androidTestImplementation(libs.turbine)

  implementation(project(":core"))
  implementation(libs.android.core)
  implementation(libs.kotlin.coroutines.android)
  implementation(libs.kotlin.serialization.json)
}
