package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun ProfileDetailsModule(navigator: Navigator): Module {
    return module {
        single<ProfileDetailsBoundary> {
            NavigatorProfileDetailsBoundary(androidContext(), navigator)
        }
    }
}
