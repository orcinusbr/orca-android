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

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  packagingOptions.resources.excludes +=
    arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
}

dependencies {
  androidTestImplementation(project(":composite:timeline-test"))
  androidTestImplementation(project(":core:sample-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.kotlin.coroutines.test)
  androidTestImplementation(libs.mockk)
  androidTestImplementation(libs.turbine)

  api(project(":platform:autos"))
  api(project(":platform:core"))
  api(project(":std:styled-string"))

  implementation(libs.android.compose.material)
  implementation(libs.android.core)
  implementation(libs.jsoup)
  implementation(libs.loadable.list)
  implementation(libs.loadable.placeholder)
  implementation(libs.loadable.placeholder.test)
  implementation(libs.time4j)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
