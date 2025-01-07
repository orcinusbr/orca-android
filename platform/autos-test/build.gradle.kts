/*
 * Copyright © 2023–2025 Orcinus
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
  alias(libs.plugins.kotlin.compose)
}

android {
  buildFeatures.compose = true
  namespace = namespaceFor("platform.autos.test")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(libs.android.compose.ui.test.junit)
  api(libs.android.test.espresso.core)

  compileOnly(libs.android.compose.material)

  implementation(project(":ext:reflection"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:testing"))

  testImplementation(libs.android.compose.ui.test.manifest)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.robolectric)
}
