package com.jeanbarrossilva.mastodonte.app.feature.profiledetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetailsBoundary
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
