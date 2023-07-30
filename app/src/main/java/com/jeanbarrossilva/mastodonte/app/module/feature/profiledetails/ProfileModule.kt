package com.jeanbarrossilva.mastodonte.app.module.feature.profiledetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetailsBoundary
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MainProfileDetailsModule(fragmentManager: FragmentManager, @IdRes containerID: Int):
    Module {
    return ProfileDetailsModule {
        FragmentManagerProfileDetailsBoundary(fragmentManager, containerID)
    }
}

@Suppress("FunctionName")
internal fun ProfileDetailsModule(boundary: Definition<ProfileDetailsBoundary>): Module {
    return module {
        single(definition = boundary)
    }
}
