package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.ProfileDetailsModule
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener

internal object TestProfileDetailsModule :
  ProfileDetailsModule(
    { Instance.sample.profileProvider },
    { Instance.sample.tootProvider },
    { TestProfileDetailsBoundary() },
    { OnBottomAreaAvailabilityChangeListener.empty }
  )
