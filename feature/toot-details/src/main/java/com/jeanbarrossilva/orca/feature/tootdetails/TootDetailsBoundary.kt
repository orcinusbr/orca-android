package com.jeanbarrossilva.orca.feature.tootdetails

import java.net.URL

interface TootDetailsBoundary {
    fun navigateTo(url: URL)

    fun navigateToTootDetails(id: String)

    fun pop()
}
