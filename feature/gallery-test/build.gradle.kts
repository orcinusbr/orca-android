import com.jeanbarrossilva.orca.namespaceFor

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
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  namespace = namespaceFor("feature.gallery.test")
}

dependencies {
  androidTestImplementation(libs.assertk)

  implementation(project(":composite:timeline"))
  implementation(project(":feature:gallery"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:testing"))
  implementation(project(":platform:ui-test"))
  implementation(libs.android.compose.ui.test.junit)
  implementation(libs.android.compose.ui.test.manifest)
  implementation(libs.android.test.espresso.core)
  implementation(libs.loadable.placeholder.test)
}

kotlin.compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
