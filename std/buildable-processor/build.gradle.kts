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
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.symbolProcessor)

  `java-library`
}

dependencies {
  implementation(project(":ext:processing"))
  implementation(project(":std:buildable"))
  implementation(libs.kotlin.symbolProcessor)

  kspTest(project(":std:buildable-processor"))

  testImplementation(libs.assertk)
  testImplementation(libs.kctfork.ksp)
  testImplementation(libs.kotlin.reflect)
  testImplementation(libs.kotlin.test)
}
