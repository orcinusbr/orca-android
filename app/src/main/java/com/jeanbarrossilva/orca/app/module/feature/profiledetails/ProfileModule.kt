package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun ProfileDetailsModule(fragmentManager: FragmentManager, @IdRes containerID: Int):
    Module {
    return module {
        single<ProfileDetailsBoundary> {
            FragmentManagerProfileDetailsBoundary(fragmentManager, containerID)
        }
    }
}
