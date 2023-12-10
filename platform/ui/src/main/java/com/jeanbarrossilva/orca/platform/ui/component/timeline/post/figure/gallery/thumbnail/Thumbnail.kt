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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.thumbnail

import androidx.annotation.IntRange
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import com.jeanbarrossilva.orca.autos.forms.Forms
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.GalleryPreview
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.Image
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader

/** Tag that identifies a [Thumbnail] for testing purposes. */
internal const val THUMBNAIL_TAG = "thumbnail"

/** Default values used by a [Thumbnail]. */
internal object ThumbnailDefaults {
  /** [CornerBasedShape] by which a [Thumbnail] is clipped by default. */
  val shape
    @Composable get() = getShape(AutosTheme.forms)

  /**
   * Gets the [CornerBasedShape] by which a [Thumbnail] is clipped by default.
   *
   * @param forms [Forms] from which the [CornerBasedShape] will be obtained.
   */
  fun getShape(forms: Forms): CornerBasedShape {
    return forms.large.asShape
  }
}

/**
 * [Image] that's a preview of the content of the [attachment].
 *
 * @param author [Author] by which the [attachment] has been added.
 * @param attachment [Attachment] whose content's preview will be loaded.
 * @param position 1-based index of the position within a [GalleryPreview].
 * @param modifier [Modifier] to be applied the underlying [Image].
 * @param shape [Shape] by which it will be clipped.
 */
@Composable
internal fun Thumbnail(
  author: Author,
  attachment: Attachment,
  @IntRange(from = 1) position: Int,
  modifier: Modifier = Modifier,
  shape: Shape = ThumbnailDefaults.shape
) {
  Image(
    rememberImageLoader(attachment.url),
    contentDescription =
      stringResource(R.string.platform_ui_gallery_preview_thumbnail, position, author.name),
    modifier.testTag(THUMBNAIL_TAG).semantics { this.shape = shape },
    shape = shape
  )
}

/** Preview of a [Thumbnail]. */
@Composable
@MultiThemePreview
private fun ThumbnailPreview() {
  AutosTheme {
    Thumbnail(
      Author.createSample(ImageLoader.Provider.createSample()),
      Attachment.sample,
      position = 1
    )
  }
}
