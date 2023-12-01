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

import com.jeanbarrossilva.orca.Dimensions
import com.jeanbarrossilva.orca.namespace
import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  compileSdk = libs.versions.android.sdk.target.get().toInt()
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  flavorDimensions += Dimensions.VERSION
  namespace = namespaceFor("app")

  buildFeatures {
    compose = true
    viewBinding = true
  }

  buildTypes { release { isMinifyEnabled = true } }

  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
  }

  defaultConfig {
    applicationId = project.namespace
    minSdk = libs.versions.android.sdk.min.get().toInt()
    targetSdk = libs.versions.android.sdk.target.get().toInt()
    versionCode = 1
    versionName = "1.0.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  productFlavors {
    register("default") { dimension = Dimensions.VERSION }

    register("demo") {
      applicationIdSuffix = ".demo"
      dimension = Dimensions.VERSION
      versionNameSuffix = "-demo"
    }
  }
}

dependencies {
  "androidTestDemoImplementation"(project(":core:sample-test"))
  "androidTestDemoImplementation"(project(":platform:ui"))
  "androidTestDemoImplementation"(libs.android.activity.ktx)
  "androidTestDemoImplementation"(libs.android.compose.ui.test.junit)
  "androidTestDemoImplementation"(libs.assertk)

  androidTestImplementation(project(":platform:ui-test"))
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.android.test.espresso.intents)

  ksp(project(":std:injector-processor"))

  implementation(project(":core:mastodon"))
  implementation(project(":core:sample"))
  implementation(project(":core:shared-preferences"))
  implementation(project(":feature:composer"))
  implementation(project(":feature:feed"))
  implementation(project(":feature:post-details"))
  implementation(project(":feature:profile-details"))
  implementation(project(":feature:search"))
  implementation(project(":feature:settings"))
  implementation(project(":feature:settings:term-muting"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:ui"))
  implementation(project(":std:image-loader:local"))
  implementation(project(":std:injector"))
  implementation(libs.android.appcompat)
  implementation(libs.android.constraintlayout)
  implementation(libs.android.fragment.ktx)
  implementation(libs.android.material)
  implementation(libs.time4j)
}
