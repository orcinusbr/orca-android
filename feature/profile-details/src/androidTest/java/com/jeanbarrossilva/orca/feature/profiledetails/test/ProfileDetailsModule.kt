package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun ProfileDetailsModule(): Module {
    return module {
        single { InstanceProvider.sample }
        single<ProfileDetailsBoundary> { TestProfileDetailsBoundary() }
        single { OnBottomAreaAvailabilityChangeListener.empty }
    }
}
