package com.jeanbarrossilva.orca.std.injector.module

import com.jeanbarrossilva.orca.std.injector.Injector

/** Container for related dependencies. **/
abstract class Module {
    /** [Scope] within which injections to this [Module] will be done. **/
    private val scope = Scope()

    /** Injection operations registered within the [scope]. **/
    @PublishedApi
    internal val injections = mutableListOf<() -> Unit>()

    /** Performs the injection of dependencies within the given [Scope]. **/
    protected abstract val dependencies: Scope.() -> Unit

    /** Context through which injections can be made. **/
    inner class Scope internal constructor() {
        /**
         * Registers the dependency of the given [injection] and injects it only when the [Module]'s
         * [inject][Module.inject] method is called.
         *
         * @param injection Returns the dependency to be injected.
         **/
        inline fun <reified T : Any> inject(noinline injection: Injector.() -> T) {
            injections.add {
                Injector.inject(injection)
            }
        }
    }

    /** Injects all lazily registered dependencies. **/
    fun inject() {
        scope.apply(dependencies)
        injections.forEach { it() }
    }
}
