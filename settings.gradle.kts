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

rootProject.name = "Orca"

pluginManagement.repositories {
  google()
  gradlePluginPortal()
  mavenCentral()
}

includeBuild("build-src")

include(
  ":app",
  ":core",
  ":core:mastodon",
  ":core:sample",
  ":core:sample-test",
  ":core:shared-preferences",
  ":core-module",
  ":core-test",
  ":ext:processing",
  ":feature:composer",
  ":feature:feed",
  ":feature:post-details",
  ":feature:profile-details",
  ":feature:search",
  ":feature:settings",
  ":feature:settings:term-muting",
  ":platform:autos",
  ":platform:cache",
  ":platform:autos-test",
  ":platform:ui",
  ":platform:ui-test",
  ":std:buildable",
  ":std:buildable-processor",
  ":std:image-loader",
  ":std:image-loader:compose",
  ":std:image-loader:local",
  ":std:image-loader-test",
  ":std:injector",
  ":std:injector-processor",
  ":std:injector-test",
  ":std:styled-string"
)
