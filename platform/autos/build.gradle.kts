/*
 * Copyright © 2023–2024 Orcinus
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
  alias(libs.plugins.kotlin.compose)
}

android {
  buildFeatures.compose = true
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(project(":platform:autos-test"))
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.kotlin.test)

  api(project(":std:markdown"))
  api(libs.android.compose.material3)
  api(libs.android.compose.ui.tooling)
  api(libs.android.material)
  api(libs.autos)

  implementation(project(":ext:coroutines"))
  implementation(project(":ext:reflection"))
  implementation(project(":std:visibility"))
  implementation(libs.accompanist.adapter)
  implementation(libs.loadable.placeholder)

  testImplementation(project(":ext:uri"))
  testImplementation(project(":platform:autos-test"))
  testImplementation(project(":platform:navigation"))
  testImplementation(project(":platform:testing"))
  testImplementation(libs.android.compose.ui.test.manifest)
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.mockk)
  testImplementation(libs.openTest4J)
  testImplementation(libs.robolectric)
}
