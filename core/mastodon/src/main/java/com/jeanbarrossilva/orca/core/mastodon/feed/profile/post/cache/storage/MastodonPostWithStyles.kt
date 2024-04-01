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
