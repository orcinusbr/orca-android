package com.jeanbarrossilva.mastodon.feature.profile

import java.net.URL

interface ProfileNavigator {
    fun navigateToWebpage(url: URL)

    fun navigateToTootDetails(id: String)
}
