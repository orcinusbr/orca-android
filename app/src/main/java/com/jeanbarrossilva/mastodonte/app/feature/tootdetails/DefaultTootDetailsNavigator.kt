package com.jeanbarrossilva.mastodonte.app.feature.tootdetails

import com.jeanbarrossilva.mastodonte.app.feature.destinations.TootDetailsDestination
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsNavigator
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

internal class DefaultTootDetailsNavigator(
    private val destinationsNavigator: DestinationsNavigator
) : TootDetailsNavigator {
    override fun navigateToTootDetails(id: String) {
        val destination = TootDetailsDestination(id)
        destinationsNavigator.navigate(destination)
    }

    override fun pop() {
        destinationsNavigator.popBackStack()
    }
}
