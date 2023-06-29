package com.jeanbarrossilva.mastodonte.app.feature.profiledetails

import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun ProfileDetailsModule(): Module {
    return module {
        single<ProfileDetailsBoundary> {
            NavControllerProfileDetailsBoundary(navController = get())
        }
    }
}
