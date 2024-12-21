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
  alias(libs.plugins.kotlin.compose)
}

android {
  buildFeatures.compose = true
  packagingOptions.resources.excludes +=
    arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(project(":platform:autos"))
  api(project(":platform:core"))
  api(libs.loadable.list)

  implementation(project(":ext:reflection"))
  implementation(project(":platform:focus"))
  implementation(project(":std:visibility"))
  implementation(libs.android.constraintLayout.compose)
  implementation(libs.android.core)
  implementation(libs.jsoup)
  implementation(libs.loadable.placeholder)
  implementation(libs.loadable.placeholder.test)
  implementation(libs.time4j)

  testImplementation(project(":core:sample-test"))
  testImplementation(project(":ext:uri"))
  testImplementation(project(":platform:autos-test"))
  testImplementation(project(":composite:timeline-test"))
  testImplementation(project(":platform:testing"))
  testImplementation(project(":std:visibility"))
  testImplementation(libs.android.activity.compose)
  testImplementation(libs.android.compose.ui.test.manifest)
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.loadable.placeholder.test)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.turbine)
}
