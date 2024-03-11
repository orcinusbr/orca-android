/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.composite.timeline.stat.details

import androidx.compose.runtime.Immutable
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.platform.core.withSample

/**
 * Details of a [Post]'s [Stat]s.
 *
 * @param commentCount Amount of comments.
 * @param isFavorite Whether it's marked as favorite.
 * @param favoriteCount Amount of times it's been marked as favorite.
 * @param isReposted Whether it's reposted.
 * @param repostCount Amount of times it's been reposted.
 */
@Immutable
data class StatsDetails
internal constructor(
  private val commentCount: Int,
  val isFavorite: Boolean,
  private val favoriteCount: Int,
  val isReposted: Boolean,
  private val repostCount: Int
) {
  /** Formatted, displayable version of [commentCount]. */
  val formattedCommentCount = commentCount.formatted

  /** Formatted, displayable version of [favoriteCount]. */
  val formattedFavoriteCount = favoriteCount.formatted

  /** Formatted, displayable version of [repostCount]. */
  val formattedReblogCount = repostCount.formatted

  companion object {
    /** [StatsDetails] with zeroed counts. */
    val Empty =
      StatsDetails(
        commentCount = 0,
        isFavorite = false,
        favoriteCount = 0,
        isReposted = false,
        repostCount = 0
      )

    /** Sample [StatsDetails]. */
    val sample = Posts.withSample.single().asStatsDetails()
  }
}
