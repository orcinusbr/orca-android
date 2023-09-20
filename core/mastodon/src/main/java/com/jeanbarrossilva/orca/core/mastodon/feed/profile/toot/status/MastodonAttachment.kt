package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Attachment
import java.net.URL
import kotlinx.serialization.Serializable

@Serializable
internal data class MastodonAttachment(val previewUrl: String, val description: String?) {
    private val coreAttachment by lazy {
        Attachment(description?.ifBlank { null }, URL(previewUrl))
    }

    fun toAttachment(): Attachment {
        return coreAttachment
    }
}
