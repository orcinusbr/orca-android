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
