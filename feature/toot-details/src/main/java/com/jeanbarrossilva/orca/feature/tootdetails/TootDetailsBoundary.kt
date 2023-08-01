package com.jeanbarrossilva.orca.feature.tootdetails

interface TootDetailsBoundary {
    fun navigateToTootDetails(id: String)

    fun pop()
}
