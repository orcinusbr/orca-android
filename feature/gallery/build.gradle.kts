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

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  kotlin.compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
}

dependencies {
  androidTestImplementation(project(":platform:ui-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.android.test.espresso.core)
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.loadable.placeholder.test)

  implementation(project(":core"))
  implementation(project(":core:sample"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:ui"))
  implementation(project(":std:image-loader:compose"))
  implementation(project(":std:image-loader:local"))
  implementation(libs.zoomable)
}
