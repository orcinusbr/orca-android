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
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(project(":platform:navigation"))
  api(project(":std:injector"))

  implementation(project(":composite:composable"))
  implementation(project(":composite:status"))
  implementation(project(":core:sample"))
  implementation(project(":platform:animator"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:stack"))

  ksp(project(":std:injector-processor"))

  testImplementation(project(":platform:autos-test"))
  testImplementation(project(":platform:navigation"))
  testImplementation(project(":platform:navigation-test"))
  testImplementation(project(":std:injector-test"))
  testImplementation(libs.android.compose.ui.test.junit)
  testImplementation(libs.android.fragment.testing)
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
}
