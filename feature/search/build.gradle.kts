/*
 * Copyright © 2023-2024 Orcinus
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
  alias(libs.plugins.kotlin.symbolProcessor)
}

android.buildFeatures.compose = true

dependencies {
  api(project(":composite:composable"))
  api(project(":platform:navigation"))

  ksp(project(":std:injector-processor"))

  implementation(project(":composite:timeline"))
  implementation(project(":core:sample"))
  implementation(project(":platform:core"))
  implementation(project(":platform:focus"))
  implementation(project(":platform:starter"))
  implementation(project(":std:injector"))
  implementation(libs.android.fragment.ktx)
  implementation(libs.android.lifecycle.viewmodel)
  implementation(libs.loadable.list)
  implementation(libs.loadable.placeholder)
}
