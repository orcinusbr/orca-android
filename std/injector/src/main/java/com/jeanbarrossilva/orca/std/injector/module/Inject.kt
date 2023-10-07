package com.jeanbarrossilva.orca.std.injector.module

import kotlin.reflect.KProperty

/**
 * Denotes that the dependency returned by the value of the annotated [KProperty] should be
 * automatically injected into the [Module] in which it was declared.
 *
 * Note that the annotated [KProperty] should return a `Module.() -> Any`, which is how an injection
 * is recognized. If it doesn't, automatic injection won't be performed and the dependency will
 * have to be injected manually by calling [inject][Module.inject] on the [Module] in which it is
 * supposed to be.
 **/
@Target(AnnotationTarget.PROPERTY)
annotation class Inject
