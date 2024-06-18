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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.status

import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import java.net.URI
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that allows displaying media that's been attached to an
 * [MastodonStatus].
 *
 * @property previewUrl [String] URI that leads to the image to be shown as a preview.
 * @property description Describes the contents of the media.
 */
@Serializable
internal data class MastodonAttachment(
  private val previewUrl: String,
  private val description: String?
) {
  /** Converts this [MastodonAttachment] into an [Attachment]. */
  fun toAttachment(): Attachment {
    return Attachment(description?.ifBlank { null }, URI(previewUrl))
  }
}
