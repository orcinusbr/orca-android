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

plugins {
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

buildConfig {
  packageName("com.jeanbarrossilva.orca.setup.android.library")
  buildConfigField("String", "JAVA_VERSION", "\"${libs.versions.java.get()}\"")
  buildConfigField("String", "MIN_SDK_VERSION", "\"${libs.versions.android.sdk.min.get()}\"")
  buildConfigField("String", "TARGET_SDK_VERSION", "\"${libs.versions.android.sdk.target.get()}\"")
}

dependencies { implementation(libs.android.plugin) }

gradlePlugin.plugins.register("setup-android-library") {
  id = libs.plugins.orca.setup.android.library.get().pluginId
  implementationClass = "com.jeanbarrossilva.orca.setup.android.library.AndroidLibrarySetupPlugin"
}

repositories.google()
