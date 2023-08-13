package com.jeanbarrossilva.orca.feature.composer.test

import com.jeanbarrossilva.orca.feature.composer.ComposerBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun ComposerModule(): Module {
    return module {
        single<ComposerBoundary> {
            TestComposerBoundary()
        }
    }
}
