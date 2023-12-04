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

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.disposition.test.assertAspectRatioEquals
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.test.assertDispositionIs
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.test.onGalleryPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.thumbnail.test.onThumbnails
import org.junit.Rule
import org.junit.Test

internal class GalleryPreviewTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun containsOneColumnWhenGivenSingleAttachment() {
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples.take(1)) } }
    composeRule.onGalleryPreview().assertDispositionIs<Disposition.Single>()
  }

  @Test
  fun containsTwoColumnsWhenGivenMultipleAttachments() {
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples.take(2)) } }
    composeRule.onGalleryPreview().assertDispositionIs<Disposition.Grid>()
  }

  @Test
  fun describesLeadingThumbnailContent() {
    val context = InstrumentationRegistry.getInstrumentation().context
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples) } }
    composeRule
      .onThumbnails()
      .onFirst()
      .assertContentDescriptionEquals(
        context.getString(R.string.platform_ui_gallery_preview_thumbnail, 1, Author.sample.name)
      )
  }

  @Test
  fun describesSecondThumbnailContent() {
    val context = InstrumentationRegistry.getInstrumentation().context
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples) } }
    composeRule
      .onThumbnails()[1]
      .assertContentDescriptionEquals(
        context.getString(R.string.platform_ui_gallery_preview_thumbnail, 2, Author.sample.name)
      )
  }

  @Test
  fun describesLastThumbnailContent() {
    val context = InstrumentationRegistry.getInstrumentation().context
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples) } }
    composeRule
      .onThumbnails()
      .onLast()
      .assertContentDescriptionEquals(
        context.getString(R.string.platform_ui_gallery_preview_thumbnail, 3, Author.sample.name)
      )
  }

  @Test
  fun leadingThumbnailHasHalfTheFullWidthWhenGivenTwoAttachments() {
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples.take(2)) } }
    composeRule
      .onThumbnails()
      .onFirst()
      .assertAspectRatioEquals(Disposition.LEADING_HALF_WIDTH_RATIO)
  }

  @Test
  fun trailingThumbnailHasHalfTheFullWidthWhenGivenTwoAttachments() {
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples.take(2)) } }
    composeRule
      .onThumbnails()
      .onLast()
      .assertAspectRatioEquals(Disposition.TRAILING_APPROXIMATE_HALF_WIDTH_RATIO)
  }

  @Test
  fun secondThumbnailHasHalfTheFullSizeWhenGivenMoreThanTwoAttachments() {
    composeRule.setContent { AutosTheme { GalleryPreview(Attachment.samples) } }
    composeRule
      .onThumbnails()[1]
      .assertAspectRatioEquals(Disposition.TRAILING_APPROXIMATE_HALF_RATIO)
  }
}
