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
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(project(":platform:testing"))
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.kotlin.test)

  api(project(":std:markdown"))

  implementation(project(":ext:coroutines"))
  implementation(project(":ext:reflection"))
  implementation(libs.android.compose.ui)
  implementation(libs.android.compose.ui.tooling)
  implementation(libs.android.core)

  testImplementation(project(":platform:markdown-test"))
  testImplementation(project(":platform:testing"))
  testImplementation(project(":std:uri"))
  testImplementation(libs.android.compose.ui.test.junit)
  testImplementation(libs.android.compose.ui.test.manifest)
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.openTest4J)
  testImplementation(libs.robolectric)
}
