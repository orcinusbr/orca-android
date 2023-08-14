package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsBoundary
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class FragmentManagerTootDetailsBoundary(private val navigator: Navigator) :
    TootDetailsBoundary {
    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(navigator, id)
    }

    override fun pop() {
        navigator.pop()
    }
}
