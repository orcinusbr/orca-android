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

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import kotlin.test.Test

internal class DispositionTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenGettingDispositionWithoutAttachments() {
    Disposition.of(emptyList())
  }

  @Test
  fun getsSingleDispositionForOneAttachment() {
    assertThat(Disposition.of(Attachment.samples.take(1)))
      .isEqualTo(Disposition.Single(Attachment.sample))
  }

  @Test
  fun getsGridDispositionForMultipleThumbnails() {
    assertThat(Disposition.of(Attachment.samples)).isEqualTo(Disposition.Grid(Attachment.samples))
  }
}
