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

package com.jeanbarrossilva.orca.feature.gallery

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onParent
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.feature.gallery.test.assertIsZoomedIn
import com.jeanbarrossilva.orca.feature.gallery.test.assertIsZoomedOut
import com.jeanbarrossilva.orca.feature.gallery.test.onPage
import com.jeanbarrossilva.orca.feature.gallery.test.performScrollToPageAt
import com.jeanbarrossilva.orca.feature.gallery.test.performZoomIn
import com.jeanbarrossilva.orca.feature.gallery.test.performZoomOut
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.`if`
import org.junit.Rule
import org.junit.Test

internal class GalleryTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun swipesThroughEachAttachment() {
    composeRule
      .apply { setContent { Gallery() } }
      .run {
        repeat(Attachment.samples.size.dec()) { index ->
          onPage().`if`(index < Attachment.samples.lastIndex) {
            onParent().performScrollToPageAt(index)
          }
        }
      }
  }

  @Test
  fun zoomsIntoAndOutOfEachAttachment() {
    composeRule
      .apply { setContent { Gallery() } }
      .run {
        repeat(Attachment.samples.size) { index ->
          onPage().performZoomIn().assertIsZoomedIn().performZoomOut().assertIsZoomedOut().`if`(
            index < Attachment.samples.lastIndex
          ) {
            onParent().performScrollToPageAt(index)
          }
        }
      }
  }
}
