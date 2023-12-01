/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.feature.postdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Post] into a [Flow] of [PostDetails].
 *
 * @param colors [Colors] by which the emitted [PostDetails]' [PostDetails.text] can be colored.
 */
internal fun Post.toPostDetailsFlow(colors: Colors): Flow<PostDetails> {
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    repost.isEnabledFlow,
    repost.countFlow
  ) { commentCount, isFavorite, favoriteCount, isReblogged, reblogCount ->
    PostDetails(
      id,
      author.avatarLoader,
      author.name,
      author.account,
      content.text.toAnnotatedString(colors),
      content.highlight,
      publicationDateTime,
      commentCount,
      isFavorite,
      favoriteCount,
      isReblogged,
      reblogCount,
      url
    )
  }
}

/** Converts this [Post] into [PostDetails]. */
@Composable
internal fun Post.toPostDetails(): PostDetails {
  val commentCount by comment.countFlow.collectAsState()
  val isFavorite by favorite.isEnabledFlow.collectAsState()
  val favoriteCount by favorite.countFlow.collectAsState()
  val isReblogged by repost.isEnabledFlow.collectAsState()
  val reblogCount by repost.countFlow.collectAsState()
  return PostDetails(
    id,
    author.avatarLoader,
    author.name,
    author.account,
    content.text.toAnnotatedString(AutosTheme.colors),
    content.highlight,
    publicationDateTime,
    commentCount,
    isFavorite,
    favoriteCount,
    isReblogged,
    reblogCount,
    url
  )
}
