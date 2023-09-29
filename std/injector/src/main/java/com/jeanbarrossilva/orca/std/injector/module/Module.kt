package com.jeanbarrossilva.orca.std.injector.module

import com.jeanbarrossilva.orca.std.injector.Injectable
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
         * Registers the injection of the given [injectable] and injects it only when the [Module]'s
         * [inject][Module.inject] method is called.
         **/
        inline fun <reified T : Any> inject(injectable: Injectable<T>) {
            injections.add {
                Injector.inject(injectable)
            }
        }
    }

    /** Injects all lazily registered dependencies. **/
    fun inject() {
        scope.apply(dependencies)
        injections.forEach { it() }
    }
}
