package com.jeanbarrossilva.orca.std.injector.module

import kotlin.reflect.KProperty

/**
 * Denotes that the value of the annotated [KProperty] should be injected into the [Module] in which
 * it was declared.
 **/
@Target(AnnotationTarget.PROPERTY)
annotation class Dependency
