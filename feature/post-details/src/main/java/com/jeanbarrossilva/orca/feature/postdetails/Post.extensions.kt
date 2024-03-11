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

package com.jeanbarrossilva.orca.feature.postdetails

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.composite.timeline.post.figure.Figure
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.composite.timeline.stat.details.asStatsDetails
import com.jeanbarrossilva.orca.composite.timeline.stat.details.asStatsDetailsFlow
import com.jeanbarrossilva.orca.composite.timeline.text.annotated.toAnnotatedString
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.std.image.compose.SomeComposableImageLoader
import java.net.URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Converts this [Post] into [PostDetails].
 *
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content is clicked.
 * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of clicks
 *   on thumbnails.
 * @see Actor.Authenticated
 * @see Actor.Authenticated.avatarLoader
 */
@Composable
internal fun Post.toPostDetails(
  onLinkClick: (URL) -> Unit = {},
  onThumbnailClickListener: Disposition.OnThumbnailClickListener =
    Disposition.OnThumbnailClickListener.empty
): PostDetails {
  return toPostDetails(AutosTheme.colors, onLinkClick, onThumbnailClickListener)
}

/**
 * Converts this [Post] into [PostDetails].
 *
 * @param colors [Colors] by which the emitted [PostDetails]' [PostDetails.text] can be colored.
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content is clicked.
 * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of clicks
 *   on thumbnails.
 * @see Actor.Authenticated
 * @see Actor.Authenticated.avatarLoader
 */
internal fun Post.toPostDetails(
  colors: Colors,
  onLinkClick: (URL) -> Unit = {},
  onThumbnailClickListener: Disposition.OnThumbnailClickListener =
    Disposition.OnThumbnailClickListener.empty
): PostDetails {
  return PostDetails(
    id,
    author.avatarLoader as SomeComposableImageLoader,
    author.name,
    author.account,
    content.text.toAnnotatedString(colors),
    Figure.of(id, author.name, content, onLinkClick, onThumbnailClickListener),
    publicationDateTime,
    asStatsDetails(),
    url
  )
}

/**
 * Converts this [Post] into a [Flow] of [PostDetails].
 *
 * @param colors [Colors] by which the emitted [PostDetails]' [PostDetails.text] can be colored.
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content is clicked.
 * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of clicks
 *   on thumbnails.
 */
internal fun Post.toPostDetailsFlow(
  colors: Colors,
  onLinkClick: (URL) -> Unit,
  onThumbnailClickListener: Disposition.OnThumbnailClickListener
): Flow<PostDetails> {
  return asStatsDetailsFlow().map {
    PostDetails(
      id,
      author.avatarLoader,
      author.name,
      author.account,
      content.text.toAnnotatedString(colors),
      Figure.of(id, author.name, content, onLinkClick, onThumbnailClickListener),
      publicationDateTime,
      it,
      url
    )
  }
}
