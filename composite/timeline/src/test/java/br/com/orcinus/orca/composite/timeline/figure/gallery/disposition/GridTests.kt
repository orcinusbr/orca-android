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

package br.com.orcinus.orca.composite.timeline.figure.gallery.disposition

import br.com.orcinus.orca.composite.timeline.post.figure.gallery.GalleryPreview
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import kotlin.test.Test

internal class GridTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenCreatingGridWithLessThanTwoAttachments() {
    Disposition.Grid(GalleryPreview.sample.copy(attachments = Attachment.samples.take(1)))
  }

  @Test
  fun createsGridWithTwoAttachments() {
    Disposition.Grid(GalleryPreview.sample.copy(attachments = Attachment.samples.take(2)))
  }

  @Test
  fun createsGridWithMoreThanTwoAttachments() {
    Disposition.Grid(GalleryPreview.sample.copy(attachments = Attachment.samples))
  }
}
