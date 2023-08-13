package com.jeanbarrossilva.orca.app.module.feature.composer

import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.composer.ComposerBoundary

internal class FragmentManagerComposerBoundary(private val fragmentManager: FragmentManager) :
    ComposerBoundary {
    override fun pop() {
        fragmentManager.popBackStack()
    }
}
