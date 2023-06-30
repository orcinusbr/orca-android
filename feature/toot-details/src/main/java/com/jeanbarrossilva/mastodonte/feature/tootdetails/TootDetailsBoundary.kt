package com.jeanbarrossilva.mastodonte.feature.tootdetails

interface TootDetailsBoundary {
    fun navigateToTootDetails(id: String)

    fun pop()
}
