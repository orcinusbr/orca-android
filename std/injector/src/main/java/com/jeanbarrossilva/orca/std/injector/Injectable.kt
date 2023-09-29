package com.jeanbarrossilva.orca.std.injector

/**
 * Defines a dependency that can be lazily injected into the [Injector].
 *
 * @param T Dependency to be injected.
 **/
fun interface Injectable<T : Any> {
    /** Gets the dependency to be injected. **/
    context(Injector)
    fun getDependency(): T
}
