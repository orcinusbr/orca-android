package com.jeanbarrossilva.orca.std.injector.module

import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * Denotes that the dependency returned by the value of the annotated property should be
 * automatically injected into the [Module] in which it was declared and that an extension property
 * for getting its result should be created for that same [Module].
 *
 * For example, declaring `class MyModule(@Inject val dependency: Module.() -> Int)` and registering
 * it in the [Injector] injects `dependency` into `MyModule` and makes its provided `Int` accessible
 * via a `MyModule.dependency` extension.
 *
 * Note that the annotated property should return a `Module.() -> Any`, which is how an injection
 * is recognized; otherwise, an error will be thrown at compile time.
 *
 * @see Injector.register
 **/
@Target(AnnotationTarget.PROPERTY)
annotation class Inject
