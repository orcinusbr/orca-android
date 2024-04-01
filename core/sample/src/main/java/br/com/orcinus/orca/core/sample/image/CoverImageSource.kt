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

package br.com.orcinus.orca.core.sample.image

import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight

/**
 * [SampleImageSource] of [Post]s' [content][Post.content]s' [highlight][Content.highlight]s'
 * [headline][Highlight.headline]s' covers.
 */
sealed class CoverImageSource : SampleImageSource() {
  /** [CoverImageSource] of the default sample [Post]. */
  data object Default : CoverImageSource()

  /** [CoverImageSource] of a [Post] about Christian Selig's Pixel Pals app. */
  data object PixelPals : CoverImageSource()
}
