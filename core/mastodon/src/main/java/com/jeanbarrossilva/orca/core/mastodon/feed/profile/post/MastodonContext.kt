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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that represents a thread of [MastodonStatus]es.
 *
 * @param ancestors [MastodonStatus]es to which the referred one is a comment.
 * @param descendants [MastodonStatus]es that have been published as comments to the one this
 *   [MastodonContext] refers to.
 */
@Serializable
internal data class MastodonContext(
  val ancestors: List<MastodonStatus>,
  val descendants: List<MastodonStatus>
)
