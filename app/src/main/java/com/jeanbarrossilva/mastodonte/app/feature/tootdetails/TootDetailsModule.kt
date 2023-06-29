package com.jeanbarrossilva.mastodonte.app.feature.tootdetails

import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun TootDetailsModule(): Module {
    return module {
        single<TootDetailsBoundary> {
            NavControllerTootDetailsBoundary(navController = get())
        }
    }
}
