/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
