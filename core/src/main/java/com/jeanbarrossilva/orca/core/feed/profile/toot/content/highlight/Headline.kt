package com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight

import java.net.URL

/**
 * Main information of a [Highlight].
 *
 * @param title Title of the external site.
 * @param subtitle Brief description of the content in the external site.
 * @param coverURL [URL] that leads to the image that represents the content.
 **/
data class Headline(val title: String, val subtitle: String?, val coverURL: URL) {
    companion object
}
