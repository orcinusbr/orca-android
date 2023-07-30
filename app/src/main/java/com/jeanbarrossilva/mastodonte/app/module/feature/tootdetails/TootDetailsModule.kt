package com.jeanbarrossilva.mastodonte.app.module.feature.tootdetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsBoundary
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MainTootDetailsModule(fragmentManager: FragmentManager, @IdRes containerID: Int):
    Module {
    return TootDetailsModule {
        FragmentManagerTootDetailsBoundary(fragmentManager, containerID)
    }
}

@Suppress("FunctionName")
internal fun TootDetailsModule(boundary: Definition<TootDetailsBoundary>): Module {
    return module {
        single(definition = boundary)
    }
}
