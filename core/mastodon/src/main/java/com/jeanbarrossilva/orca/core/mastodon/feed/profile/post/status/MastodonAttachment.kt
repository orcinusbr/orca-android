package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status

import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import java.net.URL
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that allows displaying media that's been attached to an
 * [MastodonStatus].
 *
 * @param previewUrl [String] URL that leads to the image to be shown as a preview.
 * @param description Describes the contents of the media.
 */
@Serializable
internal data class MastodonAttachment(val previewUrl: String, val description: String?) {
  /** Converts this [MastodonAttachment] into an [Attachment]. */
  fun toAttachment(): Attachment {
    return Attachment(description?.ifBlank { null }, URL(previewUrl))
  }
}
