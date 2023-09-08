package com.jeanbarrossilva.orca.core.feed.profile.toot.content

import java.net.URL

/**
 * Media that has been attached to [Content].
 *
 * @param description Description of what's displayed.
 * @param url [URL] that leads to the media.
 **/
data class Attachment(val description: String?, val url: URL)
