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
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  packagingOptions.resources.excludes +=
    arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(project(":platform:testing"))
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.kotlin.coroutines.test)
  androidTestImplementation(libs.kotlin.test)
  androidTestImplementation(libs.mockk)

  api(libs.android.fragment)

  debugImplementation(libs.kotlin.coroutines.core)

  implementation(libs.kotlin.reflect)

  testImplementation(project(":platform:testing"))
  testImplementation(libs.kotlin.test)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
}
