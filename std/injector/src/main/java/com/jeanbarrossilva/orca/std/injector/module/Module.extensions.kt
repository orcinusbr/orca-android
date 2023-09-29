package com.jeanbarrossilva.orca.std.injector.module

import com.jeanbarrossilva.orca.std.injector.module.Module.Scope

/**
 * Container for related dependencies.
 *
 * @param dependencies Performs the injection of dependencies within the given [Scope].
 **/
internal fun Module(dependencies: Module.Scope.() -> Unit): Module {
    return object : Module() {
        override val dependencies = dependencies
    }
}
