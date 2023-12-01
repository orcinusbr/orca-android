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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity

/**
 * Relationship between the [post] and the [styles] applied to it.
 *
 * @param post [MastodonPostEntity] to which the [styles] are applied.
 * @param styles [Mastodon style entities][MastodonStyleEntity] that belong to the [post].
 */
internal data class MastodonPostWithStyles(
  @Embedded val post: MastodonPostEntity,
  @Relation(parentColumn = "id", entityColumn = "parent_id") val styles: List<MastodonStyleEntity>
)
