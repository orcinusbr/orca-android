package com.jeanbarrossilva.orca.feature.feed

interface FeedBoundary {
    fun navigateToSearch()

    fun navigateToTootDetails(id: String)

    fun navigateToComposer()
}
