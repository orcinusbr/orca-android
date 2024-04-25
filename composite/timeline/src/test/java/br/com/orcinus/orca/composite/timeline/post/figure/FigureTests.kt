/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.post.figure

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import br.com.orcinus.orca.composite.timeline.avatar.sample
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.GalleryPreview
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.content.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.core.withSample
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import java.net.URI
import kotlin.test.Test

internal class FigureTests {
  @Test
  fun createsGalleryFromContentWithHighlightAndAttachments() {
    assertThat(Figure.of(Posts.withSample.single().id, Author.sample.name, Content.sample))
      .isEqualTo(Figure.Gallery(GalleryPreview.sample))
  }

  @Test
  fun createsGalleryFromContentWithAttachmentsAndWithoutHighlight() {
    assertThat(
        Figure.of(
          Posts.withSample.single().id,
          Author.sample.name,
          Content.from(Domain.sample, text = Markdown(""), Attachment.samples) { null },
          onLinkClick = {}
        )
      )
      .isEqualTo(Figure.Gallery(GalleryPreview.sample))
  }

  @Test
  fun createsLinkFromContentWithHighlightAndWithoutAttachments() {
    val onLinkClick = { _: URI -> }
    assertThat(
        Figure.of(
          Posts.withSample.single().id,
          Author.sample.name,
          Content.from(Domain.sample, text = buildMarkdown { +Highlight.sample.uri.toString() }) {
            Headline.sample
          },
          onLinkClick
        )
      )
      .isNotNull()
      .isInstanceOf<Figure.Link>()
  }
}
