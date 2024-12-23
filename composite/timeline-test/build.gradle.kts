import br.com.orcinus.orca.namespaceFor

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
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  namespace = namespaceFor("composite.timeline.test")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(libs.android.compose.ui.test.manifest)

  api(libs.android.compose.ui.test.junit)

  testImplementation(project(":core:sample"))
  testImplementation(project(":platform:autos-test"))
  testImplementation(libs.android.compose.ui.test.manifest)
  testImplementation(libs.assertk)
  testImplementation(libs.robolectric)

  implementation(project(":composite:timeline"))
}
