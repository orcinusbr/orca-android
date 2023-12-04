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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.Figure
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import java.net.URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Post] into a [Flow] of [PostPreview].
 *
 * @param colors [Colors] by which the emitted [PostPreview]s' [PostPreview.text] can be colored.
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content gets clicked.
 */
fun Post.toPostPreviewFlow(colors: Colors, onLinkClick: (URL) -> Unit): Flow<PostPreview> {
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    repost.isEnabledFlow,
    repost.countFlow
  ) { _, _, _, _, _ ->
    toPostPreview(colors, onLinkClick)
  }
}

/**
 * Converts this [Post] into a [PostPreview].
 *
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content gets clicked.
 */
@Composable
internal fun Post.toPostPreview(onLinkClick: (URL) -> Unit): PostPreview {
  return toPostPreview(AutosTheme.colors, onLinkClick)
}

/**
 * Converts this [Post] into a [PostPreview].
 *
 * @param colors [Colors] by which the resulting [PostPreview]'s [PostPreview.text] can be colored.
 * @param onLinkClick Lambda to be run whenever the [Figure.Link]'s content gets clicked.
 */
internal fun Post.toPostPreview(colors: Colors, onLinkClick: (URL) -> Unit): PostPreview {
  return PostPreview(
    id,
    author.avatarLoader,
    author.name,
    author.account,
    if (this is Repost) reposter.name else null,
    content.text.toAnnotatedString(colors),
    Figure.of(content, onLinkClick),
    publicationDateTime,
    comment.count,
    favorite.isEnabled,
    favorite.count,
    repost.isEnabled,
    repost.count,
    url
  )
}
