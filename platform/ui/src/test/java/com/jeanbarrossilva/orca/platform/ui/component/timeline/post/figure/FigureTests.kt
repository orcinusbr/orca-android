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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.GalleryPreview
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString
import java.net.URL
import kotlin.test.Test

internal class FigureTests {
  @Test
  fun createsGalleryFromContentWithHighlightAndAttachments() {
    assertThat(Figure.of(Post.sample.id, Author.sample.name, Content.sample))
      .isEqualTo(Figure.Gallery(GalleryPreview.sample))
  }

  @Test
  fun createsGalleryFromContentWithAttachmentsAndWithoutHighlight() {
    assertThat(
        Figure.of(
          Post.sample.id,
          Author.sample.name,
          Content.from(Domain.sample, text = StyledString(""), Attachment.samples) { null },
          onLinkClick = {}
        )
      )
      .isEqualTo(Figure.Gallery(GalleryPreview.sample))
  }

  @Test
  fun createsLinkFromContentWithHighlightAndWithoutAttachments() {
    val onLinkClick = { _: URL -> }
    assertThat(
        Figure.of(
          Post.sample.id,
          Author.sample.name,
          Content.from(
            Domain.sample,
            text = buildStyledString { +Highlight.sample.url.toString() }
          ) {
            Headline.sample
          },
          onLinkClick
        )
      )
      .isNotNull()
      .isInstanceOf<Figure.Link>()
  }
}
