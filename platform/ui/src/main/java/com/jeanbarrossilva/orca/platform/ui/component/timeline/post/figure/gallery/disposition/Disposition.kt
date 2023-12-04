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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.disposition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.`if`
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.GalleryPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.Thumbnail
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.ThumbnailDefaults
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.formatted

/** Indicates how a [GalleryPreview]'s [Thumbnail]s should be laid out. */
internal sealed class Disposition {
  /**
   * Indicates that a [GalleryPreview] has a single [Thumbnail].
   *
   * @param attachment [Attachment] for which a [Thumbnail] will be laid out.
   */
  data class Single(val attachment: Attachment) : Disposition() {
    @Composable
    override fun Content(modifier: Modifier, author: Author) {
      Thumbnail(author, attachment, position = 1, modifier.aspectRatio(FULL_RATIO).fillMaxWidth())
    }
  }

  /**
   * Indicates that a [GalleryPreview] has multiple [Thumbnail]s and that those should be laid out
   * as a grid.
   *
   * @param attachments [Attachment]s for which [Thumbnail]s will be laid out.
   */
  data class Grid(val attachments: List<Attachment>) : Disposition() {
    @Composable
    override fun Content(modifier: Modifier, author: Author) {
      val spacing = AutosTheme.spacings.small.dp

      Row(modifier, Arrangement.spacedBy(spacing)) {
        Thumbnail(
          author,
          attachments.first(),
          position = 1,
          Modifier.fillMaxWidth(.5f).aspectRatio(LEADING_HALF_WIDTH_RATIO)
        )

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
          attachments.getOrNull(1)?.let {
            Thumbnail(
              author,
              it,
              position = 2,
              (Modifier as Modifier)
                .`if`(attachments.size > 2) { aspectRatio(TRAILING_APPROXIMATE_HALF_RATIO) }
                .`if`(attachments.size == 2) { aspectRatio(TRAILING_APPROXIMATE_HALF_WIDTH_RATIO) }
                .fillMaxWidth()
            )
          }

          attachments.getOrNull(2)?.let {
            Box(contentAlignment = Alignment.Center) {
              Thumbnail(
                author,
                it,
                position = 3,
                Modifier.fillMaxWidth().aspectRatio(TRAILING_APPROXIMATE_HALF_RATIO)
              )

              if (attachments.size > 3) {
                Box(
                  Modifier.clip(ThumbnailDefaults.shape)
                    .background(Color.Black.copy(alpha = .5f))
                    .matchParentSize()
                )

                Text(
                  stringResource(
                    R.string.platform_ui_gallery_preview_thumbnail_over_count,
                    (attachments.size - 3).formatted
                  ),
                  color = Color.White,
                  style = AutosTheme.typography.titleLarge
                )
              }
            }
          }
        }
      }
    }
  }

  /**
   * [Thumbnail]s laid out according to this [Disposition].
   *
   * @param author [Author] by which the attachment(s) has/have been added.
   * @param modifier [Modifier] to be applied to the underlying [Composable].
   */
  @Composable
  fun Content(author: Author, modifier: Modifier = Modifier) {
    Content(modifier, author)
  }

  /**
   * [Thumbnail]s laid out according to this [Disposition].
   *
   * @param modifier [Modifier] to be applied to the underlying [Composable].
   * @param author [Author] by which the attachment(s) has/have been added.
   */
  @Composable protected abstract fun Content(modifier: Modifier, author: Author)

  companion object {
    /** Aspect ratio of a [Thumbnail] that fills the [GalleryPreview]. */
    const val FULL_RATIO = 16f / 9f

    /**
     * Aspect ratio of a [Thumbnail] that is approximately half of a [FULL_RATIO]. This estimate is
     * due to the spacing that a [Grid] has in between its [Thumbnail]s.
     */
    const val TRAILING_APPROXIMATE_HALF_RATIO = 8f / 4.52f

    /**
     * Aspect ratio of a leading [Thumbnail] that has the same height as a [FULL_RATIO], but half
     * the width.
     */
    const val LEADING_HALF_WIDTH_RATIO = 8f / 9f

    /**
     * Aspect ratio of a trailing [Thumbnail] that has the same height as a [FULL_RATIO], but
     * approximately half the width. This estimate is due to the spacing that a [Grid] has in
     * between its [Thumbnail]s.
     */
    const val TRAILING_APPROXIMATE_HALF_WIDTH_RATIO = 8f / 9.4f

    /**
     * Gets the [Disposition] that's the most suitable for the [attachments].
     *
     * @param attachments [Attachment] for which [Thumbnail]s will be laid out.
     */
    fun of(attachments: List<Attachment>): Disposition {
      require(attachments.isNotEmpty()) { "Cannot get disposition for an empty gallery." }
      return if (attachments.size == 1) Single(attachments.single()) else Grid(attachments)
    }
  }
}
