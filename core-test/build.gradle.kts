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

plugins {
  alias(libs.plugins.kotlin.jvm)

  `java-library`
}

dependencies {
  implementation(project(":core"))
  implementation(project(":std:image-test"))

  testImplementation(project(":core:sample"))
  testImplementation(libs.assertk)
  testImplementation(libs.assertk.coroutines)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
}
