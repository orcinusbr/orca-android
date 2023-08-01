package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun TootDetailsModule(fragmentManager: FragmentManager, @IdRes containerID: Int): Module {
    return module {
        single<TootDetailsBoundary> {
            FragmentManagerTootDetailsBoundary(fragmentManager, containerID)
        }
    }
}
