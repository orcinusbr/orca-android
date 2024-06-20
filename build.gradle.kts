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

import br.com.orcinus.orca.autos
import br.com.orcinus.orca.chrynan
import br.com.orcinus.orca.loadable

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.maps.secrets) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.symbolProcessor) apply false
  alias(libs.plugins.lint) apply false
  alias(libs.plugins.moduleDependencyGraph)
  alias(libs.plugins.spotless)

  id(libs.plugins.orca.setup.android.library.get().pluginId)
  id(libs.plugins.orca.setup.formatting.get().pluginId)
  id(libs.plugins.orca.setup.hooks.get().pluginId)
  id(libs.plugins.orca.setup.java.get().pluginId)
  id(libs.plugins.orca.setup.kotlin.get().pluginId)
  id(libs.plugins.orca.setup.lint.get().pluginId)
  id(libs.plugins.orca.setup.minification.get().pluginId)
  id("build-src")
}

allprojects { repositories.mavenCentral() }

subprojects {
  repositories {
    autos(rootProject)
    chrynan()
    google()
    gradlePluginPortal()
    loadable(rootProject)
  }
}

tasks.named("clean") { delete(rootProject.layout.buildDirectory) }
