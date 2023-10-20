package com.jeanbarrossilva.orca.std.buildable

/**
 * Denotes that the annotated class should be able to be built through a builder from which each one
 * of its publicly declared members.
 */
@Target(AnnotationTarget.CLASS) annotation class Buildable
