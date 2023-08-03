package com.jeanbarrossilva.orca.app.module.feature.search

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.search.SearchBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun SearchModule(fragmentManager: FragmentManager, @IdRes containerID: Int): Module {
    return module {
        single<SearchBoundary> {
            FragmentManagerSearchBoundary(fragmentManager, containerID)
        }
    }
}
