/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.stat.details

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Creates [StatsDetails] from this [Post].
 *
 * @see asStatsDetailsFlow
 */
fun Post.asStatsDetails(): StatsDetails {
  return StatsDetails(
    comment.count,
    favorite.isEnabled,
    favorite.count,
    repost.isEnabled,
    repost.count
  )
}

/**
 * Creates a [Flow] to which [StatsDetails] that have been created from this [Post] are emitted when
 * one of this [Post]'s [Stat]s change.
 *
 * @see asStatsDetails
 */
fun Post.asStatsDetailsFlow(): Flow<StatsDetails> {
  return combine(
    comment.countFlow,
    favorite.countFlow.dependOn(favorite.isEnabledFlow),
    repost.countFlow.dependOn(repost.isEnabledFlow)
  ) { _, _, _ ->
    asStatsDetails()
  }
}
