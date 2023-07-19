package com.jeanbarrossilva.mastodonte.feature.feed

interface FeedBoundary {
    fun navigateToSearch()

    fun navigateToTootDetails(id: String)

    fun navigateToComposer()
}
