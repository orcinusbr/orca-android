package com.jeanbarrossilva.orca.app.module.feature.composer

import com.jeanbarrossilva.orca.feature.composer.ComposerBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun ComposerModule(navigator: Navigator): Module {
    return module {
        single<ComposerBoundary> {
            FragmentManagerComposerBoundary(navigator)
        }
    }
}
