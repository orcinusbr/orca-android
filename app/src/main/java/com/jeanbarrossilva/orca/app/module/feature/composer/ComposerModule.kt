package com.jeanbarrossilva.orca.app.module.feature.composer

import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.composer.ComposerBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun ComposerModule(fragmentManager: FragmentManager): Module {
    return module {
        single<ComposerBoundary> {
            FragmentManagerComposerBoundary(fragmentManager)
        }
    }
}
