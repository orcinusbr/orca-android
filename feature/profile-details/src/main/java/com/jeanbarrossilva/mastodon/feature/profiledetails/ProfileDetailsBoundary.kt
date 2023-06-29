package com.jeanbarrossilva.mastodon.feature.profiledetails

import java.net.URL

interface ProfileDetailsBoundary {
    fun navigateToWebpage(url: URL)

    fun navigateToTootDetails(id: String)

    companion object {
        internal val empty = object : ProfileDetailsBoundary {
            override fun navigateToWebpage(url: URL) {
            }

            override fun navigateToTootDetails(id: String) {
            }
        }
    }
}
