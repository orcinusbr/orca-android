/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.post

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.post.figure.Figure
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.thumbnail.Thumbnail
import br.com.orcinus.orca.composite.timeline.stat.details.asStatsDetails
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated.toAnnotatedString
import br.com.orcinus.orca.std.image.compose.SomeComposableImageLoader
import java.net.URI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Post] into a [Flow] of [PostPreview].
 *
 * @param colors [Colors] by which the emitted [PostPreview]s' [PostPreview.text] can be colored.
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content is clicked.
 * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of clicks
 *   on [Thumbnail]s.
 */
fun Post.toPostPreviewFlow(
  colors: Colors,
  onLinkClick: (URI) -> Unit,
  onThumbnailClickListener: Disposition.OnThumbnailClickListener
): Flow<PostPreview> {
  return combine(comment.countFlow, favorite.countFlow, repost.countFlow) { _, _, _ ->
    toPostPreview(colors, onLinkClick, onThumbnailClickListener)
  }
}

/**
 * Converts this [Post] into a [PostPreview].
 *
 * @param colors [Colors] by which the resulting [PostPreview]'s [PostPreview.text] can be colored.
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content is clicked.
 * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of clicks
 *   on [Thumbnail]s.
 */
fun Post.toPostPreview(
  colors: Colors,
  onLinkClick: (URI) -> Unit = {},
  onThumbnailClickListener: Disposition.OnThumbnailClickListener =
    Disposition.OnThumbnailClickListener.empty
): PostPreview {
  return PostPreview(
    id,
    author.avatarLoader as SomeComposableImageLoader,
    author.name,
    author.account,
    if (this is Repost) reposter.name else null,
    content.text.toAnnotatedString(colors),
    Figure.of(id, author.name, content, onLinkClick, onThumbnailClickListener),
    publicationDateTime,
    asStatsDetails(),
    uri
  )
}
