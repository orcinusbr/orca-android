package com.jeanbarrossilva.mastodon.feature.profile

import java.net.URL

interface ProfileNavigator {
    fun navigateToWebpage(url: URL)

    fun navigateToTootDetails(id: String)

    companion object {
        internal val empty = object : ProfileNavigator {
            override fun navigateToWebpage(url: URL) {
            }

            override fun navigateToTootDetails(id: String) {
            }
        }
    }
}
