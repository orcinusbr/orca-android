package com.jeanbarrossilva.orca.app.module.feature.composer

import com.jeanbarrossilva.orca.feature.composer.ComposerBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class FragmentManagerComposerBoundary(private val navigator: Navigator) :
    ComposerBoundary {
    override fun pop() {
        navigator.pop()
    }
}
