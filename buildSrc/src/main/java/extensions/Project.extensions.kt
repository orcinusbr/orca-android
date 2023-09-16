import groovy.lang.MissingPropertyException
import org.gradle.api.Project

/**
 * Namespace of this [Project].
 *
 * @throws MissingPropertyException If the `project.namespace` property hasn't been set for this
 * [Project].
 **/
val Project.namespace
    get() = property("project.namespace").toString()

/**
 * [Namespace][namespace] that doesn't include this [Project]'s coordinates or the root [Project]'s.
 *
 * It's considered to be a candidate because it could have been named in a way that doesn't make it
 * eligible to be part of a proper namespace.
 **/
val Project.parentNamespaceCandidate
    get() = group.toString().removePrefix("${rootProject.name}.")

/**
 * Returns the namespace for a [Project] with the given [id].
 *
 * @param id ID to be put at the end of the namespace.
 **/
fun Project.namespaceFor(id: String): String {
    return "${rootProject.namespace}.$id"
}
