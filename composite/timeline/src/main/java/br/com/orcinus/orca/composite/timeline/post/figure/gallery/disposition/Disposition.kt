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

package br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.GalleryPreview
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.thumbnail.Thumbnail
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.thumbnail.ThumbnailDefaults
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.`if`
import br.com.orcinus.orca.platform.autos.theme.AutosTheme

/** Indicates how a [GalleryPreview]'s [Thumbnail]s should be laid out. */
@Immutable
sealed class Disposition {
  /** [GalleryPreview] with the information to be previewed. */
  internal abstract val preview: GalleryPreview

  /** [OnThumbnailClickListener] that is notified of clicks on [Thumbnail]s. */
  internal abstract val onThumbnailClickListener: OnThumbnailClickListener

  /**
   * Listener to be notified of clicks on a [Thumbnail].
   *
   * @see onThumbnailClickListener
   */
  fun interface OnThumbnailClickListener {
    /**
     * Operation to be performed whenever a [Thumbnail] is clicked.
     *
     * @param postID ID of the [Post] to which the [Thumbnail] belongs.
     * @param entrypointIndex Index of the [entrypoint].
     * @param secondary [Attachment]s that aren't the ones displayed by the primary [Thumbnail].
     * @param entrypoint Copy of the [Thumbnail] that's been clicked on.
     */
    fun onThumbnailClickListener(
      postID: String,
      entrypointIndex: Int,
      secondary: List<Attachment>,
      entrypoint: @Composable (ContentScale, Modifier) -> Unit
    )

    companion object {
      /** No-op [OnThumbnailClickListener]. */
      val empty = OnThumbnailClickListener { _, _, _, _ -> }
    }
  }

  /**
   * Indicates that a [GalleryPreview] has a single [Thumbnail].
   *
   * @throws IllegalArgumentException If the [preview] contains more than one [Attachment].
   * @see GalleryPreview.attachments
   */
  internal data class Single
  @Throws(IllegalArgumentException::class)
  constructor(
    override val preview: GalleryPreview,
    override val onThumbnailClickListener: OnThumbnailClickListener = OnThumbnailClickListener.empty
  ) : Disposition() {
    init {
      require(preview.attachments.size == 1) { "Single disposition can only have one attachment." }
    }

    @Composable
    override fun Content(modifier: Modifier) {
      val attachment = remember(preview, preview.attachments::single)

      Thumbnail(
        preview.authorName,
        attachment,
        position = 1,
        onClick = {
          onThumbnailClickListener.onThumbnailClickListener(
            preview.postID,
            entrypointIndex = 0,
            listOf(attachment),
            entrypoint = it
          )
        },
        modifier.aspectRatio(FULL_RATIO).fillMaxWidth()
      )
    }
  }

