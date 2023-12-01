/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
