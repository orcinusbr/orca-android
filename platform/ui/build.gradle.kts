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
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()

  defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
}

dependencies {
  androidTestImplementation(kotlin("reflect"))
  androidTestImplementation(project(":core:sample-test"))
  androidTestImplementation(project(":platform:ui-test"))
  androidTestImplementation(project(":std:image-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.android.constraintlayout.compose)
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.assertk)

  api(project(":platform:starter:lifecycle"))
  api(project(":std:image:compose"))
  api(libs.android.compose.foundation)
  api(libs.android.fragment.ktx)
  api(libs.loadable)

  implementation(project(":core"))
  implementation(project(":core:sample"))
  implementation(project(":ext:coroutines"))
  implementation(project(":platform:autos"))
  implementation(project(":std:image:compose"))
  implementation(libs.android.compose.material)
  implementation(libs.android.material)
  implementation(libs.jsoup)
  implementation(libs.kotlin.reflect)
  implementation(libs.loadable.list)
  implementation(libs.loadable.placeholder)
  implementation(libs.loadable.placeholder.test)
  implementation(libs.orbital)
  implementation(libs.time4j)

  testImplementation(project(":core:sample-test"))
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
}

kotlin.compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
