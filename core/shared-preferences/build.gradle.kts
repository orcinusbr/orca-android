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

import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)

  kotlin("plugin.serialization")
}

android {
  namespace = namespaceFor("core.sharedpreferences")
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
  androidTestImplementation(project(":core:sample"))
  androidTestImplementation(project(":core:sample-test"))
  androidTestImplementation(project(":core-test"))
  androidTestImplementation(project(":ext:reflection"))
  androidTestImplementation(project(":platform:testing"))
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.assertk)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.kotlin.coroutines.test)
  androidTestImplementation(libs.turbine)

  implementation(project(":core"))
  implementation(libs.android.core)
  implementation(libs.android.lifecycle.runtime)
  implementation(libs.kotlin.coroutines.android)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.serialization.json)
}
