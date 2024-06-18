/*
 * Copyright © 2023–2024 Orcinus
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

  kotlin("plugin.serialization")
}

android {
  namespace = namespaceFor("core.sharedpreferences")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(project(":core"))

  implementation(libs.android.core)
  implementation(libs.android.lifecycle.runtime)
  implementation(libs.kotlin.coroutines.android)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.serialization.json)

  testImplementation(project(":core:sample"))
  testImplementation(project(":core:sample-test"))
  testImplementation(project(":core-test"))
  testImplementation(project(":ext:reflection"))
  testImplementation(project(":ext:uri"))
  testImplementation(project(":platform:testing"))
  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.turbine)
}
