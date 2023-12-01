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

package com.jeanbarrossilva.orca

import groovy.lang.MissingPropertyException
import org.gradle.api.Project

/**
 * Namespace of this [Project].
 *
 * @throws MissingPropertyException If the `project.namespace` property hasn't been set for this
 *   [Project].
 */
val Project.namespace
  get() = property("project.namespace").toString()

/**
 * Returns the namespace for a [Project] with the given [id].
 *
 * @param id ID to be put at the end of the namespace.
 */
fun Project.namespaceFor(id: String): String {
  return "${rootProject.namespace}.$id"
}
