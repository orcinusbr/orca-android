/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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
