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

import br.com.orcinus.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  namespace = namespaceFor("platform.navigation.test")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(libs.android.fragment.testing)
  api(libs.android.test.core)

  implementation(project(":ext:reflection"))
  implementation(project(":platform:navigation"))
  implementation(libs.assertk)

  testImplementation(project(":platform:testing"))
  testImplementation(libs.android.fragment.testing)
  testImplementation(libs.android.test.junit)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.openTest4J)
  testImplementation(libs.robolectric)
}
