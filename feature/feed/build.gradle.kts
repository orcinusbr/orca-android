/*
 * Copyright © 2023-2024 Orca
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
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
  androidTestImplementation(project(":composite:timeline-test"))
  androidTestImplementation(project(":core:sample-test"))
  androidTestImplementation(project(":platform:core"))
  androidTestImplementation(project(":platform:testing"))
  androidTestImplementation(project(":std:injector-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)

  api(project(":composite:composable"))

  ksp(project(":std:injector-processor"))

  implementation(project(":composite:timeline"))
  implementation(project(":core:sample"))
  implementation(project(":ext:coroutines"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:intents"))
  implementation(project(":platform:navigation"))
  implementation(project(":std:injector"))
  implementation(libs.loadable.list)

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
}
