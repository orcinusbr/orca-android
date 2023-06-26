package com.jeanbarrossilva.mastodonte.feature.tootdetails

interface TootDetailsNavigator {
    fun navigateToTootDetails(id: String)

    fun pop()
}
