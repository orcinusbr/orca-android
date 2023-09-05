package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import java.net.URL
import kotlinx.serialization.Serializable

@Serializable
internal data class Card(
    val url: String,
    val title: String,
    val description: String,
    val image: String?
) {
    fun toHeadline(): Headline? {
        return image?.let {
            Headline(title, subtitle = description.ifEmpty { null }, coverURL = URL(image))
        }
    }
}
