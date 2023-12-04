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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.disposition.disposition
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image
import java.net.URL

/** Tag that identifies a [GalleryPreview] for testing purposes. */
internal const val GALLERY_PREVIEW_TAG = "gallery-preview"

/**
 * Grid in which thumbnails of the [attachments] are placed.
 *
 * @param attachments [Attachment]s whose [Image]s will be loaded from their respective [URL].
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
internal fun GalleryPreview(attachments: List<Attachment>, modifier: Modifier = Modifier) {
  GalleryPreview(Author.createSample(ImageLoader.Provider.createSample()), attachments, modifier)
}

/**
 * Grid in which thumbnails of the [attachments] are placed.
 *
 * @param author [Author] by which the [attachments] have been added.
 * @param attachments [Attachment]s whose [Thumbnail]s will be loaded from their respective [URL].
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
internal fun GalleryPreview(
  author: Author,
  attachments: List<Attachment>,
  modifier: Modifier = Modifier
) {
  val disposition = Disposition.of(attachments)

  Box(modifier.testTag(GALLERY_PREVIEW_TAG).semantics { this.disposition = disposition }) {
    disposition.Content(author)
  }
}

/** Preview of a [GalleryPreview] with a single thumbnail. */
@Composable
@MultiThemePreview
private fun SingleThumbnailGalleryPreviewPreview() {
  AutosTheme { GalleryPreview(Attachment.samples.take(1)) }
}

/** Preview of a [GalleryPreview] with 2 [Thumbnail]s. */
@Composable
@MultiThemePreview
private fun TwoThumbnailGalleryPreviewPreview() {
  AutosTheme { GalleryPreview(Attachment.samples.take(2)) }
}

/** Preview of a [GalleryPreview] with 3 thumbnails. */
@Composable
@MultiThemePreview
private fun ThreeThumbnailGalleryPreviewPreview() {
  AutosTheme { GalleryPreview(Attachment.samples.take(3)) }
}

/** Preview of a [GalleryPreview] with 3+ [Thumbnail]s. */
@Composable
@MultiThemePreview
private fun ThreePlusThumbnailGalleryPreviewPreview() {
  AutosTheme { GalleryPreview(Attachment.samples) }
}
