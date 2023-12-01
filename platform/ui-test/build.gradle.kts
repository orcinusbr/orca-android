/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = namespaceFor("platform.ui.test")
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

  buildFeatures {
    compose = true
    viewBinding = true
  }
}

dependencies {
  androidTestImplementation(project(":platform:autos"))
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.assertk)

  api(libs.android.navigation.fragment)

  implementation(project(":platform:ui"))
  implementation(libs.android.compose.ui.test.junit)
  implementation(libs.time4j)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.test)
}