  /**
   * Indicates that a [GalleryPreview] has multiple [Thumbnail]s and that those should be laid out
   * as a grid.
   */
  internal data class Grid(
    override val preview: GalleryPreview,
    override val onThumbnailClickListener: OnThumbnailClickListener = OnThumbnailClickListener.empty
  ) : Disposition() {
    /**
     * Provides a [Shape] for a non-leading [Thumbnail] that takes both the overall amount of
     * visible [Attachment]s and the position of the [Thumbnail] into account.
     *
     * @see provide
     */
    enum class SubsequentShapeProvider {
      /**
       * Provides a [Shape] for a [Thumbnail] that is the second one, after the leading and before
       * the trailing one.
       */
      SECOND_OF_THREE {
        @Composable
        override fun provide(): Shape {
          return ThumbnailDefaults.shape.withoutTopStart.withoutBottomEnd.withoutBottomStart
        }
      },

      /** Provides a [Shape] for a [Thumbnail] that is both the second and the trailing one. */
      TRAILING_OF_TWO {
        @Composable
        override fun provide(): Shape {
          return ThumbnailDefaults.shape.withoutTopStart.withoutBottomStart
        }
      },

      /** Provides a [Shape] for a [Thumbnail] that is both the third and the trailing one. */
      TRAILING_OF_THREE {
        @Composable
        override fun provide(): Shape {
          return ThumbnailDefaults.shape.withoutTopStart.withoutTopEnd.withoutBottomStart
        }
      };

      /** Provides the appropriate shape for a [Thumbnail]. */
      @Composable abstract fun provide(): Shape

      companion object {
        /**
         * Gets the appropriate [SubsequentShapeProvider] for both the [attachmentCount] and the
         * [position].
         *
         * @param attachmentCount Amount of [Attachment]s for which [Thumbnail]s will be laid out.
         * @param position Position of the [Thumbnail].
         */
        fun of(
          @IntRange(from = 2) attachmentCount: Int,
          @IntRange(from = 2, to = 3) position: Int
        ): SubsequentShapeProvider {
          require(attachmentCount >= 2) { "Amount of attachments should be >= 2." }
          require(position in 2..3) { "Position should be either 2 or 3." }
          val visibleAttachmentCount = minOf(3, attachmentCount)
          return when {
            position == 2 && visibleAttachmentCount == 2 -> TRAILING_OF_TWO
            position == 2 && visibleAttachmentCount == 3 -> SECOND_OF_THREE
            else -> TRAILING_OF_THREE
          }
        }
      }
    }

    init {
      require(preview.attachments.size > 1) { "Grid should be given at least two attachments." }
    }

    @Composable
    override fun Content(modifier: Modifier) {
      val postID = remember(preview, preview::postID)
      val authorName = remember(preview, preview::authorName)
      val attachments = remember(preview, preview::attachments)
      val spacing = AutosTheme.spacings.extraSmall.dp

      Row(modifier, Arrangement.spacedBy(spacing)) {
        Thumbnail(
          authorName,
          attachments.first(),
          position = 1,
          onClick = {
            onThumbnailClickListener.onThumbnailClickListener(
              postID,
              entrypointIndex = 0,
              preview.attachments.minusAt(0),
              entrypoint = it
            )
          },
          Modifier.fillMaxWidth(.5f).aspectRatio(LEADING_HALF_WIDTH_RATIO),
          ThumbnailDefaults.shape.withoutTopEnd.withoutBottomEnd
        )

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
          attachments.getOrNull(1)?.let { attachment ->
            Thumbnail(
              authorName,
              attachment,
              position = 2,
              onClick = { copy ->
                onThumbnailClickListener.onThumbnailClickListener(
                  postID,
                  entrypointIndex = 1,
                  preview.attachments.minusAt(1),
                  entrypoint = copy
                )
              },
              (Modifier as Modifier)
                .`if`(attachments.size > 2) { aspectRatio(TRAILING_APPROXIMATE_HALF_RATIO) }
                .`if`(attachments.size == 2) { aspectRatio(TRAILING_APPROXIMATE_HALF_WIDTH_RATIO) }
                .fillMaxWidth(),
              SubsequentShapeProvider.of(attachments.size, position = 2).provide()
            )
          }

          attachments.getOrNull(2)?.let { attachment ->
            val shape = SubsequentShapeProvider.of(attachments.size, position = 3).provide()

            Box(contentAlignment = Alignment.Center) {
              Thumbnail(
                authorName,
                attachment,
                position = 3,
                onClick = { copy ->
                  onThumbnailClickListener.onThumbnailClickListener(
                    postID,
                    entrypointIndex = 2,
                    attachments.minusAt(2),
                    entrypoint = copy
                  )
                },
                Modifier.fillMaxWidth().aspectRatio(TRAILING_APPROXIMATE_HALF_RATIO),
                shape
              )

              if (attachments.size > 3) {
                Box(
                  Modifier.clip(shape).background(Color.Black.copy(alpha = .5f)).matchParentSize()
                )

                Text(
                  stringResource(
                    R.string.composite_timeline_post_preview_gallery_thumbnail_over_count,
                    (attachments.size - 2).formatted
                  ),
                  Modifier.testTag(OVER_COUNT_TAG),
                  Color.White,
                  style = AutosTheme.typography.titleLarge
                )
              }
            }
          }
        }
      }
    }

    companion object {
      /** Tag that identifies a [Grid]'s [Content]'s [Thumbnail] over-count for testing purposes. */
      const val OVER_COUNT_TAG = "gallery-grid-disposition-over-count"
    }
  }

  /**
   * [Thumbnail]s laid out according to this [Disposition].
   *
   * @param modifier [Modifier] to be applied to the underlying [Composable].
   */
  @Composable abstract fun Content(modifier: Modifier)

  companion object {
    /** Aspect ratio of a [Thumbnail] that fills the [GalleryPreview]. */
    internal const val FULL_RATIO = 16f / 9f

    /**
     * Aspect ratio of a leading [Thumbnail] that has the same height as a [FULL_RATIO], but half
     * the width.
     */
    internal const val LEADING_HALF_WIDTH_RATIO = 8f / 9f

    /**
     * Aspect ratio of a [Thumbnail] that is approximately half of a [FULL_RATIO]. This estimate is
     * due to the spacing that a [Grid] has in between its [Thumbnail]s.
     */
    internal const val TRAILING_APPROXIMATE_HALF_RATIO = 8f / 4.52f

    /**
     * Aspect ratio of a trailing [Thumbnail] that has the same height as a [FULL_RATIO], but
     * approximately half the width. This estimate is due to the spacing that a [Grid] has in
     * between its [Thumbnail]s.
     */
    internal const val TRAILING_APPROXIMATE_HALF_WIDTH_RATIO = 8f / 9.2f

    /**
     * Gets the [Disposition] that's the most suitable for the [preview].
     *
     * @param preview [GalleryPreview] with the information to be previewed.
     * @param onThumbnailClickListener [OnThumbnailClickListener] that is notified of clicks on
     *   [Thumbnail]s.
     */
    internal fun of(
      preview: GalleryPreview,
      onThumbnailClickListener: OnThumbnailClickListener = OnThumbnailClickListener.empty
    ): Disposition {
      val attachments = preview.attachments
      require(attachments.isNotEmpty()) { "Cannot get disposition for an empty gallery." }
      return if (attachments.size == 1) Single(preview, onThumbnailClickListener)
      else Grid(preview, onThumbnailClickListener)
    }
  }
}
