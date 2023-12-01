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
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

dependencies { implementation(libs.spotless) }

gradlePlugin.plugins.register("setup-formatting") {
  id = libs.plugins.orca.setup.formatting.get().pluginId
  implementationClass = "com.jeanbarrossilva.orca.setup.formatting.FormattingSetupPlugin"
}
