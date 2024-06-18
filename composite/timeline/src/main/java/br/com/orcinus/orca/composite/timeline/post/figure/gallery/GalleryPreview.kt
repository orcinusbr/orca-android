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

package br.com.orcinus.orca.composite.timeline.post.figure.gallery

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.disposition
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.thumbnail.Thumbnail
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.core.withSample

/** Tag that identifies a [GalleryPreview] for testing purposes. */
internal const val GALLERY_PREVIEW_TAG = "gallery-preview"

/**
 * Information to be previewed of a [Post]'s [Attachment]s.
 *
 * @param postID ID of the [Post] to which the [attachments] belong.
 * @param authorName Name of the [Author] by which the [Post] was published.
 * @param attachments [Attachment]s to be previewed.
 */
data class GalleryPreview(
  val postID: String,
  val authorName: String,
  val attachments: List<Attachment>
) {
  companion object {
    /** Sample [GalleryPreview]. */
    internal val sample = Posts.withSample.single().asGalleryPreview()
  }
}

/**
 * Grid in which thumbnails of the [preview]'s [Attachment]s are placed.
 *
 * @param preview [GalleryPreview] with the information to be previewed.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 * @see GalleryPreview.attachments
 */
@Composable
internal fun GalleryPreview(
  modifier: Modifier = Modifier,
  preview: GalleryPreview = GalleryPreview.sample
) {
  GalleryPreview(preview, Disposition.OnThumbnailClickListener.empty, modifier)
}

/**
 * Grid in which thumbnails of the [preview]'s [Attachment]s are placed.
 *
 * @param preview [GalleryPreview] with the information to be previewed.
 * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of clicks
 *   on [Thumbnail]s.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 * @see GalleryPreview.attachments
 */
@Composable
internal fun GalleryPreview(
  preview: GalleryPreview,
  onThumbnailClickListener: Disposition.OnThumbnailClickListener,
  modifier: Modifier = Modifier
) {
  val disposition = Disposition.of(preview, onThumbnailClickListener)

  Box(modifier.testTag(GALLERY_PREVIEW_TAG).semantics { this.disposition = disposition }) {
    with(disposition) { Content(Modifier) }
  }
}

/** Preview of a [GalleryPreview] with a single thumbnail. */
@Composable
@MultiThemePreview
private fun SingleThumbnailGalleryPreviewPreview() {
  AutosTheme {
    GalleryPreview(preview = GalleryPreview.sample.copy(attachments = Attachment.samples.take(1)))
  }
}

/** Preview of a [GalleryPreview] with 2 [Thumbnail]s. */
@Composable
@MultiThemePreview
private fun TwoThumbnailGalleryPreviewPreview() {
  AutosTheme {
    GalleryPreview(preview = GalleryPreview.sample.copy(attachments = Attachment.samples.take(2)))
  }
}

/** Preview of a [GalleryPreview] with 3 thumbnails. */
@Composable
@MultiThemePreview
private fun ThreeThumbnailGalleryPreviewPreview() {
  AutosTheme {
    GalleryPreview(preview = GalleryPreview.sample.copy(attachments = Attachment.samples.take(3)))
  }
}

/** Preview of a [GalleryPreview] with 3+ [Thumbnail]s. */
@Composable
@MultiThemePreview
private fun ThreePlusThumbnailGalleryPreviewPreview() {
  AutosTheme {
    GalleryPreview(preview = GalleryPreview.sample.copy(attachments = Attachment.samples))
  }
}
