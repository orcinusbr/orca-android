package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun TootDetailsModule(navigator: Navigator): Module {
    return module {
        single<TootDetailsBoundary> {
            NavigatorTootDetailsBoundary(androidContext(), navigator)
        }
    }
}
