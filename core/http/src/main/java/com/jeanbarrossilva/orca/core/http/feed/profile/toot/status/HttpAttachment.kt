package com.jeanbarrossilva.orca.core.http.feed.profile.toot.status

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Attachment
import java.net.URL
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that allows displaying media that's been attached to an
 * [HttpStatus].
 *
 * @param previewUrl [String] URL that leads to the image to be shown as a preview.
 * @param description Describes the contents of the media.
 **/
@Serializable
internal data class HttpAttachment(val previewUrl: String, val description: String?) {
    /** Converts this [HttpAttachment] into an [Attachment]. **/
    fun toAttachment(): Attachment {
        return Attachment(description?.ifBlank { null }, URL(previewUrl))
    }

    companion object
}
